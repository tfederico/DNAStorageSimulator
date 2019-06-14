package com.federico.target_tracker.logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by federico on 25/05/16.
 */

/**
 *Class that stores the simulation data on files
 */
public class FileLogger implements Logger{

    /**
     * Directory in which the file containing the simulations data will be stored
     */
    private String simFolder;

    /**
     * Resource used to write on a file the receivers placement during the simulation
     */
    private PrintWriter receiversPlacement;

    /**
     * Resource used to write on a file the beacons placement for every simulation
     */
    private PrintWriter beaconsPlacement;

    private PrintWriter clustersPlacement;

    private PrintWriter results;

    private PrintWriter plasmids;

    private PrintWriter retrievedPlasmids;

    /**
     * Resource used to write on a file the destination placement for every simulation
     */
    private PrintWriter destinations;

    /**
     * File extention used to store data
     */
    private String extention;

    /**
     * Constructor of the FileLogger class
     * @param type Extension of the file on which data will be stored
     * @param dir Path of the directory containing the file on which data will be stored
     */
    public FileLogger(FileType type, String dir, int num){

        extention = FileType.toString(type);

        //controlling existance of data directory
        File dataDir = new File("data/");
        if(!dataDir.exists())
            dataDir.mkdir();



        File buildDir = new File("data/"+dir);
        if(!buildDir.exists()){
            buildDir.mkdir();
        }

        this.simFolder = "data/"+dir;

        /*try {
            destinations = new PrintWriter(simFolder+"/destinations_"+num+"."+extention, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/

        try{
            results = new PrintWriter(simFolder+"/results_"+num+"."+extention, "UTF-8");
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        try{
            plasmids = new PrintWriter(simFolder+"/unorderedPlasmids_"+num+"."+extention, "UTF-8");
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        try{
            retrievedPlasmids = new PrintWriter(simFolder+"/retrievedPlasmids_"+num+"."+extention, "UTF-8");
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

    }

    /**
     * Method which stores the information about receivers during the simulation
     * @param data String which represents the position of the receiver
     */
    @Override
    public void logReceiver(String data){

        receiversPlacement.println(data);
        receiversPlacement.flush();

    }

    /**
     * Method which stores the information about destinations during simulations
     * @param data String which represents the position of the destination
     */
    @Override
    public void logDestination(String data) {
        destinations.println(data);
        destinations.flush();
    }

    /**
     * Method that starts the storage of the simulation data
     * @param runNumber Number of simulations executed by the simulator
     */
    @Override
    public void startLogging(int runNumber) {

        String num = String.valueOf(runNumber);
        //usefull in order to keep files ordered by name
        /*if (runNumber < 10)
            num = "0"+num;*/

        /*try {
            beaconsPlacement = new PrintWriter(simFolder+"/beacons_placement_"+num+"."+extention, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        try {
            receiversPlacement = new PrintWriter(simFolder+"/receivers_placement_"+num+"."+extention, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /*try {
             clustersPlacement = new PrintWriter(simFolder+"/clusters_placement_"+num+"."+extention, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/




    }

    /**
     * Method which stores the information about beacons during the simulation
     * @param data String which represents the position of the beacon
     */
    @Override
    public void logBeacon(String data){

        beaconsPlacement.println(data);
        beaconsPlacement.flush();
    }

    /**
     * Method that stop the storage of the simulation data
     */
    @Override
    public void stopLogging(){
        //beaconsPlacement.close();
        receiversPlacement.close();
        //destinations.close();
        //clustersPlacement.close();
    }

    @Override
    public void logClusters(String data) {
        clustersPlacement.println(data);
        clustersPlacement.flush();
    }

    @Override
    public void logResults(String data) {
        results.println(data);
        results.flush();
    }

    @Override
    public void logPlasmids(String data){
        plasmids.println(data);
        plasmids.flush();
    }

    @Override
    public void logRetrievedPlasmids(String data){
        retrievedPlasmids.println(data);
        retrievedPlasmids.flush();
    }

    public String getSimFolder(){
        return simFolder;
    }
}
