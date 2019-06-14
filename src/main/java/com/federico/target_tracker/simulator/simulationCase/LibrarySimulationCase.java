package com.federico.target_tracker.simulator.simulationCase;

import com.federico.target_tracker.bionanothings.*;
import com.federico.target_tracker.dna.BytePlasmidConverter;
import com.federico.target_tracker.dna.IPlasmid;
import com.federico.target_tracker.logger.Logger;
import com.federico.target_tracker.movementAlgorithm.LibraryMovementAlgorithm;
import com.federico.target_tracker.movementAlgorithm.MovementAlgorithm;
import com.federico.target_tracker.utils.ImpossibleException;

import com.federico.target_tracker.utils.InformationManager;
import com.federico.target_tracker.utils.TooMuchInfoException;
import javafx.geometry.Point2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;



/**
 * Created by federico on 27/05/17.
 */
public class LibrarySimulationCase extends DestinationSimulationCase {


    protected HashMap<Integer, ClusterBacteria> clusterBacterias;
    private HashMap<Integer, IPlasmid> createdPlasmids;
    protected int nReceivPerCluster;
    protected int nClusters;
    protected ArrayList<Point2D> clusterPos;
    protected ArrayList<Point2D> beaconPos;
    private static String path = "file.bin";
    private InformationManager informationManager;
    private int rN;

    /**
     * Constructor of the DestinationSimulationCase class
     *
     * @param logger  Logger used to store the simulation data
     */
    public LibrarySimulationCase(Logger logger) throws IOException {
        super(logger, true);

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

            informationManager = new InformationManager(path);
        }

        nClusters = Integer.parseInt(content.get("Ncluster"));
        nReceivPerCluster = Integer.parseInt(content.get("BNSxClust"));


        clusterBacterias = new HashMap<>(nClusters);
        clusterPos = new ArrayList<>(nClusters);
        for(int i = 1; i<= nClusters; i++)
            clusterPos.add(new Point2D(Double.parseDouble(content.get("c"+i+"X")),
                    Double.parseDouble(content.get("c"+i+"Y"))));
        beaconPos = new ArrayList<>();
        for(int i = 1; i<= beacons; i++)
            beaconPos.add(new Point2D(Double.parseDouble(content.get("b"+i+"X")),
                    Double.parseDouble(content.get("b"+i+"Y"))));

        PlasmidRetriever.setThreshold(Double.parseDouble(content.get("conjThr")));
        destination = new Point2D(0.7,0.0);


    }

    @Override
    public void execute(int runNumber) throws ImpossibleException, TooMuchInfoException {

        rN = runNumber;
        logger.startLogging(runNumber);


        double t = 0; //Start time

        //placing beacon in the experimental area

        placeBeacons();

        //System.out.println("Beacon placed");

        placeClusters();

        //System.out.println("Clusters placed");

        encodeIntoClusters();

        //System.out.println("Information encoded");

        nReceivPerCluster = runNumber*nReceivPerCluster + nReceivPerCluster;
        receivers = nClusters * nReceivPerCluster;

        placeReceivers();

        //System.out.println("Plasmid retriever placed");


        double oldT = -Double.MAX_VALUE;

        HashMap<Integer, IPlasmid> retrievedPlasmids = new HashMap<>(createdPlasmids.size());
        HashMap<Integer, Integer> bacteriaConjugatingCluster = new HashMap<>();

        //System.out.println("Starting simulation.....");

        while(t < time && createdPlasmids.size() > retrievedPlasmids.size()) {
            if((t - oldT) > Tlog) {
                //Print receivers' placement every Tlog seconds to restrict the quantity of data
                //System.out.println((int) t+"s");

                for(int i = 0; i < receiversList.size(); i++){
                    Receiver receiver = receiversList.get(i);
                    logger.logReceiver(receiver.getID() + "," + receiver.getPosition().getX()
                            + "," + receiver.getPosition().getY() +","+Thread.currentThread().getId()+","+t);
                }
                /*for (Receiver receiver : receiversList) {

                    logger.logReceiver(receiver.getID() + "," + receiver.getPosition().getX()
                            + "," + receiver.getPosition().getY() +","+Thread.currentThread().getId()+","+t);
                }*/

                oldT = t;
            }




            //Move all sensors by one step
            //Compute the beacons emission of attractants
            for (Receiver receiver : receiversList) {

                if((!((PlasmidRetriever) receiver).isConjugating()) &&
                        ((PlasmidRetriever) receiver).shouldGoToCluster()){

                    receiver.moveOneStep();
                    receiver.computeNextAngle(beaconsList, psiA, psiR);
                    ClusterBacteria c = getClusterByID(((PlasmidRetriever) receiver).getClusterID());
                    if(((PlasmidRetriever) receiver).shouldGoToCluster() &&
                            ((PlasmidRetriever) receiver).canConjugateWithCluster(c)) {

                        ((PlasmidRetriever) receiver).startConjugationWithCluster(c);
                        if(((PlasmidRetriever) receiver).isConjugating()) {
                            //System.out.println("CONJUGATING, BITCH! " + receiver.getID());
                            bacteriaConjugatingCluster.put(receiver.getID(),
                                    ((PlasmidRetriever) receiver).getClusterID());
                        }

                    }

                }
                else
                    if(((PlasmidRetriever) receiver).shouldGoToDestination()){ //moving to drop plasmids

                        if(!((PlasmidRetriever) receiver).isConjugating()) {//detached from cluster
                            if(bacteriaConjugatingCluster.containsKey(receiver.getID())){
                                int clusterID = bacteriaConjugatingCluster.get(receiver.getID());
                                bacteriaConjugatingCluster.remove(receiver.getID());
                                clusterBacterias.get(clusterID).setConjugating(false);
                            }


                        }

                        ArrayList<IPlasmid> plasm = ((PlasmidRetriever) receiver).conjugationStep(dt,
                                beaconsList);

                        if(plasm != null) {
                            //System.out.println("Filling");
                            for (IPlasmid p : plasm) {
                                retrievedPlasmids.putIfAbsent(p.getID(), p);
                                //System.out.println("DROPPING, BITCH! "+((PlasmidRetriever) receiver).getID());
                            }
                        }
                    }
                    // else is still conjugating

            }


            t = t + dt;
        }

        /*//Print beacons placement, if you want to use them change also startLogging() and stopLogging() inside FileLogger
        for (Beacon beacon : beaconsList) {
            logger.logBeacon(beacon.getID() + "," + beacon.getPosition().getX()
                    + "," + beacon.getPosition().getY());
        }

        for(Map.Entry<Integer, ClusterBacteria> entry : clusterBacterias.entrySet()){
            ClusterBacteria c = entry.getValue();
            logger.logClusters(c.getID() + "," + c.getPosition().getX()
                    + "," + c.getPosition().getY());
        }*/


        Set s1 = new HashSet();
        s1.addAll(retrievedPlasmids.keySet());
        Set s2 = new HashSet();
        s2.addAll(createdPlasmids.keySet());

        s2.removeAll(s1);

        ArrayList <Integer> lostPlasmidsSortedByKey = new ArrayList<>(new TreeSet<Integer>(s2));

        int lostBases = 0;

        for(Integer key : lostPlasmidsSortedByKey)
            lostBases+=createdPlasmids.get(key).getNumberOfBases();

        int totalBases = 0;
        for(IPlasmid plasmid : createdPlasmids.values()){
            totalBases+=plasmid.getNumberOfBases();
        }

        float perc = 100*(1 - (1.0f*lostBases)/totalBases);

        System.out.println("Created plasmids: "+createdPlasmids.size());
        System.out.println("Retrieved plasmids: "+ retrievedPlasmids.size());
        System.out.println("Percentage (plasmids): "+(1.0f* retrievedPlasmids.size())/createdPlasmids.size()*100+"%");
        System.out.println("Percentage (data): "+perc+"%");
        System.out.println("Time needed: ~"+(int) t/60+"min");



        logger.logResults(runNumber+","+perc+","+(int) t/60);
        String s = calculateRetrievedPlasmids(retrievedPlasmids);
        logger.logRetrievedPlasmids(runNumber+","+s);

        //Close all output files

        logger.stopLogging();
        beaconsList.clear();
        receiversList.clear();
        clusterBacterias.clear();


    }

    protected void placeClusters(){

        for(int i = 0;i < nClusters; i++){
            int id = beacons+i;
            ClusterBacteria c = new ClusterBacteria(id ,
                    clusterPos.get(i).getX(), clusterPos.get(i).getY());
            c.emitRepellents(false);
            c.emitRepellents(false);
            clusterBacterias.put(id, c);
        }
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

            b = new LibraryBeacon(i, beaconPos.get(i).getX(),
                    beaconPos.get(i).getY(),i<startIndex,
                    i>=startIndex && i<clusterIndex);
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
                int id = j + i*nReceivPerCluster + beacons + nClusters;
                int clusterID = i + beacons;
                PlasmidRetriever p = new PlasmidRetriever(id, sx, sy, dx, dy,
                        getMovingAlgorithmWithDestination(clusterBacterias.get(clusterID).getPosition()),clusterID);
                p.emitAttractants(false);
                p.emitRepellents(false);
                receiversList.add(p);
            }

    }

    protected MovementAlgorithm getMovingAlgorithmWithDestination(Point2D destination){
        return new LibraryMovementAlgorithm(v,D,dt,L,gamma,destination);
    }

    private void encodeIntoClusters(){

        BitSet bits = informationManager.getData();

        ArrayList<IPlasmid> plasmids = getPlasmidsFromData(bits);

        System.out.println("Number of plasmids: "+plasmids.size());

        int totalCapacity;

        totalCapacity = calculateCapacity();

        System.out.println("Total capacity: "+totalCapacity);

        boolean changed = false;
        while(plasmids.size() > totalCapacity) {
            changed = true;
            placeClusters();
            totalCapacity = calculateCapacity();
        }

        if(changed)
            System.out.println("New total capacity: "+totalCapacity);

        int lastInsertedPlasmid = 0;
        int plasmidPerCluster = plasmids.size()/nClusters;
        int temp=plasmidPerCluster;


        temp+=plasmids.size()%4; //adding missing plasmids to the first cluster

        for(Map.Entry<Integer, ClusterBacteria> entry : clusterBacterias.entrySet()) {
            ClusterBacteria c = entry.getValue();
            while(temp > 0 && lastInsertedPlasmid < plasmids.size()) {
                c.getPlasmids().add(plasmids.get(lastInsertedPlasmid));

                String s = plasmids.get(lastInsertedPlasmid).getID()+","+
                        plasmids.get(lastInsertedPlasmid).toString()+","+
                        (c.getID()-beacons+1);

                logger.logPlasmids(rN+","+s);
                lastInsertedPlasmid++;
                temp--;
            }
            temp = plasmidPerCluster;
        }



        createdPlasmids = new HashMap<>();

        for(IPlasmid plasmid : plasmids){
            createdPlasmids.put(plasmid.getID(), plasmid);
        }


    }

    private ClusterBacteria getClusterByID(int id){
        return clusterBacterias.get(id);
    }

    private int calculateCapacity(){
        int totalCapacity = 0;
        for(Map.Entry<Integer, ClusterBacteria> entry : clusterBacterias.entrySet())
            totalCapacity += entry.getValue().getCapacity();
        return totalCapacity;
    }

    protected ArrayList<IPlasmid> getPlasmidsFromData(BitSet bitSet){
        return new BytePlasmidConverter().convert(bitSet);
    }

    protected String calculateRetrievedPlasmids(HashMap<Integer, IPlasmid> retrieved){

        int[] clusterCounter = new int[] {0,0,0,0};

        for(IPlasmid plasmid : retrieved.values()){
            for(ClusterBacteria cluster : clusterBacterias.values()){
                if (cluster.getPlasmids().contains(plasmid))
                    clusterCounter[cluster.getID()-beacons]++;
            }
        }

        float[] clusterPercentages = new float[4];

        String percentages = "";

        for(ClusterBacteria cluster : clusterBacterias.values()){
            int index = cluster.getID()-beacons;
            clusterPercentages[index] =  clusterCounter[index]*1.0f/cluster.getPlasmids().size();
        }

        for (float percentage : clusterPercentages){
            percentages = percentages + percentage + ",";
        }

        percentages = percentages.substring(0, percentages.length()-1);

        return percentages;

    }
}
