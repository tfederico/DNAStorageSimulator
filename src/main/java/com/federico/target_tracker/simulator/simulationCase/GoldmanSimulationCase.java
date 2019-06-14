package com.federico.target_tracker.simulator.simulationCase;


import com.federico.target_tracker.dna.IPlasmid;
import com.federico.target_tracker.dna.GoldmanBytePlasmidConverter;
import com.federico.target_tracker.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

public class GoldmanSimulationCase extends LibrarySimulationCase {
    /**
     * Constructor of the DestinationSimulationCase class
     *
     * @param logger  Logger used to store the simulation data
     */
    public GoldmanSimulationCase(Logger logger) throws IOException {
        super(logger);
    }

    @Override
    protected ArrayList<IPlasmid> getPlasmidsFromData(BitSet bitSet){
        return new GoldmanBytePlasmidConverter().convert(bitSet);
    }
}
