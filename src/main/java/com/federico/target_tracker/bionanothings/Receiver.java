package com.federico.target_tracker.bionanothings;

import javafx.geometry.Point2D;
import com.federico.target_tracker.movementAlgorithm.MovementAlgorithm;

import java.util.ArrayList;

/**
 * Created by federico on 24/05/16.
 */

/**
 *Class that describes a special type of bionanosensor which is able to move following chemical substances such as attractants or repellents
 */
public class Receiver extends AbsBionanothing{

    /**
     * Algorithm used by receivers to move during the simulation
     */
    protected MovementAlgorithm algorithm;

    /**
     * Constructor of the Receiver class
     * @param id Numerical identifier of the receiver
     * @param x Position on the x-axis of the receiver. The point (0,0) is the center of the square
     * @param y Position on the y-axis of the receiver. The point (0,0) is the center of the square
     * @param algorithm Algorithm used by receivers to move in the simulation area
     */
    public Receiver(int id, double x, double y, MovementAlgorithm algorithm) {
        super(id, x, y);
        this.algorithm = algorithm;
    }

    /**
     * Method that computes the next drift angle of the receiver
     * @param beacons Ensemble of the beacons used during the simulation
     * @param psi_a Attractant inducted angle  (expressed in rad)
     * @param psi_r Repellent inducted angle  (expressed in rad)
     */
    public void computeNextAngle(ArrayList<Beacon> beacons, double psi_a, double psi_r){
        algorithm.computeAngle(beacons, psi_a, psi_r, getPosition());
    }

    /**
     * Method that calculate the position of the receiver at the next step of the simulation
     */
    public void moveOneStep(){
        Point2D newPosition = algorithm.moveOneStep(getPosition());
        setPosition(newPosition.getX(), newPosition.getY());
    }

    /**
     * Method that checks if a bionanothing is emitting attractants or not
     * @return  boolean
     */
    @Override
    public boolean isEmittingAttractants(){
        return super.isEmittingAttractants();
    }

    /**
     * Method that checks if a bionanothing is emitting repellents or not
     * @return  boolean
     */
    @Override
    public boolean isEmittingRepellents(){
        return super.isEmittingRepellents();
    }

    /**
     * Method used to order to a bionanothing to start or stop emitting attractants
     * @param attractants Boolean variable used to tell to a bionanothing to start or stop to emit attractans
     */
    @Override
    public void emitAttractants(boolean attractants) {
        super.emitAttractants(attractants);
    }

    /**
     * Method that orders to a bionanothing to start or stop emitting repellents
     * @param repellents Boolean variable used to tell to a bionanothing to start or stop to emit repellents
     */
    @Override
    public void emitRepellents(boolean repellents) {
        super.emitRepellents(repellents);
    }
    

}
