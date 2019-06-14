package com.federico.target_tracker.dna;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by federico on 27/05/17.
 */
public class Plasmid implements IPlasmid {

    private int ID;
    private ArrayList<DNABase> bases;
    private static int maxBases = 200;

    Plasmid(int id){
        bases = new ArrayList<>();
        ID = id;
    }

    public ArrayList<DNABase> getBases(){
        return bases;
    }

    public int getCapacity(){
        return maxBases;
    }

    public int getID(){
        return ID;
    }

    public void addBase(DNABase base){
        bases.add(base);
    }

    public int getNumberOfBases(){
        return bases.size();
    }

    @Override
    public String toString(){
        String plasmid = "";
        for(DNABase base : bases){
            plasmid += base.toString(base);
        }
        return plasmid;
    }
}
