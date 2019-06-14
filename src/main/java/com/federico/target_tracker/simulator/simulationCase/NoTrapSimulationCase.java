package com.federico.target_tracker.simulator.simulationCase;

import com.federico.target_tracker.bionanothings.Receiver;
import com.federico.target_tracker.logger.Logger;
import com.federico.target_tracker.movementAlgorithm.MovementAlgorithm;
import com.federico.target_tracker.movementAlgorithm.NoTrapMovementAlgorithm;
import com.federico.target_tracker.utils.PositionChecker;
import javafx.geometry.Point2D;

/**
 * Created by federico on 16/06/16.
 */

/**
 *Class that redefines the DestinationSimulationCase trying to discover if it is possible to avoid a certain situation that traps the receivers, using a NoTrapMovementAlgorithm for its receivers
 */
public class NoTrapSimulationCase extends DestinationSimulationCase{

    /**
     * Constructor of the NoTrapSimulationCase class. It does not need the boolean argument inOrOut since in this case the simulation target is always out
     * @param logger Logger used to store or display information about the current simulation
     */
    public NoTrapSimulationCase(Logger logger) {
        super(logger, false);
    }

    @Override
    protected MovementAlgorithm getMovingAlgorithm() {
            return new NoTrapMovementAlgorithm(v,D,dt,L,gamma,destination);
    }

    /**
     * Method that defines the algorithm used to place the receivers in the simulation area. In this case, the algorithm is defined by the method decideReceiverPlacement
     */
    @Override
    protected void placeReceivers(){
        for (int i=0; i < receivers; i++) {
            //positioning receivers on the opposite side of the triangle compared to destination
            Point2D point = decideReceiverPlacement();
            receiversList.add(new Receiver(i, point.getX(),point.getY(), getMovingAlgorithm()));
        }
    }

    /**
     * Method used by placeReceivers to decide the right spot for every single receiver. It uses the positions of the beacons and the position of the destination to calculate the point inside the simulation area
     * @return  Point2D
     */
    private Point2D decideReceiverPlacement(){
        int right = getMostRightBeaconIndex();
        int left = getMostLeftBeaconIndex();
        int up = getUppermostBeaconIndex();
        int low = getLowestBeaconIndex();

        //receiver position
        Point2D point = new Point2D(Math.random()-L/2, Math.random()-L/2);
        /*
         * check:
         *          - point not in triangle
         *          - point and destination on opposite sides of the triangle
         */
        while(PositionChecker.pointInTriangle(point,beaconsList.get(0).getPosition(),beaconsList.get(1).getPosition(),
                beaconsList.get(2).getPosition()) || !rightPlaceForReceiver(point,left,right,low,up)){
            point = new Point2D(Math.random()-L/2, Math.random()-L/2);
        }



        int i = 0;

        int twoSides = 0; //line needs to intercept two side of triangle
        while(i<beacons && twoSides<=2){
            //coefficients of the line that passes through the receiver and the destination
            double m = (point.getY()-destination.getY())/(point.getX()-destination.getX());
            double q = point.getY() - m * point.getX();

            Point2D pos1 = beaconsList.get(i).getPosition();
            Point2D pos2;
            if(i<beacons-1)
                pos2 = beaconsList.get(i+1).getPosition();
            else
                pos2 = beaconsList.get(0).getPosition();
            double m2 = (pos1.getY()-pos2.getY())/(pos1.getX()-pos2.getX());
            double q2 = pos1.getY() - m2 * pos1.getX();
            if(m2!=m){
                double x = (q-q2)/(m2-m); //x coordinate of the line-line interception
                if(x <= L/2 && x >= -L/2) {
                    double y = m * x + q;
                    if(y <= L/2 && y >= -L/2)
                        twoSides++;
                }

            }

            i++;
            if((i == beacons && twoSides<2) || (i == beacons-1 && twoSides<1)){ //no good results in this iteration
                point = new Point2D(Math.random()-L/2, Math.random()-L/2);
                while(PositionChecker.pointInTriangle(point,beaconsList.get(0).getPosition(),
                        beaconsList.get(1).getPosition(), beaconsList.get(2).getPosition())
                        || !rightPlaceForReceiver(point,left,right,low,up)){
                    point = new Point2D(Math.random()-L/2, Math.random()-L/2);
                }
                i = 0;
                twoSides = 0;
            }
        }

        return point;
    }

    /**
     * Method which returns the index of the most left beacon in the simulation area
     * @return  int
     */
    private int getMostLeftBeaconIndex(){
        int i = -1;
        double left = L/2;
        for(int j = 0; j < beaconsList.size();j++){
            if(beaconsList.get(j).getPosition().getX()<=left) {
                i = j;
                left = beaconsList.get(j).getPosition().getX();
            }
        }
        return i;
    }

    /**
     * Method which returns the index of the most right beacon in the simulation area
     * @return  int
     */
    private int getMostRightBeaconIndex(){
        int i = -1;
        double right = -L/2;
        for(int j = 0; j < beaconsList.size();j++){
            if(beaconsList.get(j).getPosition().getX()>=right) {
                i = j;
                right = beaconsList.get(j).getPosition().getX();
            }
        }
        return i;
    }

    /**
     * Method which returns the index of the lowest beacon in the simulation area
     * @return  int
     */
    private int getLowestBeaconIndex(){
        int i = -1;
        double low = L/2;
        for(int j = 0; j < beaconsList.size();j++){
            if(beaconsList.get(j).getPosition().getY()<=low) {
                i = j;
                low = beaconsList.get(j).getPosition().getY();
            }
        }
        return i;
    }

    /**
     * Method which returns the index of the uppermost beacon in the simulation area
     * @return  int
     */
    private int getUppermostBeaconIndex(){
        int i = -1;
        double up = -L/2;
        for(int j = 0; j < beaconsList.size();j++){
            if(beaconsList.get(j).getPosition().getY()>=up) {
                i = j;
                up = beaconsList.get(j).getPosition().getY();
            }
        }
        return i;
    }

    /**
     * Method that tells if the receiver is in a good place by returning a boolean value. It reaches its purpose using the methods upperAndLowerToAllBeacons and lefterAndRighterToAllBeacons
     * @param point Receiver position
     * @param left Index of the most left beacon
     * @param right Index of the most right beacon
     * @param low Index of the lowest beacon
     * @param up Index of the uppermost beacon
     * @return  boolean
     */
    private boolean rightPlaceForReceiver(Point2D point,int left, int right, int low, int up){
        //true if in wrong place, false otherwise
        return uppermostAndLowestToAllBeacons(point,low,up) || mostLeftAndMostRightToAllBeacons(point,left,right);
        //at least one condition, both can't be granted every time
    }

    /**
     * Method that checks if the destination is placed lefter than all beacons and the receiver is righter or viceversa
     * @param point Possible placement for the receiver
     * @param low Index of the lowest beacon
     * @param up Index of the uppermost beacon
     * @return boolean
     */
    private boolean uppermostAndLowestToAllBeacons(Point2D point, int low, int up){
        return ((point.getY()<=beaconsList.get(low).getPosition().getY() && destination.getY()>=
                beaconsList.get(up).getPosition().getY()) //lower && upper
                ||
                (destination.getY()<=beaconsList.get(low).getPosition().getY() && point.getY()>=
                        beaconsList.get(up).getPosition().getY()));//upper && lower
    }

    /**
     * Method that checks if the destination is placed lefter than all beacons and the receiver is righter or viceversa
     * @param point Possible placement for the receiver
     * @param left Index of the most left beacon
     * @param right Index of the most right beacon
     * @return boolean
     */
    private boolean mostLeftAndMostRightToAllBeacons(Point2D point, int left, int right){
        return ((point.getX()<=beaconsList.get(left).getPosition().getX() && destination.getX()>=
                beaconsList.get(right).getPosition().getX()) //lefter && righter
                ||
                (destination.getX()<=beaconsList.get(left).getPosition().getX() && point.getX()>=
                        beaconsList.get(right).getPosition().getX()));//righter && lefter
    }

    /**
     * Method used to place the destination in a proper place, which is based on the kind of simulation. In this case, given a receiver position, a good place is on the opposite side of the triangle in which the receiver is placed
     */
    @Override
    protected void destinationInGoodPlace(){

        int right = getMostRightBeaconIndex();
        int left = getMostLeftBeaconIndex();
        int up = getUppermostBeaconIndex();
        int low = getLowestBeaconIndex();
        boolean isOk = false;
        destination = new Point2D(Math.random()-L/2,Math.random()-L/2);

        while (!isOk){
            if(!isInOrOut()){
                if(PositionChecker.pointInTriangle(destination,beaconsList.get(0).getPosition(),
                        beaconsList.get(1).getPosition(), beaconsList.get(2).getPosition())
                                || !rightPlaceForDestination(destination,left,right,low,up)){
                    destination = new Point2D(Math.random()-L/2,Math.random()-L/2);
                }
                else
                    isOk = true;
            }
            else{
                if(!PositionChecker.pointInTriangle(destination,beaconsList.get(0).getPosition(),
                        beaconsList.get(1).getPosition(),beaconsList.get(2).getPosition())
                        || !rightPlaceForDestination(destination,left,right,low,up)){
                    destination = new Point2D(Math.random()-L/2,Math.random()-L/2);
                }
                else
                    isOk = true;
            }
        }
    }


    /**
     * Method that tells if the destination is in a good place by returning a boolean value. It uses the methods getLowestBeaconIndex, getMostLeftBeaconIndex, getMostRightBeaconIndex and getUppermostBeaconIndex
     * @param dest Destination that must be reached during the simulation
     * @param left Index of the most left beacon
     * @param right Index of the most right beacon
     * @param low Index of lowest beacon
     * @param up Index of the uppermost beacon
     * @return  boolean
     */
    private boolean rightPlaceForDestination(Point2D dest, int left, int right, int low, int up){
        boolean lefter = dest.getX() <= beaconsList.get(left).getPosition().getX();
        boolean righter = dest.getX() >= beaconsList.get(right).getPosition().getX();

        boolean lower = dest.getY() <= beaconsList.get(low).getPosition().getY();
        boolean upper = dest.getY() >= beaconsList.get(up).getPosition().getY();

        return lefter || righter || upper || lower;
    }
}
