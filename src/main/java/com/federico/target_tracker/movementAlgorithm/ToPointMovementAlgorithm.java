package com.federico.target_tracker.movementAlgorithm;

import com.federico.target_tracker.bionanothings.Beacon;
import com.federico.target_tracker.bionanothings.Bionanothing;
import javafx.geometry.Point2D;

import java.util.ArrayList;

/**
 * Created by federico on 01/06/16.
 */

/**
 *Class that defines a concrete MovementAlgorithm. This algorithm considers the difference of concentration detected from the receiver to the destination and if it is greater than zero, it moves the receivers towards the destination
 */
public class ToPointMovementAlgorithm extends MovementAlgorithm {

    /**
     * Point in the space that the receiver must reach
     */
    protected Point2D destination;

    /**
     * Constructor of the ToPointMovementAlgorithm class
     * @param speed Speed of the receivers
     * @param D Rotational coefficient of the receivers (expressed in \begin{math}rad^2 /s\end{math})
     * @param deltaT Delta time (in seconds) that divides one step of the simulation from the previous one
     * @param L Side of the square in which the simulation takes place
     * @param gamma Coefficient that defines the degree of disturbance
     * @param destination Point to reach during the simulation
     */
    public ToPointMovementAlgorithm(double speed, double D, double deltaT, double L, double gamma, Point2D destination) {
        super(speed, D, deltaT, L, gamma);
        this.destination = destination;
    }

    /**
     * Method that calculates the concentration of repellents in the given point. This version calculates the concentration as the sum of the detected concentration considering one beacon at a time
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
            double testRepellent = Math.exp(-Math.pow(testPoint.distance(beacon.getPosition()), 2));
            double destinationRepellent = Math.exp(-Math.pow(destination.distance(beacon.getPosition()), 2));
            if (beacon.isEmittingRepellents() && testRepellent - destinationRepellent> 0)
                perceivedRepellent += testRepellent;
        }

        return perceivedRepellent;
    }

    /**
     * Method that calculates the concentration of attractants in the given point. This version calculates the concentration as the sum of the detected concentration considering one beacon at a time
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
            double testAttractant = Math.exp(-Math.pow(testPoint.distance(beacon.getPosition()), 2));
            double destinationAttractant = Math.exp(-Math.pow(destination.distance(beacon.getPosition()), 2));
            if (beacon.isEmittingAttractants() && destinationAttractant - testAttractant > 0)
                perceivedAttractant += testAttractant;
        }
        return perceivedAttractant;
    }

    /**
     * Method used to get the destination of the simulation
     * @return  Point2D
     */
    public Point2D getDestination(){
        return destination;
    }
}
