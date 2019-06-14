package com.federico.target_tracker.simulator.simulationCase;

import com.federico.target_tracker.bionanothings.Beacon;
import com.federico.target_tracker.bionanothings.Receiver;
import com.federico.target_tracker.logger.FileLogger;
import com.federico.target_tracker.logger.FileType;
import com.federico.target_tracker.logger.Logger;
import com.federico.target_tracker.movementAlgorithm.MovementAlgorithm;
import com.federico.target_tracker.utils.ImpossibleException;
import com.federico.target_tracker.utils.TooMuchInfoException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by federico on 25/05/16.
 */
public abstract class AbsSimulationCase implements SimulationCase {

    /**
     * Number of beacons in the simulation
     */
    protected final int beacons;

    /**
     * Ensemble of the beacons of the simulation
     */
    protected ArrayList<Beacon> beaconsList;

    /**
     * Rotational coefficient of the receivers (expressed in \begin{math}rad^2 /s\end{math})
     */
    protected final int D;

    /**
     * Delta time (in seconds) that divides one step of the simulation from the previous one
     */
    protected final double dt;

    /**
     * Coefficient that defines the degree of disturbance
     */
    protected final double gamma;

    /**
     * Lenght of the side of the square in which the simulation takes place
     */
    protected final double L;

    /**
     * Logger used to store the information about the simulation
     */
    protected Logger logger;

    /**
     * Attractant inducted angle (expressed in rad)
     */
    protected final double psiA;

    /**
     * Repellent inducted angle (expressed in rad)
     */
    protected final double psiR;

    /**
     * Number of receivers in the simulation
     */
    protected int receivers;

    /**
     * Ensemble of the receivers of the simulation
     */
    protected ArrayList<Receiver> receiversList;

    /**
     * Number of seconds in which the simulation keeps going on
     */
    protected final int time;

    /**
     * Speed of the receivers
     */
    protected final double v;

    /**
     * Pause between two logs of the receiver data
     */
    protected final int Tlog;

    /**
     * Attribute that defines if the beacons should emit attractants
     */
    protected final boolean shouldAllEmitsAttractans;

    /**
     * Attribute that defines if the beacons should emit repellents
     */
    protected final boolean shouldAllEmitsRepellents;

    /**
     * Constructor of the AbsSimulationCase class. It reads the simulation parameters from the file config.csv
     * @param logger Logger used to store the simulation data
     */
    AbsSimulationCase(Logger logger){
        this.logger = logger;
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

        dt = Double.parseDouble(content.get("dt"));
        receivers = Integer.parseInt(content.get("NBS"));;
        L = Double.parseDouble(content.get("L"));
        v = Double.parseDouble(content.get("v"));
        time = Integer.parseInt(content.get("T"));
        D  = Integer.parseInt(content.get("D"));
        psiA = Double.parseDouble(content.get("psiA"));
        psiR = Double.parseDouble(content.get("psiR"));
        gamma = Double.parseDouble(content.get("v"));
        Tlog = Integer.parseInt(content.get("Tlog"));
        shouldAllEmitsAttractans = Objects.equals(content.get("attractants"), "yes");
        shouldAllEmitsRepellents = Objects.equals(content.get("repellents"), "yes");
        beacons = Integer.parseInt(content.get("Nbeacon"));
        beaconsList = new ArrayList<>(beacons);
        receiversList = new ArrayList<>(receivers);

    }

    /**
     * Method that defines the behaviour of a simulation case, which matches with the steps to take in order to complete a simulation
     * @param runNumber Integer which correspond to the number of simulation case executed by the simulator
     */
    public void execute(int runNumber) throws ImpossibleException, TooMuchInfoException {

        logger.startLogging(runNumber);

        double t = 0; //Start time

        placeBeacons();

        //Add all receivers to a list

        placeReceivers();


        double oldT = -Double.MAX_VALUE;

        while(t < time) {
            if((t - oldT) > Tlog) {
                //Print receivers' placement every 25 seconds to restrict the quantity of data

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
            for (Receiver receiver : receiversList) {
                receiver.computeNextAngle(beaconsList, psiA, psiR);
            }

            t = t + dt;
        }

        //Print beacons placement
        for (Beacon beacon : beaconsList) {
            logger.logBeacon(beacon.getID() + "," + beacon.getPosition().getX()
                    + "," + beacon.getPosition().getY());
        }

        //Close all output files

        logger.stopLogging();
        beaconsList.clear();
        receiversList.clear();

    }

    /**
     * Method that defines the algorithm used to place the beacons in the simulation area. The default algorithm is placing the beacons in random positions inside the simulation square
     */
    protected void placeBeacons() throws ImpossibleException {
        //Add all beacons to a list
        for (int i=0; i < beacons; i++) {
            //random positioning the beacons
            Beacon b = new Beacon(receivers+3+i, Math.random()-L/2, Math.random()-L/2);
            b.emitAttractants(shouldAllEmitsAttractans);
            b.emitRepellents(shouldAllEmitsRepellents);
            beaconsList.add(b);
        }
    }

    /**
     * Method that defines the algorithm used to place the receivers in the simulation area. The default algorithm is placing the receivers in random positions inside the simulation square
     */
    protected void placeReceivers(){
        for (int i=0; i < receivers; i++) {
            //random positioning the receiver
            receiversList.add(new Receiver(i, Math.random()-L/2, Math.random()-L/2, getMovingAlgorithm()));
        }
    }

    /**
     * Method which returns the algorithm used by the receivers to move inside the simulation area
     * @return  MovementAlgorithm
     */
    protected abstract MovementAlgorithm getMovingAlgorithm();
}
