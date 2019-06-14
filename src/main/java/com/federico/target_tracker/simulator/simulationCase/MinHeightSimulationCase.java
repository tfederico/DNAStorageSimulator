package com.federico.target_tracker.simulator.simulationCase;

import com.federico.target_tracker.bionanothings.Receiver;
import com.federico.target_tracker.logger.Logger;
import com.federico.target_tracker.utils.WrongHeightException;
import javafx.geometry.Point2D;

/**
 * Created by federico on 21/07/16.
 */

/**
 * Used to ensure that the triangle has a minimum height and the receivers start from the same point P,
 * which is different for each execution
 */
public class MinHeightSimulationCase extends FixedHeightSimulationCase {
    /**
     * Constructor of the FixedHeightSimulationCase class
     *  @param logger  Logger used to store the information about the simulation
     * @param inOrOut Argument that tells if the destination must be placed inside or outside the triangle designed by beacons
     * @param height
     */
    public MinHeightSimulationCase(Logger logger, boolean inOrOut, double height) throws WrongHeightException {
        super(logger, inOrOut, height);
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
