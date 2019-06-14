package com.federico.target_tracker.simulator.simulationCase;

import com.federico.target_tracker.bionanothings.Receiver;
import com.federico.target_tracker.logger.Logger;
import javafx.geometry.Point2D;

/**
 * Created by federico on 20/07/16.
 */

/**
 * Used to ensure that the receivers start from the same point P,
 * which is different for each execution
 */
public class DiffBeaconSimulationCase extends DestinationSimulationCase {
    /**
     * Constructor of the DestinationSimulationCase class
     *  @param logger  Logger used to store the simulation data
     * @param inOrOut Argument that defines if the destination must be placed inside or outside the triangle plotted by beacons
     */
    public DiffBeaconSimulationCase(Logger logger, boolean inOrOut) {
        super(logger, inOrOut);
    }

    @Override
    protected void placeReceivers(){

        Point2D p = new Point2D(Math.random()-L/2, Math.random()-L/2);
        for (int i=0; i < receivers; i++) {
            //random positioning the receiver
            receiversList.add(new Receiver(i, p.getX(), p.getY(), getMovingAlgorithm()));
        }
    }
}
