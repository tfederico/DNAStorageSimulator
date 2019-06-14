package com.federico.target_tracker.movementAlgorithm;

import com.federico.target_tracker.bionanothings.Beacon;
import com.federico.target_tracker.bionanothings.Bionanothing;
import javafx.geometry.Point2D;

import java.util.ArrayList;

/**
 * Created by federico on 25/05/16.
 */

/**
 *Class that defines a concrete MovementAlgorithm. This algorithm is really simple: if a beacon emits attractants or repellents, the concentration detected considering that beacon is added to the whole concentration
 */
public class BasicMovementAlgorithm extends MovementAlgorithm{

    /**
     * Constructor of the BasicMovementAlgorithm class
     * @param speed Speed of the receivers
     * @param D Rotational coefficient of the receivers (expressed in rad^2 /s\end)
     * @param deltaT Delta time (in seconds) that divides one step of the simulation from the previous one
     * @param L Side of the square in which the simulation takes place
     * @param gamma Coefficient that defines the degree of disturbance
     */
    public BasicMovementAlgorithm(double speed, double D, double deltaT, double L, double gamma) {
        super(speed, D, deltaT, L, gamma);
    }

    /**
     * Method that calculates the concentration of repellents in the given point. The basic version calculates the concentration as the sum of the detected concentration considering one beacon at a time
     * @param beacons Ensemble of the beacons used during the simulation
     * @param x Position on the x-axis of the point from where calculate the repellent concentration
     * @param y Position on the y-axis of the point from where calculate the repellent concentration
     * @return  double
     */
    @Override
    protected double checkRepellentConcentration(ArrayList<Beacon> beacons, double x, double y) {
        Point2D testPoint = new Point2D(x, y);
        double perceivedRepellent = 0;

        for (Bionanothing beacon : beacons) {
            if (beacon.isEmittingRepellents())
                perceivedRepellent += Math.exp(-Math.pow(testPoint.distance(beacon.getPosition()), 2));
        }

        return perceivedRepellent;
    }

    /**
     * Method that calculates the concentration of attractants in the given point. The basic version calculates the concentration as the sum of the detected concentration considering one beacon at a time
     * @param beacons Ensemble of the beacons used during the simulation
     * @param x Position on the x-axis of the point from where calculate the attractant concentration
     * @param y Position on the x-axis of the point from where calculate the attractant concentration
     * @return  double
     */
    @Override
    protected double checkAttractantConcentration(ArrayList<Beacon> beacons, double x, double y) {
        Point2D testPoint = new Point2D(x, y);
        double perceivedAttractant = 0;

        for (Bionanothing beacon : beacons) {
            if (beacon.isEmittingAttractants())
                perceivedAttractant += Math.exp(-Math.pow(testPoint.distance(beacon.getPosition()), 2));
        }

        return perceivedAttractant;
    }
}
