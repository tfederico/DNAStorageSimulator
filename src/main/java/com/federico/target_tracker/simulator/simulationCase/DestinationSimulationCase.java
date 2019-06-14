package com.federico.target_tracker.simulator.simulationCase;

import com.federico.target_tracker.bionanothings.Beacon;
import com.federico.target_tracker.bionanothings.Receiver;
import com.federico.target_tracker.logger.Logger;
import com.federico.target_tracker.movementAlgorithm.MovementAlgorithm;
import com.federico.target_tracker.movementAlgorithm.ToPointMovementAlgorithm;
import com.federico.target_tracker.utils.ImpossibleException;
import com.federico.target_tracker.utils.PositionChecker;
import com.federico.target_tracker.utils.TooMuchInfoException;
import javafx.geometry.Point2D;

/**
 * Created by federico on 01/06/16.
 */

/**
 *Class that redefines the default behaviour of the system adding a destination that must be reached and uses a DestinationMovementAlgorithm for its receivers
 */
public class DestinationSimulationCase extends AbsSimulationCase {

    /**
     * Random point inside the simulation area which must be reached by the receivers
     */
    protected Point2D destination;

    /**
     * Attribute that defines if the destination must be placed inside or outside the triangle plotted by the beacons
     */
    private boolean inOrOut;

    /**
     * Constructor of the DestinationSimulationCase class
     * @param logger Logger used to store the simulation data
     * @param inOrOut Argument that defines if the destination must be placed inside or outside the triangle plotted by beacons
     */
    public DestinationSimulationCase(Logger logger, boolean inOrOut) {
        super(logger);
        this.inOrOut = inOrOut;
        destination = new Point2D(Math.random()-L/2,Math.random()-L/2);

    }

    /**
     * Method which returns the algorithm used by the receivers to move inside the simulation area. In this case, it returns a DestinationMovementAlgorithm
     * @return  MovementAlgorithm
     */
    @Override
    protected MovementAlgorithm getMovingAlgorithm() {
        return new ToPointMovementAlgorithm(v,D,dt,L,gamma,destination);
    }

    /**
     * Method that defines the behaviour of a simulation case, which matches with the steps to take in order to complete a simulation. The default behaviour is redefined because a destination must be placed after placing the beacons
     * @param runNumber Integer which correspond to the number of simulation case executed by the simulator
     */
    @Override
    public void execute(int runNumber) throws ImpossibleException, TooMuchInfoException {

        logger.startLogging(runNumber);


        double t = 0; //Start time

        //placing beacon in the experimental area

        placeBeacons();

        destinationInGoodPlace();

        logger.logDestination(destination.getX()+","+destination.getY());

        //Add all receivers to a list


        placeReceivers();


        double oldT = -Double.MAX_VALUE;

        while(t < time) {
            if((t - oldT) > Tlog) {
                //Print receivers' placement every 25 seconds to restrict the quantity of data

                for (Receiver receiver : receiversList) {

                    logger.logReceiver(receiver.getID() + "," + receiver.getPosition().getX()
                            + "," + receiver.getPosition().getY());
                }

                oldT = t;
            }

            //Move all sensors by one step
            for (Receiver receiver : receiversList)
                receiver.moveOneStep();


            //Compute the beacons emission of attractants
            for (Receiver receiver : receiversList)
                receiver.computeNextAngle(beaconsList, psiA, psiR);


            t = t + dt;
        }

        //Print beacons placement
        for (Beacon beacon : beaconsList) {
            logger.logBeacon(beacon.getID() + "," + beacon.getPosition().getX()
                    + "," + beacon.getPosition().getY());
        }

        //Close all output files

        logger.stopLogging();
        beaconsList.clear();
        receiversList.clear();

    }

    protected void destinationInGoodPlace(){
        destinationInsideOrOutside();
    }

    /**
     * Methot that places the destination inside or outside the triangle designed by the beacons depending on the attribute inOrOut
     */
    private void destinationInsideOrOutside(){

        boolean isOutside = false;
        destination = new Point2D(Math.random()-L/2,Math.random()-L/2);
        if(!inOrOut){
            while(!isOutside){ // ensure that the point to reach is outside the triangle
                if(PositionChecker.pointInPolygon(beaconsList,destination)){
                    destination = new Point2D(Math.random()-L/2,Math.random()-L/2);
                }
                else
                    isOutside = true;

            }
        }
        else{
            isOutside = true;
            while(isOutside){ // ensure that the point to reach is inside the triangle
                if(!PositionChecker.pointInPolygon(beaconsList,destination)){
                    destination = new Point2D(Math.random()-L/2,Math.random()-L/2);
                }
                else
                    isOutside = false;

            }
        }

    }

    /**
     * Method which says if the destination must be placed inside or outside the triangle designed by the beacons returning the inOrOut attribute
     * @return  boolean
     */
    protected boolean isInOrOut(){
        return inOrOut;
    }


}
