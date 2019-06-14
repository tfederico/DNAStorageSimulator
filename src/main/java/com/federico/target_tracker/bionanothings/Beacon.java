package com.federico.target_tracker.bionanothings;

/**
 * Created by federico on 24/05/16.
 */

/**
 *Class that defines a special types of bionanothing which emits attractant or repellents
 */
public class Beacon extends AbsBionanothing{

    /**
     * Constructor of the Beacon class
     * @param id Numerical identifier of the beacon
     * @param x Position on the x-axis of the beacon. The point (0,0) is the center of the square
     * @param y Position on the y-axis of the beacon. The point (0,0) is the center of the square
     */
    public Beacon(int id, double x, double y) {
        super(id, x, y);
        emitAttractants(true);
        emitRepellents(false);
    }

    /**
     * Method that checks if a beacon is emitting attractants or not
     * @return  boolean
     */
    @Override
    public boolean isEmittingAttractants(){
        return super.isEmittingAttractants();
    }

    /**
     * Method that checks if a beacon is emitting repellents or not
     * @return  boolean
     */
    @Override
    public boolean isEmittingRepellents(){
        return super.isEmittingRepellents();
    }
}
