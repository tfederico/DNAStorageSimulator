package com.federico.target_tracker.logger;

/**
 * Created by federico on 25/05/16.
 */

/**
 *Class used to display the simulation data using the prompt. This kind of logger is mainly used for debug purpose since the big amount of data makes reading it from the prompt quite uncomfortable
 */
public class PromptLogger implements Logger {

    /**
     * Boolean variable that tells if during the simulation the data should be stored or not
     */
    volatile private boolean shouldLog = false;

    /**
     * Number of simulation case executed by simulator
     */
    private int runNumber = -1;

    /**
     * Method that starts the storage of the simulation data
     * @param runNumber Number of simulations executed by the simulator
     */
    @Override
    public void startLogging(int runNumber) {
        this.runNumber = runNumber;
        shouldLog = true;
    }

    /**
     * Method which shows the information about beacons during the simulation on the prompt
     * @param data String which represents the position of the beacon
     */
    @Override
    public void logBeacon(String data) {
        if (shouldLog)
            System.out.println("EXEC #"+runNumber+" BEACON: "+data);
    }

    /**
     * Method which shows the information about receivers during the simulation on the prompt
     * @param data String which represents the position of the receiver
     */
    @Override
    public void logReceiver(String data) {
        if(shouldLog)
            System.out.println("EXEC #"+runNumber+" RECEIVER: "+data);
    }

    /**
     * Method which shows the information about destinations during simulations on the prompt
     * @param data String which represents the position of the destination
     */
    @Override
    public void logDestination(String data) {
        if(shouldLog)
            System.out.println("EXEC #"+runNumber+" DESTINATION: "+data);
    }

    /**
     * Method that stop displaying of the simulation data
     */
    @Override
    public void stopLogging() {
        runNumber = -1;
        shouldLog = false;
    }

    @Override
    public void logClusters(String data) {
        if(shouldLog)
            System.out.println("EXEC #"+runNumber+" CLUSTERS"+data);
        //empty
    }

    @Override
    public void logResults(String data) {

    }

    @Override
    public void logPlasmids(String data) {

    }

    @Override
    public void logRetrievedPlasmids(String data) {

    }
}
