package com.federico.target_tracker.dna;

import com.federico.target_tracker.utils.InformationManager;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;

/**
 * Created by federico on 27/05/17.
 */
public class PlasmidByteConverter implements IPlasmidByteConverter {
    @Override
    public byte[] convert(ArrayList<IPlasmid> plasmids) {

        ArrayList<DNABase> bases = toDNABases(plasmids);

        return toByte(bases);
    }

    private ArrayList<DNABase> toDNABases(ArrayList<IPlasmid> plasmids){
        ArrayList<DNABase> bases = new ArrayList<>();
        for(IPlasmid plasmid : plasmids)
            bases.addAll(plasmid.getBases());

        return bases;

    }

    private byte [] toByte(ArrayList<DNABase> bases){

        StringBuffer bytes = new StringBuffer();
        for (DNABase base : bases){
            switch (base){
                case A:
                    bytes.append("00");
                case C:
                    bytes.append("01");
                case G:
                    bytes.append("10");
                case T:
                    bytes.append("11");
            }
        }

        return bytes.toString().getBytes();
    }
}
