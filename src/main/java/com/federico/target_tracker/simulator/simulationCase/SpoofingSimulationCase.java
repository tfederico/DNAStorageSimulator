package com.federico.target_tracker.simulator.simulationCase;

import com.federico.target_tracker.bionanothings.*;
import com.federico.target_tracker.dna.IPlasmid;
import com.federico.target_tracker.logger.Logger;
import javafx.geometry.Point2D;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SpoofingSimulationCase extends LibrarySimulationCase {

    private int nBadBeacons;
    private ArrayList<Point2D> badBeaconPos;

    /**
     * Constructor of the DestinationSimulationCase class
     *
     * @param logger Logger used to store the simulation data
     */
    public SpoofingSimulationCase(Logger logger) throws IOException {
        super(logger);
        nBadBeacons = 1;
        badBeaconPos = new ArrayList<>(nBadBeacons);
        badBeaconPos.add(new Point2D(0.3, -0.3));

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
                    i>=startIndex && i<clusterIndex, false);
            b.emitAttractants(true);
            b.emitRepellents(false);
            beaconsList.add(b);
        }

        for (int i = 0; i < nBadBeacons; i++){
            Beacon b;
            b = new MaliciousBeacon(beacons, badBeaconPos.get(i).getX(), badBeaconPos.get(i).getY(),
                    false, false, true);
            b.emitAttractants(true);
            b.emitRepellents(false);
            beaconsList.add(b);
        }


    }

    @Override
    protected void placeReceivers(){

        // start position coordinates
        double sx = -0.1;
        double sy = 0.0;

        double dx = 0.7;
        double dy = 0.0;

        for(int i = 0; i < nClusters; i++)
            for (int j = 0; j < nReceivPerCluster; j++){
                int id = j + i*nReceivPerCluster + (beacons + nBadBeacons) + nClusters;
                int clusterID = i + (beacons + nBadBeacons);
                PlasmidRetriever p = new SpoofedPlasmidRetriever(id, sx, sy, dx, dy,
                        getMovingAlgorithmWithDestination(clusterBacterias.get(clusterID).getPosition()),clusterID);
                p.emitAttractants(false);
                p.emitRepellents(false);
                receiversList.add(p);
            }

    }

    @Override
    protected void placeClusters(){

        for(int i = 0;i < nClusters; i++){
            int id = (beacons + nBadBeacons) + i;
            ClusterBacteria c = new ClusterBacteria(id ,
                    clusterPos.get(i).getX(), clusterPos.get(i).getY());
            c.emitRepellents(false);
            c.emitRepellents(false);
            clusterBacterias.put(id, c);
        }
    }

    @Override
    protected String calculateRetrievedPlasmids(HashMap<Integer, IPlasmid> retrieved){

        int[] clusterCounter = new int[] {0,0,0,0};

        for(IPlasmid plasmid : retrieved.values()){
            for(ClusterBacteria cluster : clusterBacterias.values()){
                if (cluster.getPlasmids().contains(plasmid))
                    clusterCounter[cluster.getID()- (beacons + nBadBeacons)]++;
            }
        }

        float[] clusterPercentages = new float[4];

        String percentages = "";

        for(ClusterBacteria cluster : clusterBacterias.values()){
            int index = cluster.getID()- (beacons + nBadBeacons);
            clusterPercentages[index] =  clusterCounter[index]*1.0f/cluster.getPlasmids().size();
        }

        for (float percentage : clusterPercentages){
            percentages = percentages + percentage + ",";
        }

        percentages = percentages.substring(0, percentages.length()-1);

        return percentages;

    }
}
