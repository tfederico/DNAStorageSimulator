package com.federico.target_tracker.parser;

/**
 * Created by federico on 25/05/16.
 */

/**
 *Interface that defines the operations that a parser (used mainly on file) can offer
 */
public interface Parser {

    /**
     * Method that parses the data of the simulation and rewrites them in order to improve their understandability
     * @param fileNum Number of simulations executed by the simulator
     * @param dir Path of the directory containing the file in which data has been stored
     */
    public void parse(int fileNum, String dir);

    /**
     * Method that parses the data of parse method and calculate the distance between the destination and the receivers
     * @param runNum Number of simulations executed by the simulator
     * @param dir Path of the directory containing the file on which data will be stored
     */
    public void printDistance(int runNum, String dir);
}
