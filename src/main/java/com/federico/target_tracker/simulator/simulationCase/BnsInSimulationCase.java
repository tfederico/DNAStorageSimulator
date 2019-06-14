package com.federico.target_tracker.simulator.simulationCase;

import com.federico.target_tracker.bionanothings.Receiver;
import com.federico.target_tracker.logger.Logger;
import com.federico.target_tracker.utils.PositionChecker;
import com.federico.target_tracker.utils.WrongHeightException;
import javafx.geometry.Point2D;

/**
 * Created by federico on 21/07/16.
 */

/**
 * BNSs are placed inside the triangle in order to understand if there is a particular behaviour
 */
public class BnsInSimulationCase extends MinHeightSimulationCase {
    /**
     * Constructor of the FixedHeightSimulationCase class
     *  @param logger  Logger used to store the information about the simulation
     * @param inOrOut Argument that tells if the destination must be placed inside or outside the triangle designed by beacons
     * @param height
     */
    public BnsInSimulationCase(Logger logger, boolean inOrOut, double height) throws WrongHeightException {
        super(logger, inOrOut, height);
    }

    @Override
    protected void placeReceivers(){


        Point2D v0 = beaconsList.get(0).getPosition();
        Point2D v1 = beaconsList.get(1).getPosition();
        Point2D v2 = beaconsList.get(2).getPosition();
        Point2D p = new Point2D(Math.random()-L/2, Math.random()-L/2);
        while (!PositionChecker.pointInTriangle(p,v0,v1,v2)){
            p = new Point2D(Math.random()-L/2, Math.random()-L/2);
        }
        for (int i=0; i < receivers; i++) {
            //random positioning the receiver
            receiversList.add(new Receiver(i, p.getX(), p.getY(), getMovingAlgorithm()));
        }
    }
}
