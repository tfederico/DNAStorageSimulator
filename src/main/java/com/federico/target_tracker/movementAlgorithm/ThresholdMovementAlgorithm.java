package com.federico.target_tracker.movementAlgorithm;

import com.federico.target_tracker.bionanothings.Beacon;
import com.federico.target_tracker.bionanothings.Bionanothing;
import javafx.geometry.Point2D;

import java.util.ArrayList;

/**
 * Created by federico on 25/05/16.
 */

/**
 *Class that redefines the BasicMovementAlgorithm. This algorithm is a step further the one of its superclass: if a beacon emits attractants or repellents, the concentration detected considering that beacon is added to the whole concentration only if it overcomes an established threshold
 */
public class ThresholdMovementAlgorithm extends BasicMovementAlgorithm {

    /**
     * Threshold of attractants that defines a lower bound under which the receiver does not detect any attractant
     */
    private static final double Ha = 0.2;

    /**
     * Threshold of receivers that defines a lower bound under which the receiver does not detect any receiver
     */
    private static final double Hr = 0.2;

    /**
     * Constructor of the ThresholdMovementAlgorithm class
     * @param speed Speed of the receivers
     * @param D Rotational coefficient of the receivers (expressed in \begin{math}rad^2 /s\end{math})
     * @param deltaT Delta time (in seconds) that divides one step of the simulation from the previous one
     * @param L Side of the square in which the simulation takes place
     * @param gamma Coefficient that defines the degree of disturbance
     */
    public ThresholdMovementAlgorithm(double speed, double D, double deltaT, double L, double gamma) {
        super(speed, D, deltaT, L, gamma);
    }

    /**
     * Method that calculate the concentration of repellents in the given point. This version calculates the concentration as the sum of the detected concentration considering one beacon at a time but only if the single concentration exceed the threshold
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
            if (beacon.isEmittingRepellents() && Math.exp(-Math.pow(testPoint.distance(beacon.getPosition()), 2)) >= Hr)
                perceivedRepellent += Math.exp(-Math.pow(testPoint.distance(beacon.getPosition()), 2));
        }

        return perceivedRepellent;

    }

    /**
     * Method that calculate the concentration of attractants in the given point. This version calculates the concentration as the sum of the detected concentration considering one beacon at a time but only if the single concentration exceed the threshold
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
            if (beacon.isEmittingAttractants() && Math.exp(-Math.pow(testPoint.distance(beacon.getPosition()), 2)) >= Ha)
                perceivedAttractant += Math.exp(-Math.pow(testPoint.distance(beacon.getPosition()), 2));

        }

        return perceivedAttractant;
    }

}
