package com.federico.target_tracker.bionanothings;

import javafx.geometry.Point2D;

/**
 * Created by federico on 24/05/16.
 */
/**
 *Interface that define the behaviour of a bionanothing which is mainly emitting attractants or repellents
 */
public interface Bionanothing {

    /**
     * Method that checks if a bionanothing is emitting attractants or not
     * @return  boolean
     */
    public boolean isEmittingAttractants();

    /**
     * Method that checks if a bionanothing is emitting repellents or not
     * @return  boolean
     */
    public boolean isEmittingRepellents();

    /**
     * Method that calculates the distance between two bionanothings
     * @param fellow Bionanothing whence calculate the distance
     * @return  double
     */
    public double getDistance(Bionanothing fellow);

    /**
     * Method used to get the position of the bionanothing
     * @return  Point2D
     */
    public Point2D getPosition();

    /**
     * Method used to order to a bionanothing to start or stop emitting attractants
     * @param attractants Boolean variable used to tell to a bionanothing to start or stop to emit attractans
     */
    public void emitAttractants(boolean attractants);

    /**
     * Method that orders to a bionanothing to start or stop emitting repellents
     * @param repellents Boolean variable used to tell to a bionanothing to start or stop to emit repellents
     */
    public void emitRepellents(boolean repellents);
}
