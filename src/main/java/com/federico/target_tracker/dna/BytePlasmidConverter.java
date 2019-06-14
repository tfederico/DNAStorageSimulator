package com.federico.target_tracker.dna;

import com.federico.target_tracker.utils.InformationManager;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;

/**
 * Created by federico on 27/05/17.
 */
public class BytePlasmidConverter implements IBytePlasmidConverter {
    @Override
    public ArrayList<IPlasmid> convert(BitSet bits) {

        ArrayList<DNABase> bases = toDNABases(bits);

        return toPlasmids(bases);
    }

    protected ArrayList<DNABase> toDNABases(BitSet bits){
        ArrayList<DNABase> bases = new ArrayList<>();
        int i = 0;

        while (i < bits.size() - 2){
            BitCouple couple = new BitCouple(bits.get(i),bits.get(i+1));
            switch (couple.toString()){
                case("00"):
                    bases.add(DNABase.A);
                    break;
                case("01"):
                    bases.add(DNABase.C);
                    break;
                case("10"):
                    bases.add(DNABase.G);
                    break;
                case("11"):
                    bases.add(DNABase.T);
                    break;
            }
            i+=2;
        }

        return bases;
    }

    protected ArrayList<IPlasmid> toPlasmids(ArrayList<DNABase> bases){
        ArrayList<IPlasmid> plasmids = new ArrayList<>();
        int ID = 0;
        IPlasmid plasmid = new Plasmid(ID);
        int capacity = plasmid.getCapacity();
        int lastBase = 0;
        while (lastBase < bases.size()) {
            //System.out.println("Capacity: "+capacity);
            //System.out.println("Number of DNA bases: "+bases.size());
            while (capacity > 0 && lastBase < bases.size()) {
                //System.out.println(lastBase);
                plasmid.addBase(bases.get(lastBase));
                lastBase++;
                capacity--;
            }
            plasmids.add(plasmid);
            ID++;
            plasmid = new Plasmid(ID);
            capacity = plasmid.getCapacity();
        }

        //System.out.println("Bases converted to plasmids");
        return plasmids;
    }

    private class BitCouple{
        boolean firstBit;
        boolean secondBit;

        BitCouple(boolean first, boolean second){
            firstBit = first;
            secondBit = second;
        }

        public String toString(){
            int i,j;
            if(firstBit)
                i=1;
            else
                i=0;
            if(secondBit)
                j=1;
            else
                j=0;
            return i+""+j;
        }
    }
}
