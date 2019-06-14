package com.federico.target_tracker.movementAlgorithm;

import com.federico.target_tracker.bionanothings.Beacon;
import javafx.geometry.Point2D;

import java.util.ArrayList;

/**
 * Created by federico on 27/05/17.
 */
public class LibraryMovementAlgorithm extends ToPointMovementAlgorithm {

    /**
     * Constructor of the ToPointMovementAlgorithm class
     *
     * @param speed       Speed of the receivers
     * @param D           Rotational coefficient of the receivers (expressed in \begin{math}rad^2 /s\end{math})
     * @param deltaT      Delta time (in seconds) that divides one step of the simulation from the previous one
     * @param L           Side of the square in which the simulation takes place
     * @param gamma       Coefficient that defines the degree of disturbance
     * @param destination Point to reach during the simulation
     */
    public LibraryMovementAlgorithm(double speed, double D, double deltaT, double L, double gamma, Point2D destination) {
        super(speed, D, deltaT, L, gamma, destination);
    }

    public void reSetDestination(Point2D newDestination){
        super.destination = newDestination;
    }

    public Point2D getDestination(){
        return destination;
    }
}
