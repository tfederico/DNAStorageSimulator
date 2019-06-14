package com.federico.target_tracker.dna;

import com.federico.target_tracker.utils.InformationManager;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by federico on 27/05/17.
 */
public interface IPlasmidByteConverter {
    byte [] convert(ArrayList<IPlasmid> plasmids);
}
