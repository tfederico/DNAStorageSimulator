package com.federico.target_tracker.parser;

import com.federico.target_tracker.logger.FileType;
import javafx.geometry.Point2D;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by federico on 25/05/16.
 */

/**
 *Classes that defines how to execute the parsing of the simulation data and
 * how to store that data in a more usefull way
 */
public class FileParser implements Parser {


    /**
     * Extention of the file that must be read
     */
    private String readExtention;

    /**
     * Extention of the file that must be wrote
     */
    private String writeExtention;

    private int numberOfTests;


    /**
     * Constructor of the FileParser class
     * @param readFile Extension of the file on which data has been stored
     * @param writeFile Extension of the file on which  the parsed will be stored
     */
    public FileParser(FileType readFile, FileType writeFile, int numberOfTests){
        readExtention = FileType.toString(readFile);
        writeExtention = FileType.toString(writeFile);
        this.numberOfTests = numberOfTests;
    }

    /**
     * Method that parses the data of the simulation reading the files and rewrites them in a second file in order to
     * improve their understandability
     * @param fileNum Number of simulations executed by the simulator
     * @param dir Path of the directory containing the file on which data will be stored
     */
    public void parse(int fileNum, String dir) {

        List<String[]> beaconContent = parseFileByName(dir+"/beacons_placement_"+fileNum+"."+readExtention);
        List<String[]> receiverContent = parseFileByName(dir+"/receivers_placement_"+fileNum+"."+readExtention);
        List<String[]> destinations = parseFileByName(dir+"/destinations."+readExtention);


        List <String> list = prepareToPrint(beaconContent, receiverContent, destinations.get(fileNum));
        print(list, fileNum, dir);

        //printDistance(fileNum,dir);

    }

    /**
     * Method that parse a file with a given name and return a Collection containing the content of the file
     * @param name Name of the file that must be parsed
     * @return  List<String[]>
     */
    private List<String[]> parseFileByName(String name){

        List<String[]> content = new ArrayList<>();
        String csvFile = name;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(cvsSplitBy);
                content.add(data);

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

        return content;
    }

    /**
     * Method used in order to dispose the information that must be stored on a file
     * @param beacon Information about the beacons during the current simulation
     * @param receiver Information about the receiver during the current simulation
     * @param destination Information about the destination during the current simulation
     * @return  List<String>
     */
    private List<String> prepareToPrint(List<String[]> beacon, List<String[]> receiver, String[] destination){

        List<String> list = new ArrayList<>();
        boolean destinationAdded = false;

        Iterator<String[]> it = receiver.iterator();
        for(String [] oneBeacon : beacon) {
            String s = "";
            String[] receiverElementList = it.next();
            //i = 1 to skip the id
            for(int i = 1; i < receiverElementList.length; i++){
                s = s + receiverElementList[i] + ",";
            }

            s += oneBeacon[1].trim()+","+oneBeacon[2].trim();
            if(!destinationAdded){
                s += ","+destination[0]+","+destination[1];
                destinationAdded = true;
            }
            list.add(s);
        }
        //adding one more time the position of the first beacon in order to plot a triangle
        if(it.hasNext()){
            String s = "";
            String[] receiverElementList = it.next();
            String[] oneBeacon = beacon.get(0);
            for(int i = 1; i < receiverElementList.length; i++){
                s = s + receiverElementList[i] + ",";
            }

            s += oneBeacon[1].trim()+","+oneBeacon[2].trim();
            list.add(s);
        }

        while(it.hasNext()){
            String[] receiverElementList = it.next();
            String s = "";
            for(int i = 1; i < receiverElementList.length; i++){
                s = s + receiverElementList[i]+",";
            }

            list.add(s.substring(0,s.length()-1));

        }
        return list;
    }

    /**
     * Method that prints the simulation information on the selected file
     * @param list Ensemble of rows that must be printed on the destination file
     * @param fileNum Number of simulations executed by the simulator
     * @param dir Directory in which the file must be stored
     */
    private void print(List<String> list, int fileNum, String dir){

        //controlling existance of results directory
        File resDir = new File(dir+"/A_results/");
        resDir.mkdir();

        try {
            PrintWriter resWriter = new PrintWriter(dir+"/A_results/result"+fileNum+"."+writeExtention, "UTF-8");
            for(String s : list) {
                resWriter.println(s);
                resWriter.flush();
            }

            resWriter.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that parses the data written by the parse method and calculate the distance between the destination and
     * the receivers. In addition, it writes the distance at every single step on a file
     * @param runNum Number of simulations executed by the simulator
     * @param dir Path of the directory containing the file on which data will be stored
     */
    public void printDistance(int runNum, String dir){

        List<String[]> destinations = parseFileByName(dir+"/destinations."+readExtention);
        List<Point2D> destination = new ArrayList<>();
        List<List<Double>> distances = new ArrayList<>();
        for(String[] strings : destinations)
            destination.add(new Point2D(Double.parseDouble(strings[0]),Double.parseDouble(strings[1])));

        for(int i = 0; i < runNum; i++) {
            List<String[]> receiverContent = parseFileByName(dir+"/receivers_placement_"+i+"."+readExtention);
            List<Point2D> receiverValues = new ArrayList<>();
            for(String[] strings : receiverContent)
                receiverValues.add(new Point2D(Double.parseDouble(strings[1]),Double.parseDouble(strings[2])));
            List<Double> distanceFromDestination = new ArrayList<>();
            for(Point2D receiverPosition : receiverValues)
                distanceFromDestination.add(receiverPosition.distance(destination.get(i)));
            distances.add(distanceFromDestination);
        }

        try {
            PrintWriter resWriter = new PrintWriter(dir+"/A_results/distances"+"."+writeExtention, "UTF-8");
            for(int i = 0;i < distances.get(0).size(); i++){
                String s = "";
                for(List<Double> distanceFromDestination : distances)
                    s = s + distanceFromDestination.get(i) + ",";
                int n = i+1;

                int nLastChar = numberOfTests%26;

                char lastChar = 'A';

                lastChar+=nLastChar-1;
                int nFirstChar = 0;
                if(numberOfTests>26){
                   nFirstChar= numberOfTests/26;
                }

                String avg;
                String var;

                if(nFirstChar==0){
                    avg = "=AVERAGE(A"+n+(":"+lastChar)+n+"),";
                    var = "=VAR(A"+n+(":"+lastChar)+n+")";
                }
                else {
                    char firstChar = 'A';
                    firstChar += nFirstChar;
                    avg = "=AVERAGE(A"+n+(":"+firstChar+""+lastChar)+n+"),";
                    var = "=VAR(A"+n+(":A"+firstChar+""+lastChar)+n+")";
                }

                s += avg + var;
                resWriter.println(s);
                resWriter.flush();

            }

            resWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}

