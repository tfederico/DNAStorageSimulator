package com.federico.target_tracker.simulator.simulationCase;

import com.federico.target_tracker.utils.ImpossibleException;
import com.federico.target_tracker.utils.TooMuchInfoException;

import java.io.IOException;

/**
 * Created by federico on 25/05/16.
 */
public interface SimulationCase {

    /**
     * Method that defines the behaviour of a simulation case, which matches with the steps to take in order to complete a simulation
     * @param runNumber Integer which correspond to the number of simulation case executed by the simulator
     */
    public void execute(int runNumber) throws ImpossibleException, TooMuchInfoException;

}
