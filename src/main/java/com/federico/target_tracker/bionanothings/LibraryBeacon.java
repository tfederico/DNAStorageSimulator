package com.federico.target_tracker.bionanothings;

/**
 * Created by federico on 27/05/17.
 */
public class LibraryBeacon extends Beacon {


    private boolean isStartBeacon;
    private boolean isClusterBeacon;
    /**
     * Constructor of the Beacon class
     *
     * @param id Numerical identifier of the beacon
     * @param x  Position on the x-axis of the beacon. The point (0,0) is the center of the square
     * @param y  Position on the y-axis of the beacon. The point (0,0) is the center of the square
     */
    public LibraryBeacon(int id, double x, double y, boolean isStart, boolean isCluster) {
        super(id, x, y);
        isStartBeacon = isStart;
        isClusterBeacon = isCluster;
    }

    public boolean isStartBeacon(){
        return isStartBeacon;
    }

    public boolean isClusterBeacon(){
        return isClusterBeacon;
    }
}
