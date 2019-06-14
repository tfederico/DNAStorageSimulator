package com.federico.target_tracker.simulator.simulationCase;

import com.federico.target_tracker.bionanothings.Beacon;
import com.federico.target_tracker.logger.Logger;
import com.federico.target_tracker.utils.ImpossibleException;
import com.federico.target_tracker.utils.WrongHeightException;
import javafx.geometry.Point2D;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by federico on 16/06/16.
 */

/**
 *Class that redefines the DestinationSimulationCase behaviour adding a parameter, which is the height of the triangle designed by the beacons, and uses a DestinationMovementAlgorithm for its receivers
 */
public class FixedHeightSimulationCase extends DestinationSimulationCase {

    /**
     * Height of the triangle plotted by the beacons
     */
    private static double triangleHeight;

    /**
     * Maximum height of the triangle designed by beacons
     */
    private static double maximumTriangleHeight;

    /**
     * Constructor of the FixedHeightSimulationCase class
     * @param logger Logger used to store the information about the simulation
     * @param inOrOut Argument that tells if the destination must be placed inside or outside the triangle designed by beacons
     */
    public FixedHeightSimulationCase(Logger logger, boolean inOrOut, double height) throws WrongHeightException {
        super(logger, inOrOut);
        maximumTriangleHeight = L;
        setTriangleHeight(height);
    }

    /**
     * Method that defines the algorithm used to place the beacons in the simulation area. In this case, two beacons are placed at two random point and the third one must be such far from the line that passes from the two points as triangleHeight attribute
     */
    @Override
    protected void placeBeacons() throws ImpossibleException {
        boolean stopPlacing = false;
        int maxIt = 0;
        while(!stopPlacing && maxIt<999){
            beaconsList.clear();
            boolean done = false;
            while (!done){
                for(int i = 0; i < beacons-1; i++)
                    beaconsList.add(new Beacon(receivers + 3 + i,Math.random()-L/2,Math.random()-L/2));

                double m = (beaconsList.get(0).getPosition().getY()-beaconsList.get(1).getPosition().getY())/
                        (beaconsList.get(0).getPosition().getX()-beaconsList.get(1).getPosition().getX());
                double q = beaconsList.get(0).getPosition().getY() - m*beaconsList.get(0).getPosition().getX();

                double qUpper = q + triangleHeight * Math.sqrt(Math.pow(m,2)+1);
                double qLower = q - triangleHeight * Math.sqrt(Math.pow(m,2)+1);

                double yUpper = m * beaconsList.get(0).getPosition().getX() + qUpper;
                double yLower = m *  beaconsList.get(0).getPosition().getX() + qLower;

                if((yUpper <= L/2 && yUpper >= -L/2) || (yLower <= L/2 && yLower >= -L/2))
                    done = true;
                else
                    beaconsList.clear();
            }

            ensureMinimumTriangleHeight();

            boolean ok = false;
            boolean in = true;
            int i = 0; //max number of attemps to enlarge the base

            while(!ok && in && i < 99){
                Point2D v0 = beaconsList.get(0).getPosition();
                Point2D v1 = beaconsList.get(1).getPosition();
                Point2D v2 = beaconsList.get(2).getPosition();
                ok = checkTriangleHeight(v1,v2,v0) && checkTriangleHeight(v2,v0,v1); //every height is >= triangleHeight
                if(!ok) {
                    if(v0.getX() == v1.getX())
                        in = enlargeVerticalBase();
                    else
                        in = enlargeBase();
                    i++;
                }

            }

            if(in && i < 99)
                stopPlacing = true;
            else //point outside square or not found using less than 100 iterations
                maxIt++;

        }

        if (maxIt>=999)
            throw new ImpossibleException();

        for(Beacon b : beaconsList){
            b.emitAttractants(shouldAllEmitsAttractans);
            b.emitRepellents(shouldAllEmitsRepellents);
        }

    }

    /**
     * Method used to set up the height of the triangle plotted by beacons. It throws a WrongHeightException if the height is not a positive number or if it is greater than maximumTriangleHeight
     * @param height Height of the triangle plotted by beacons
     * @throws WrongHeightException
     */
    public static void setTriangleHeight(double height) throws WrongHeightException {
        if(height < maximumTriangleHeight && height > 0)
            triangleHeight = height;
        else
            throw new WrongHeightException();
    }

    /**
     * Method which ensures that the minimum height of the triangle is greater than triangleHeight
     */
    private void ensureMinimumTriangleHeight(){

        double m = (beaconsList.get(0).getPosition().getY()-beaconsList.get(1).getPosition().getY())/
                (beaconsList.get(0).getPosition().getX()-beaconsList.get(1).getPosition().getX());
        double q = beaconsList.get(0).getPosition().getY() - m*beaconsList.get(0).getPosition().getX();

        boolean done = false;
        double coefficient = triangleHeight * Math.sqrt(Math.pow(m,2)+1);
        double q2 = q + coefficient; //upper parallel line
        double randomX = Double.MIN_VALUE, randomY = Double.MIN_VALUE;

        while(!done){
            randomX = ThreadLocalRandom.current().nextDouble(-L/2,L/2+Double.MIN_VALUE);
            randomY = m*randomX + q2;
            if(randomY > L/2 || randomY < -L/2){
                q2 = q - coefficient;
                randomY = m*randomX + q2;
                if(randomY <= L/2 && randomY >= -L/2) //lower parallel line
                    done = true;
                else
                    q2 = q + coefficient;
            }
            else
                done = true;

        }

        beaconsList.add(new Beacon(receivers + 3 + beacons, randomX, randomY));

    }

    /**
     * Method which enlarges the base of the triangle designed by beacons in order to ensure that the minimum height of the triangle is greater than the attribute triangleHeight. It return a boolean value which says if enlarging the base has caused the leak of a beacon by simulation area
     * @return  boolean
     */
    private boolean enlargeBase(){

        double m = (beaconsList.get(0).getPosition().getY()-beaconsList.get(1).getPosition().getY())/
                (beaconsList.get(0).getPosition().getX()-beaconsList.get(1).getPosition().getX());
        double q = beaconsList.get(0).getPosition().getY() - m*beaconsList.get(0).getPosition().getX();

        int lefterX = beaconsList.get(0).getPosition().getX() < beaconsList.get(1).getPosition().getX() ? 0 : 1;
        int farthestFromSide = Math.abs(new Point2D(beaconsList.get(lefterX).getPosition().getX(),0).distance(-L/2,0)) >
                Math.abs(new Point2D(beaconsList.get(1 - lefterX).getPosition().getX(),0).distance(L/2,0)) ? lefterX : 1 - lefterX;

        double randomX = 0;
        if (lefterX==farthestFromSide){ //lefter beacon is the farthest beacon
            try {
                randomX = ThreadLocalRandom.current().nextDouble(-L / 2, beaconsList.get(farthestFromSide)
                        .getPosition().getX() + Double.MIN_VALUE);
            }
            catch (IllegalArgumentException i){

                System.out.println(-L/2);
                System.out.println(beaconsList.get(farthestFromSide).getPosition().getX() + Double.MIN_VALUE);
                System.out.println(beaconsList.get(1 - farthestFromSide).getPosition().getX() + Double.MIN_VALUE);
                throw i;
            }
        }
        else{//righter beacon is the farest
            randomX = ThreadLocalRandom.current().nextDouble(beaconsList.get(farthestFromSide)
                    .getPosition().getX(),L/2+Double.MIN_VALUE);
        }
        double randomY = m * randomX + q;
        if (randomY < -L/2  || randomY > L/2)
            return false;
        else{
            beaconsList.remove(farthestFromSide);
            beaconsList.add(farthestFromSide, new Beacon(receivers + 3 + farthestFromSide,randomX,randomY));
            return true;
        }
    }

    /**
     * Method which enlarges the base of the triangle designed by beacons in order to ensure that the minimum height of the triangle is greater than the attribute triangleHeight. It is used when the base is a vertical line and it returns a boolean value which says if enlarging the base has caused the leak of a beacon by simulation area
     * @return  boolean
     */
    private boolean enlargeVerticalBase(){

        int lowerY = beaconsList.get(0).getPosition().getY() < beaconsList.get(1).getPosition().getY() ? 0 : 1;
        int farthestFromSide = Math.abs(-L/2+beaconsList.get(lowerY).getPosition().getX()) >
                Math.abs(L/2-beaconsList.get(1 - lowerY).getPosition().getX()) ? lowerY : 1 - lowerY;

        double randomY = 0;
        if (lowerY==farthestFromSide){ //lefter beacon is the farest beacon
            randomY = ThreadLocalRandom.current().nextDouble(-L/2,beaconsList.get(farthestFromSide)
                    .getPosition().getY()+Double.MIN_VALUE);
        }
        else{//righter beacon is the farest
            randomY = ThreadLocalRandom.current().nextDouble(beaconsList.get(farthestFromSide)
                    .getPosition().getY(),L/2+Double.MIN_VALUE);
        }

        double notRandomX = beaconsList.get(0).getPosition().getX();
        if (randomY < -L/2  || randomY > L/2)
            return false;
        else{
            beaconsList.remove(farthestFromSide);
            beaconsList.add(farthestFromSide, new Beacon(receivers + 3 + farthestFromSide,notRandomX,randomY));
            return true;
        }
    }

    /**
     * Method which checks if the height of the triangle designed by beacons is greater than the attribute triangleHeight
     * @param v0 First vertex of the triangle
     * @param v1 Second vertex of the triangle
     * @param v2 Third vertex of the triangle
     * @return  boolean
     */
    private boolean checkTriangleHeight(Point2D v0, Point2D v1, Point2D v2){
        double m = (v0.getY()-v1.getY())/(v0.getX()-v1.getX());
        double q = v0.getY() - m*v0.getX();

        double x = v2.getX();
        double y = v2.getY();

        double distance = Math.abs((y - m*x - q)/Math.sqrt(1+Math.pow(m,2)));

        return distance > triangleHeight; //ensures that the minimun height is triangleHeight
    }
}
