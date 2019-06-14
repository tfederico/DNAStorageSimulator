package com.federico.target_tracker.dna;

/**
 * Created by federico on 27/05/17.
 */
public enum DNABase {
    A,C,G,T;

    public String toString(DNABase base) {
        if(base.equals(DNABase.A))
            return "A";
        else if(base.equals(DNABase.C))
            return "C";
        else if (base.equals(DNABase.G))
            return "G";
        else
            return "T";
    }
}
