package com.federico.target_tracker.simulator.simulationCase;

import com.federico.target_tracker.bionanothings.PlasmidRetriever;
import com.federico.target_tracker.bionanothings.SmartDoSPlasmidRetriever;
import com.federico.target_tracker.logger.Logger;

import java.io.IOException;

public class DisruptiveSmartDoSSimulationCase extends SmartDoSSimulationCase {
    /**
     * Constructor of the DestinationSimulationCase class
     *
     * @param logger Logger used to store the simulation data
     */
    public DisruptiveSmartDoSSimulationCase(Logger logger) throws IOException {
        super(logger);
    }

    protected void placeBadReceivers(){

        double msx = 0.3;
        double msy = 0.3;

        double mdx = 0.3;
        double mdy = -0.4;

        int rlSize = receiversList.size();

        for (int i = 0; i < clusterBacterias.size(); i++){
            int clusterID = i + beacons;
            for (int j = 0; j < nReceivPerCluster + 50; j++) {
                int id = j + i * nReceivPerCluster + beacons + nClusters + rlSize;
                PlasmidRetriever p = new SmartDoSPlasmidRetriever(id, msx, msy, mdx, mdy,
                        getMovingAlgorithmWithDestination(clusterBacterias.get(clusterID).getPosition()), clusterID);
                p.emitAttractants(false);
                p.emitRepellents(false);
                receiversList.add(p);
            }
        }
    }
}
