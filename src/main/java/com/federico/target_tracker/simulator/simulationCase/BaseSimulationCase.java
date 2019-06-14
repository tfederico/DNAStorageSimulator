package com.federico.target_tracker.simulator.simulationCase;

import com.federico.target_tracker.logger.Logger;
import com.federico.target_tracker.movementAlgorithm.BasicMovementAlgorithm;
import com.federico.target_tracker.movementAlgorithm.MovementAlgorithm;

/**
 * Created by federico on 25/05/16.
 */

/**
 *Class that defines the basic behaviour of the system and uses a BasicMovementAlgorithm for its receivers
 */
public class BaseSimulationCase extends AbsSimulationCase {

    /**
     * Constructor of the BaseSimulationCase class
     * @param logger Logger used to store the simulation data
     */
    public BaseSimulationCase(Logger logger){
        super(logger);

    }

    /**
     * Method which returns the algorithm used by the receivers to move inside the simulation area. In this case, it returns a BasicMovementAlgorithm
     * @return  MovementAlgorithm
     */
    @Override
    protected MovementAlgorithm getMovingAlgorithm(){
        return new BasicMovementAlgorithm(v,D,dt,L,gamma);
    }


}
