package com.federico.target_tracker.bionanothings;

import com.federico.target_tracker.dna.IPlasmid;
import com.federico.target_tracker.movementAlgorithm.LibraryMovementAlgorithm;
import com.federico.target_tracker.movementAlgorithm.MovementAlgorithm;
import com.federico.target_tracker.movementAlgorithm.ToPointMovementAlgorithm;
import com.federico.target_tracker.utils.ConjugationFailedException;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by federico on 27/05/17.
 */
public class PlasmidRetriever extends Receiver {

    protected boolean isConjugating; //can't move during conjugation process

    private int clusterID; //id of the cluster to reach

    protected boolean shouldGoToCluster; //go towards clusters or back, if false and isConjugating
                                //don't move

    protected boolean shouldGoToDestination;

    private Point2D destinationPoint;

    private static int conjugationNeededTime = 1500; // 25 min * 60s

    protected double conjugationCounter;

    private int capacity;

    private static double threshold;

    private ArrayList<IPlasmid> plasmids;

    protected double psiA;

    protected double psiR;

    /**
     * Constructor of the Receiver class
     *
     * @param id        Numerical identifier of the receiver
     * @param x         Position on the x-axis of the receiver. The point (0,0) is the center of the square
     * @param y         Position on the y-axis of the receiver. The point (0,0) is the center of the square
     * @param algorithm Algorithm used by receivers to move in the simulation area
     */
    public PlasmidRetriever(int id, double sx, double sy, double x, double y, MovementAlgorithm algorithm, int cid) {
        super(id, sx, sy, algorithm);
        isConjugating = false;
        clusterID = cid;
        shouldGoToCluster = true;
        shouldGoToDestination = false;
        plasmids = null;
        conjugationCounter = conjugationNeededTime;
        capacity = (int) new Random().nextGaussian() * 10 + 100;
        destinationPoint = new Point2D(x,y);
    }

    public boolean shouldGoToCluster(){
        return shouldGoToCluster;
    }

    public boolean isConjugating(){
        return isConjugating;
    }

    @Override
    public void computeNextAngle(ArrayList<Beacon> beacons, double psi_a, double psi_r){

        psiA = psi_a;
        psiR = psi_r;
        ArrayList<Beacon> correctBeacons = new ArrayList<>();
        for(Beacon b : beacons){
            if(shouldGoToCluster) {
                if (!((LibraryBeacon) b).isStartBeacon() && ((LibraryBeacon) b).isClusterBeacon())
                    correctBeacons.add(b);
            }
            else
                if (shouldGoToDestination) {
                    if (!((LibraryBeacon) b).isStartBeacon() &&
                            ! ((LibraryBeacon) b).isClusterBeacon())
                        correctBeacons.add(b);
                }
        }

        super.computeNextAngle(correctBeacons, psi_a, psi_r);
    }

    @Override
    public void moveOneStep(){
            super.moveOneStep();
    }

    public void startConjugationWithCluster(ClusterBacteria cluster){
        try {
            isConjugating = true;
            shouldGoToCluster = false;
            shouldGoToDestination = true;
            plasmids = cluster.conjugate();
            ((LibraryMovementAlgorithm) algorithm).reSetDestination(destinationPoint);

        } catch (ConjugationFailedException e) {
            shouldGoToDestination = false;
            shouldGoToCluster = true;
            isConjugating = false;
            plasmids = null;
            ((LibraryMovementAlgorithm) algorithm).reSetDestination(cluster.getPosition());
            //e.printStackTrace();
            //conjugation failed
        }
    }

    public ArrayList<IPlasmid> conjugationStep(double step, ArrayList<Beacon> beacons){

        if(conjugationCounter <= 0){

            isConjugating = false;
            if(!isNearEndPoint()){
                /*System.out.println("GOING TO DESTINATION, BITCH! "+
                        ((LibraryMovementAlgorithm)algorithm).getDestination());*/
                moveOneStep();
                computeNextAngle(beacons, psiA, psiR);
                return null;
            }
            else{ //arrived, stop
                shouldGoToDestination = false;
                algorithm.shouldMove(false);
                conjugationCounter = -Integer.MAX_VALUE;
                return dropPlasmids();
            }

        }
        else
            conjugationCounter -= step;

        return null;
    }

    public  boolean canConjugateWithCluster(ClusterBacteria cluster){
        return (this.getDistance(cluster) < threshold) && !cluster.isConjugating();
    }

    public int getClusterID(){
        return clusterID;
    }

    private ArrayList<IPlasmid> dropPlasmids(){
        HashSet<IPlasmid> copy = new HashSet<>();
        if(plasmids != null){
            if (plasmids.size() > capacity)
                for(int i = 0; i < capacity; i++){
                    int index = new Random().nextInt(plasmids.size());
                    copy.add(plasmids.get(index));
                }
            else
                copy.addAll(plasmids);
            plasmids = null;
        }
        ArrayList<IPlasmid> list = new ArrayList<>();
        list.addAll(copy);
        return list;
    }

    public static void setThreshold(double t){
        threshold = t;
    }

    protected boolean isNearEndPoint(){
        return Math.abs(this.getPosition().distance(destinationPoint))<threshold;
    }

    public boolean shouldGoToDestination(){
        return shouldGoToDestination;
    }
}
