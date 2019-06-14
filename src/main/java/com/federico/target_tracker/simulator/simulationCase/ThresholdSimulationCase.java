package com.federico.target_tracker.simulator.simulationCase;

import com.federico.target_tracker.logger.Logger;
import com.federico.target_tracker.movementAlgorithm.MovementAlgorithm;
import com.federico.target_tracker.movementAlgorithm.ThresholdMovementAlgorithm;

/**
 * Created by federico on 26/05/16.
 */

/**
 *Class that redefines the basic behaviour of the system and uses a ThresholdMovementAlgorithm for its receivers
 */
public class ThresholdSimulationCase extends BaseSimulationCase {

    /**
     * Constructor of the ThresholdSimulationCase class
     * @param logger Logger used to store the simulation data
     */
    public ThresholdSimulationCase(Logger logger) {
        super(logger);
    }

    /**
     * Method which returns the algorithm used by the receivers to move inside the simulation area. In this case, it returns a ThresholdMovementAlgorithm
     * @return  MovementAlgorithm
     */
    @Override
    protected MovementAlgorithm getMovingAlgorithm() {
        return new ThresholdMovementAlgorithm(v,D,dt,L,gamma);
    }
}
