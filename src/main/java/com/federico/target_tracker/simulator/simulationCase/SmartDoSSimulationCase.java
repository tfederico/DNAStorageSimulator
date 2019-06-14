package com.federico.target_tracker.simulator.simulationCase;

import com.federico.target_tracker.bionanothings.*;
import com.federico.target_tracker.logger.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SmartDoSSimulationCase extends LibrarySimulationCase {

    private int nBadBacteria; // -1 means use the number of good bacteria
    /**
     * Constructor of the DestinationSimulationCase class
     *
     * @param logger Logger used to store the simulation data
     */
    public SmartDoSSimulationCase(Logger logger) throws IOException {
        super(logger);

        Map<String, String> content = new HashMap<>();
        String csvFile = "config.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(cvsSplitBy);
                content.put(data[0],data[1]);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        nBadBacteria = Integer.parseInt(content.get("NBadBacteria"));
    }

    /**
     * Method that defines the algorithm used to place the beacons in the simulation area. The default algorithm is placing the beacons in random positions inside the simulation square
     */
    @Override
    protected void placeBeacons() {

        int startIndex = 3;
        int clusterIndex = 6;
        for(int i = 0; i < beacons; i++){
            Beacon b;

            b = new MaliciousBeacon(i, beaconPos.get(i).getX(),
                    beaconPos.get(i).getY(),i<startIndex,
                    i>=startIndex && i<clusterIndex, i >= clusterIndex);
            b.emitAttractants(true);
            b.emitRepellents(false);
            beaconsList.add(b);
        }


    }

    @Override
    protected void placeReceivers() {
        super.placeReceivers();
        placeBadReceivers();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void placeBadReceivers(){

        int rlSize = receiversList.size();
        if (nBadBacteria == -1){
            nBadBacteria = rlSize;
        }

        double msx = 0.3;
        double msy = 0.2;

        double mdx = 0.3;
        double mdy = -0.3;

        for (int i = 0; i < nClusters; i++){
            for (int j = 0; j < nBadBacteria; j++) {
                int id = j + i * nBadBacteria + beacons + nClusters + rlSize;
                int clusterID = i + beacons;
                PlasmidRetriever p = new SmartDoSPlasmidRetriever(id, msx, msy, mdx, mdy,
                        getMovingAlgorithmWithDestination(clusterBacterias.get(clusterID).getPosition()), clusterID);
                p.emitAttractants(false);
                p.emitRepellents(false);
                receiversList.add(p);
            }
        }
    }

}
