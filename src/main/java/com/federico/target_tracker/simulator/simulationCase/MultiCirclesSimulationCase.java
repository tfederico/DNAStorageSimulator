package com.federico.target_tracker.simulator.simulationCase;

import com.federico.target_tracker.bionanothings.Beacon;
import com.federico.target_tracker.bionanothings.Receiver;
import com.federico.target_tracker.logger.Logger;
import com.federico.target_tracker.utils.ImpossibleException;
import com.federico.target_tracker.utils.TooMuchInfoException;
import javafx.geometry.Point2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by federico on 09/08/16.
 */
public class MultiCirclesSimulationCase extends DestinationSimulationCase {

    Map<String, String> content;

    int actualRun;
    /**
     * Constructor of the DestinationSimulationCase class
     *
     * @param logger  Logger used to store the simulation data
     */
    public MultiCirclesSimulationCase(Logger logger) {
        super(logger, true);

        content = new HashMap<>();
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

        beaconsList.add(new Beacon(receivers+3,Double.parseDouble(content.get("b1X")),
                Double.parseDouble(content.get("b1Y"))));
        beaconsList.add(new Beacon(receivers+4,Double.parseDouble(content.get("b2X")),
                Double.parseDouble(content.get("b2Y"))));
        beaconsList.add(new Beacon(receivers+5,Double.parseDouble(content.get("b3X")),
                Double.parseDouble(content.get("b3Y"))));
    }

    @Override
    public void execute(int runNum) throws ImpossibleException, TooMuchInfoException {
        actualRun = runNum;
        beaconsList.add(new Beacon(receivers+3,Double.parseDouble(content.get("b1X")),
                Double.parseDouble(content.get("b1Y"))));
        beaconsList.add(new Beacon(receivers+4,Double.parseDouble(content.get("b2X")),
                Double.parseDouble(content.get("b2Y"))));
        beaconsList.add(new Beacon(receivers+5,Double.parseDouble(content.get("b3X")),
                Double.parseDouble(content.get("b3Y"))));
        super.execute(runNum);
    }

    @Override
    protected void placeBeacons(){
        //do nothing
    }

    @Override
    protected void destinationInGoodPlace(){
        int actualAngle = actualRun%6;
        int nextCircle = (actualRun/6)%3;
        double radius = Double.parseDouble(content.get("Ri"+nextCircle));
        Point2D p;
        if(actualAngle == 0)
            p = new Point2D(0,radius);
        else if(actualAngle == 1)
            p = new Point2D(radius*Math.cos(Math.toRadians(45)),radius*Math.sin(Math.toRadians(45)));
        else if(actualAngle == 2)
            p = new Point2D(radius,0);
        else if(actualAngle == 3)
            p = new Point2D(0,-radius);
        else if (actualAngle == 4)
            p = new Point2D(radius*Math.cos(Math.toRadians(225)),radius*Math.sin(Math.toRadians(225)));
        else
            p = new Point2D(-radius,0);

        destination = new Point2D(p.getX(),p.getY());
    }

    @Override
    protected void placeReceivers(){
        int actualAngle = (actualRun/18)%3;
        int nextCircle = actualRun/54;
        double radius = Double.parseDouble(content.get("Ro"+nextCircle));
        Point2D p;
        if(actualAngle == 0)
            p = new Point2D(0,radius);
        else if(actualAngle == 1)
            p = new Point2D(radius*Math.cos(Math.toRadians(45)),radius*Math.sin(Math.toRadians(45)));
        else
            p = new Point2D(radius,0);

        for(int i = 0; i < receivers; i++)
            receiversList.add(new Receiver(i, p.getX(), p.getY(), getMovingAlgorithm()));
    }
}
