package com.federico.target_tracker.bionanothings;

import com.federico.target_tracker.movementAlgorithm.MovementAlgorithm;

import java.util.ArrayList;

public class SpoofedPlasmidRetriever extends PlasmidRetriever {
    /**
     * Constructor of the Receiver class
     *
     * @param id        Numerical identifier of the receiver
     * @param sx
     * @param sy
     * @param x         Position on the x-axis of the receiver. The point (0,0) is the center of the square
     * @param y         Position on the y-axis of the receiver. The point (0,0) is the center of the square
     * @param algorithm Algorithm used by receivers to move in the simulation area
     * @param cid
     */
    public SpoofedPlasmidRetriever(int id, double sx, double sy, double x, double y, MovementAlgorithm algorithm, int cid) {
        super(id, sx, sy, x, y, algorithm, cid);
    }

    @Override
    public void computeNextAngle(ArrayList<Beacon> beacons, double psi_a, double psi_r){

        psiA = psi_a;
        psiR = psi_r;
        ArrayList<Beacon> correctBeacons = new ArrayList<>();
        for(Beacon b : beacons){
            if(shouldGoToCluster) {
                if ((!((MaliciousBeacon) b).isStartBeacon() && ((MaliciousBeacon) b).isClusterBeacon()) ||
                        ((MaliciousBeacon) b).isMalicious())
                    correctBeacons.add(b);
            }
            else
            if (shouldGoToDestination) {
                if (!((MaliciousBeacon) b).isStartBeacon() && ! ((MaliciousBeacon) b).isClusterBeacon())
                    correctBeacons.add(b);
            }
        }

        super.computeNextAngle(correctBeacons, psi_a, psi_r);
    }
}
