package com.federico.target_tracker.simulator.simulationCase;

import com.federico.target_tracker.bionanothings.Beacon;
import com.federico.target_tracker.bionanothings.Receiver;
import com.federico.target_tracker.logger.Logger;
import com.federico.target_tracker.movementAlgorithm.MovementAlgorithm;
import com.federico.target_tracker.utils.ImpossibleException;
import javafx.geometry.Point2D;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by federico on 18/07/16.
 */

/**
 * For each simulation, beacons and BNSs have the same start position
 */
public class SamePosSimulationCase extends DestinationSimulationCase{

    private List<Point2D> beaconsPos;

    private List<Point2D> receiversPos;


    /**
     * Constructor of the DestinationSimulationCase class
     *  @param logger  Logger used to store the simulation data
     * @param inOrOut Argument that defines if the destination must be placed inside or outside
     */
    public SamePosSimulationCase(Logger logger, boolean inOrOut) throws ImpossibleException {
        super(logger, inOrOut);

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
        beaconsList.add(new Beacon(receivers+3+0,Double.parseDouble(content.get("b1X")),
                Double.parseDouble(content.get("b1Y"))));
        beaconsList.add(new Beacon(receivers+3+1,Double.parseDouble(content.get("b2X")),
                Double.parseDouble(content.get("b2Y"))));
        beaconsList.add(new Beacon(receivers+3+2,Double.parseDouble(content.get("b3X")),
                Double.parseDouble(content.get("b3Y"))));

        destination = new Point2D(Double.parseDouble(content.get("tarX")),Double.parseDouble(content.get("tarY")));

        for(int i=0;i<receivers;i++)
            receiversList.add(new Receiver(i,Double.parseDouble(content.get("recX")),
                    Double.parseDouble(content.get("recY")),getMovingAlgorithm()));

        beaconsPos = new ArrayList<>(beacons);

        for(Beacon beacon : beaconsList)
            beaconsPos.add(beacon.getPosition());

        receiversPos = new ArrayList<>(receivers);

        for(Receiver receiver : receiversList)
            receiversPos.add(receiver.getPosition());
    }

    @Override
    public void execute(int runNumber){



        logger.startLogging(runNumber);


        double t = 0; //Start time

        logger.logDestination(destination.getX()+","+destination.getY());

        double oldT = -Double.MAX_VALUE;

        while(t < time) {

            if((t - oldT) > Tlog) {
                //Print receivers' placement every Tlog seconds to restrict the quantity of data

                for (Receiver receiver : receiversList) {

                    logger.logReceiver(receiver.getID() + "," + receiver.getPosition().getX()
                            + "," + receiver.getPosition().getY());
                }

                oldT = t;
            }

            //Move all sensors by one step
            for (Receiver receiver : receiversList)
                receiver.moveOneStep();


            //Compute the beacons emission of attractants
            for (Receiver receiver : receiversList)
                receiver.computeNextAngle(beaconsList, psiA, psiR);


            t = t + dt;
        }

        //Print beacons placement
        for (Beacon beacon : beaconsList) {
            logger.logBeacon(beacon.getID() + "," + beacon.getPosition().getX()
                    + "," + beacon.getPosition().getY());
        }

        //Close all output files

        logger.stopLogging();

        for(int i = 0; i<beacons; i++){
            Beacon beacon = beaconsList.get(i);
            beacon.setPosition(beaconsPos.get(i).getX(),beaconsPos.get(i).getY());
        }

        for(int i = 0; i<receivers; i++){
            Receiver receiver = receiversList.get(i);
            receiver.setPosition(receiversPos.get(i).getX(),receiversPos.get(i).getY());
        }
    }
}
