package com.federico.target_tracker.simulator;

import com.federico.target_tracker.logger.FileLogger;
import com.federico.target_tracker.logger.FileType;
import com.federico.target_tracker.logger.Logger;
import com.federico.target_tracker.parser.Parser;
import com.federico.target_tracker.simulator.simulationCase.*;
import com.federico.target_tracker.utils.ImpossibleException;
import com.federico.target_tracker.utils.TooMuchInfoException;
import com.federico.target_tracker.utils.WrongHeightException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by federico on 25/05/16.
 */

/**
 *Class that defines the kind of simulation to execute, the number of exections for the simulation case and how to save
 * or view the results using a logger
 */
public class Simulator {

    /**
     * Method which starts the main thread of the simulator
     * @param args Array of String objects containing the command line
     * @throws WrongHeightException if the height of the triangle is wrong
     * @throws NoSuchAlgorithmException if the algorithm in the "config.csv file" is wrong
     */
    public static void main(String[] args) throws WrongHeightException, NoSuchAlgorithmException, ImpossibleException, IOException, TooMuchInfoException {

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

        //System.out.println(content);

        int numberOfTests = Integer.parseInt(content.get("Nrun"));
        System.out.println("Number of tests: "+numberOfTests);

        //creating simulation directory
        Instant instant = Instant.now(); // Current date-time in UTC.
        String simFolder = instant.toString().replace ("T","_").replace("Z","");

        System.out.println("Simulation folder: "+simFolder);
        FileType simpleDataType = FileType.CSV;

        ArrayList<Logger> loggers = new ArrayList<>();
        for(int i = 0; i < numberOfTests; i++){
            loggers.add(new FileLogger(simpleDataType, simFolder,i));
        }

        //Parser parser = new FileParser(simpleDataType,FileType.CSV,numberOfTests);
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(cores);

        for(int i = 0; i < numberOfTests; i++) {
            SimulationCase simulationCase;
            try {
                simulationCase = chooseSimulationCase(content,loggers.get(i));
            } catch (NoSuchAlgorithmException e) {
                System.out.println("WRONG ALGORITHM IN 'config.csv' FILE!");
                throw e;
            } catch (WrongHeightException e){
                System.out.println("WRONG HEIGHT FOR THE TRIANGLE!");
                throw e;
            }
            //simulationCase.execute(i);
            final int j = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Simulation #"+j);
                    try {
                        simulationCase.execute(j);
                    } catch (ImpossibleException e) {
                        e.printStackTrace();
                    } catch (TooMuchInfoException e) {
                        e.printStackTrace();
                    }
                }
            });
            //parser.parse(i, "data/"+simFolder);
            /*if(Integer.parseInt(content.get("NBS"))>1)
                parser.printDistance(i,"data/"+simFolder);*/

        }
        executor.shutdown();
        while (!executor.isTerminated());
        System.out.println();

        //launchScript(parser,simFolder,numberOfTests,Integer.parseInt(content.get("NBS")));

    }

    /**
     * Method used to launch the python script in order to analyze the simulation data
     * @param parser Parser used to read the data of the simulation and rewrite them in order to improve their
     *               understandability
     * @param simFolder Path of the directory containing the file in which data will be stored
     * @param numberOfTests Number of times that the simulation case has been executed
     */
    private static void launchScript(Parser parser,String simFolder,int numberOfTests,int bns){


        try {
            Process p;
            if(bns<=1)
                p = Runtime.getRuntime().exec("python analyzer.py "+simFolder);
            else
                p = Runtime.getRuntime().exec("python analyzer_multi.py "+simFolder);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s;
            while ((s = buffer.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        parser.printDistance(numberOfTests,"data/"+simFolder);
    }

    /**
     * Method that reads the simulation case that must be executed from the file config.csv and returns the
     * corrisponding MovementAlgorithm
     * @param content Content of the config.csv file. It contains the simulation parameters using a Map in which the
     *                keys are the names of the parameters
     * @param logger Logger used to store the information about the simulation
     * @return  MovementAlgorithm
     */
    private static SimulationCase chooseSimulationCase(Map<String,String> content, Logger logger)
            throws WrongHeightException, NoSuchAlgorithmException, ImpossibleException, IOException {
        String simCase = content.get("SimCase");
        switch (simCase){
            case "BaseSimulationCase":
                return new BaseSimulationCase(logger);
            case "ThresholdSimulationCase":
                return new ThresholdSimulationCase(logger);
            case "DestinationSimulationCase":
                return new DestinationSimulationCase(logger, Objects.equals(content.get("destination"), "IN"));
            case "FixedHeightSimulationCase": {
                return new FixedHeightSimulationCase(logger, Objects.equals(content.get("destination"), "IN"),
                        Double.parseDouble(content.get("height")));
            }
            case "NoTrapSimulationCase":
                return new NoTrapSimulationCase(logger);
            case "SamePosSimulationCase":
                return new SamePosSimulationCase(logger, Objects.equals(content.get("destination"), "IN"));
            case "DiffBeaconSimulationCase":
                return new DiffBeaconSimulationCase(logger, Objects.equals(content.get("destination"), "IN"));
            case "MinHeightSimulationCase":
                return new MinHeightSimulationCase(logger, Objects.equals(content.get("destination"), "IN"),
                        Double.parseDouble(content.get("height")));
            case "BnsInSimulationCase":
                return new BnsInSimulationCase(logger, Objects.equals(content.get("destination"), "IN"),
                        Double.parseDouble(content.get("height")));
            case "SquareSimulationCase":
                return new SquareSimulationCase(logger, Objects.equals(content.get("destination"), "IN"));
            case "MultiCirclesSimulationCase":
                return new MultiCirclesSimulationCase(logger);
            case "LibrarySimulationCase":
                return new LibrarySimulationCase(logger);
            case "GoldmanSimulationCase":
                return new GoldmanSimulationCase(logger);
            case "SmartDoSSimulationCase":
                return new SmartDoSSimulationCase(logger);
            case "SpoofingSimulationCase":
                return new SpoofingSimulationCase(logger);
            case "SniffingSimulationCase":
                return new SniffingSimulationCase(logger);
            case "DisruptiveSmartDoSSimulationCase":
                return new DisruptiveSmartDoSSimulationCase(logger);
            default:
                throw new NoSuchAlgorithmException();
        }
    }
}
