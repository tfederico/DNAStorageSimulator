package com.federico.target_tracker.dna;

import com.federico.target_tracker.utils.InformationManager;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;

/**
 * Created by federico on 27/05/17.
 */
public interface IBytePlasmidConverter {
    ArrayList<IPlasmid> convert(BitSet b);
}
