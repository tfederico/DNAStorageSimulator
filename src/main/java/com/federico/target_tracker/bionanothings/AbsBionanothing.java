package com.federico.target_tracker.bionanothings;

import javafx.geometry.Point2D;

/**
 * Created by federico on 24/05/16.
 */

/**
 *Abstract class that defines the features of a bionanothing and its information
 */

public abstract class AbsBionanothing implements Bionanothing{

    /**
     * Numerical identifier of the bionanothing
     */

    private final int ID;

    /**
     * Position of the bionanothing
     */
    private Point2D position;

    /**
     * Boolean variable which tells if the bionanothing is emitting attractants or not
     */
    private boolean isEmittingAttractans = false;

    /**
     * Boolean variable which tells if the bionanothing is emitting repellents or not
     */

    private boolean isEmittingRepellents = true;

    /**
     * Constructor of the AbsBionanothing class
     * @param id Numerical identifier of the bionanothing
     * @param x Position on the x-axis of the bionanothing. The point (0,0) is the center of the square
     * @param y Position on the y-axis of the bionanothing. The point (0,0) is the center of the square
     */
    AbsBionanothing(int id,double x, double y){
        ID = id;
        position = new Point2D(x,y);
    }

    /**
     * Method that calculates the distance between two bionanothings
     * @param fellow Bionanothing whence calculate the distance
     * @return  double
     */
    public double getDistance(Bionanothing fellow) {
        return position.distance(fellow.getPosition());
    }

    /**
     * Method used to get the position of the bionanothing
     * @return  Point2D
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * Method used to set the position of the bionanothing
     * @param x New position on the x-axis of the bionanothing. The point (0,0) is the center of the square
     * @param y New position on the y-axis of the bionanothing. The point (0,0) is the center of the square
     */
    public void setPosition(double x, double y){
        position = new Point2D(x,y);
    }

    /**
     * Method used to get the identifier of the bionanothing
     * @return  int
     */
    public int getID(){
        return ID;
    }

    /**
     * Method that checks if a bionanothing is emitting attractants or not
     * @return  boolean
     */
    public boolean isEmittingAttractants() {
        return isEmittingAttractans;
    }

    /**
     * Method that checks if a bionanothing is emitting repellents or not
     * @return  boolean
     */
    public boolean isEmittingRepellents() {
        return isEmittingRepellents;
    }

    /**
     * Method that orders to a bionanothing to start or stop emitting attractants
     * @param attractants Boolean variable used to tell to a bionanothing to start or stop to emit attractans
     */
    public void emitAttractants(boolean attractants) {
        isEmittingAttractans = attractants;
    }

    /**
     * Method that orders to a bionanothing to start or stop emitting repellents
     * @param repellents Boolean variable used to tell to a bionanothing to start or stop to emit repellents
     */
    public void emitRepellents(boolean repellents) {
        isEmittingRepellents = repellents;
    }

}
