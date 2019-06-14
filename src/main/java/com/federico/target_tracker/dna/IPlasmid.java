package com.federico.target_tracker.dna;

import java.util.ArrayList;

/**
 * Created by federico on 27/05/17.
 */
public interface IPlasmid {
    int getCapacity();

    ArrayList<DNABase> getBases();

    int getID();

    void addBase(DNABase base);

    int getNumberOfBases();
}
