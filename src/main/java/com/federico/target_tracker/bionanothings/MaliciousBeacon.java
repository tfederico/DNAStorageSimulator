package com.federico.target_tracker.bionanothings;

public class MaliciousBeacon extends LibraryBeacon {


    private boolean isMalicious;
    /**
     * Constructor of the Beacon class
     *
     * @param id        Numerical identifier of the beacon
     * @param x         Position on the x-axis of the beacon. The point (0,0) is the center of the square
     * @param y         Position on the y-axis of the beacon. The point (0,0) is the center of the square
     * @param isStart
     * @param isCluster
     */
    public MaliciousBeacon(int id, double x, double y, boolean isStart, boolean isCluster, boolean isMalicious) {
        super(id, x, y, isStart, isCluster);
        this.isMalicious = isMalicious;
    }

    public boolean isMalicious(){
        return isMalicious;
    }
}
