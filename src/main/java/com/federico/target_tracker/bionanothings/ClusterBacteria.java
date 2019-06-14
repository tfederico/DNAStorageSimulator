package com.federico.target_tracker.bionanothings;

import com.federico.target_tracker.dna.IPlasmid;
import com.federico.target_tracker.dna.Plasmid;
import com.federico.target_tracker.utils.ConjugationFailedException;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by federico on 27/05/17.
 */
public class ClusterBacteria extends AbsBionanothing {

    private ArrayList<IPlasmid> plasmids;
    private int nPlasmids;

    private int maxConjugability;
    private int conjugatingNumber = 0;


    /**
     * Constructor of the AbsBionanothing class
     *
     * @param id Numerical identifier of the bionanothing
     * @param x  Position on the x-axis of the bionanothing. The point (0,0) is the center of the square
     * @param y  Position on the y-axis of the bionanothing. The point (0,0) is the center of the square
     */
    public ClusterBacteria(int id, double x, double y) {
        super(id, x, y);
        nPlasmids = (int) new Random().nextGaussian() * 10 + 100;
        plasmids = new ArrayList<>(nPlasmids);
        maxConjugability = 50;
    }

    ArrayList<IPlasmid> conjugate() throws ConjugationFailedException {
        double prob = new Random().nextGaussian();
        if(prob < -1)
            throw new ConjugationFailedException();
        conjugatingNumber += 1;
        return plasmids;
    }


    public int getCapacity(){
        return nPlasmids;
    }

    public ArrayList<IPlasmid> getPlasmids(){
        return plasmids;
    }

    @Override
    public int getID(){
        return super.getID();
    }

    boolean isConjugating(){
        return conjugatingNumber >= maxConjugability;
    }

    public void setConjugating(boolean b){
        if(!b)
            conjugatingNumber -= 1;
    }
}
