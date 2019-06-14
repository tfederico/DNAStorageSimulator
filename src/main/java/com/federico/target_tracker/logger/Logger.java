package com.federico.target_tracker.logger;

/**
 * Created by federico on 25/05/16.
 */

/**
 *Interface that defines the behaviour of a logger used to store and show data of the simulations
 */
public interface Logger{

    /**
     * Method that starts the storage of the simulation data
     * @param runNumber Number of simulations executed by the simulator
     */
    public void startLogging(int runNumber);

    /**
     * Method which stores the information about beacons during the simulation
     * @param data String which represents the position of the beacon
     */
    public void logBeacon(String data);

    /**
     * Method which stores the information about receivers during the simulation
     * @param data String which represents the position of the receiver
     */
    public void logReceiver(String data);

    /**
     * Method which stores the information about destinations during simulations
     * @param data String which represents the position of the destination
     */
    public void logDestination(String data);

    /**
     * Method that stop the storage of the simulation data
     */
    public void stopLogging();

    public void logClusters(String data);

    public void logResults(String data);

    public void logPlasmids(String data);

    public void logRetrievedPlasmids(String data);


}
