package com.federico.target_tracker.movementAlgorithm;

import com.federico.target_tracker.bionanothings.Beacon;
import javafx.geometry.Point2D;

import java.util.ArrayList;

/**
 * Created by federico on 16/06/16.
 */

/**
 *Class that redefines the ToPointMovementAlgorithm. This algorithm is used in a special case described by the NoTrapSimulationCase and it works as follow: in orded to avoid being trapped by attractants emitted by beacons, the receivers follows only attractants emitted by the beacon which is the nearest to the destination and, once it has reached that beaconm it uses the ToPointMovementAlgorithm
 */
public class NoTrapMovementAlgorithm extends ToPointMovementAlgorithm{

    /**
     * Boolean variable that tells if the receiver is close to destination or not using a threshold
     */
    private boolean closeToBeacon;

    /**
     * the closest beacon to the destination
     */
    private Beacon closerToDestination;

    /**
     * Constructor of the NoTrapMovementAlgorithm class
     * @param speed Speed of the receivers
     * @param D Rotational coefficient of the receivers (expressed in \begin{math}rad^2 /s\end{math})
     * @param deltaT Delta time (in seconds) that divides one step of the simulation from the previous one
     * @param L Side of the square in which the simulation takes place
     * @param gamma Coefficient that defines the degree of disturbance
     * @param destination Point to reach during the simulation
     */
    public NoTrapMovementAlgorithm(double speed, double D, double deltaT, double L, double gamma, Point2D destination) {
        super(speed, D, deltaT, L, gamma, destination);
        closerToDestination = null;
        closeToBeacon = false;

    }

    /**
     * Method that calculates the concentration of repellents in the given point. This version uses two different algorithms: the first one makes the receiver go to the beacon which is the nearest to the destination, whereas the second one used is the ToPointMovementAlgorithm
     * @return  double
     */
    @Override
    protected double checkRepellentConcentration(ArrayList<Beacon> beacons, double x, double y) {
        Point2D testPoint = new Point2D(x, y);
        double perceivedRepellent = 0;
        if(closerToDestination == null)
            closerToDestination = beacons.get(getBeaconIndexCloserToDestination(beacons));
        if(!closeToBeacon && closerToDestination.isEmittingRepellents()){

            double testRepellent = Math.exp(-Math.pow(testPoint.distance(closerToDestination.getPosition()), 2));

            if(closerToDestination.isEmittingRepellents())
                perceivedRepellent = testRepellent;

            if(testRepellent >= 0.999) {
                closeToBeacon = true;
            }


        }
        else{
            perceivedRepellent = super.checkRepellentConcentration(beacons,x,y);
        }

        return perceivedRepellent;
    }


    /**
     * Method that calculates the concentration of attractants in the given point. This version uses two different algorithms: the first one makes the receiver go to the beacon which is nearer than the others to the destination, whereas the second one used is the ToPointMovementAlgorithm
     * @return  double
     */
    @Override
    protected double checkAttractantConcentration(ArrayList<Beacon> beacons, double x, double y) {
        Point2D testPoint = new Point2D(x, y);
        double perceivedAttractant = 0;
        if(closerToDestination == null)
            closerToDestination = beacons.get(getBeaconIndexCloserToDestination(beacons));

        if(!closeToBeacon && closerToDestination.isEmittingAttractants()){

            double testAttractant = Math.exp(-Math.pow(testPoint.distance(closerToDestination.getPosition()), 2));


            if(closerToDestination.isEmittingAttractants())
                perceivedAttractant = testAttractant;

            if(testAttractant >= 0.999){
                closeToBeacon = true;
            }



        }
        else{
            perceivedAttractant = super.checkAttractantConcentration(beacons,x,y);
        }

        return perceivedAttractant;
    }

    /**
     * Method used to get the index of the beacon which the nearest to the destination
     * @param beacons Ensemble of the beacons used during the simulation
     * @return  int
     */
    private int getBeaconIndexCloserToDestination(ArrayList<Beacon> beacons){
        Point2D destination = getDestination();
        int closer = -1;
        double distance = Double.MAX_VALUE;
        for(int i = 0; i < beacons.size(); i++){
            if(destination.distance(beacons.get(i).getPosition()) < distance) {
                closer = i;
                distance = destination.distance(beacons.get(i).getPosition());
            }
        }
        return closer;
    }


}
