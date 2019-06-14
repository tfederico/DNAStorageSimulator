package com.federico.target_tracker.dna;

import com.federico.target_tracker.utils.HuffmanCode;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Objects;

public class GoldmanBytePlasmidConverter extends BytePlasmidConverter {

    @Override
    protected ArrayList<DNABase> toDNABases(BitSet bits){

        HashMap<Character, String> charMap = toHuffmanEncoding(bits);


        //convert to bases using Microsoft algorithm
        ArrayList<DNABase> bases = new ArrayList<>();

        byte[] bytes = bits.toByteArray();

        DNABase previous = null;

        for (byte b : bytes){
            String huffmanCode = charMap.get(Character.valueOf((char) (b+128)));
            
            for(int i = 0; i < huffmanCode.length(); i++){
               char bit = huffmanCode.charAt(i);
               previous = castToBase(bit, previous);
               bases.add(previous);
            }
        }

        return bases;
    }

    private HashMap<Character, String> toHuffmanEncoding(BitSet bits){
        //convert BitSet to byte array
        byte[] bytes = bits.toByteArray();
        int[] charFreqs = new int[256];

        for(int i=0; i<charFreqs.length; i++)
            charFreqs[i]=0;

        for (byte b : bytes)
            charFreqs[b+128]++;

        HuffmanCode h = new HuffmanCode();

        HuffmanCode.HuffmanTree tree = h.buildTree(charFreqs);

        HashMap<Character, String> charMap = new HashMap();
        // print out results
        //System.out.println("SYMBOL\tWEIGHT\tHUFFMAN CODE");

        StringBuffer s = new StringBuffer();
        h.printCodes(tree, s, charMap);

        //System.out.println(charMap.size());

        return charMap;
    }

    private DNABase castToBase(char b, DNABase prev){

        DNABase base;
        if (b == '0'){
            if (prev == null)
                base = DNABase.C;
            else{
                switch (prev){
                    case A:
                        base = DNABase.C;
                        break;
                    case C:
                        base = DNABase.G;
                        break;
                    case G:
                        base = DNABase.T;
                        break;
                    case T:
                        base = DNABase.A;
                        break;
                    default:
                        base = DNABase.C;

                }
            }

        }
        else if(b == '1'){
            if (prev == null)
                base = DNABase.G;
            else {
                switch (prev) {
                    case A:
                        base = DNABase.G;
                        break;
                    case C:
                        base = DNABase.T;
                        break;
                    case G:
                        base = DNABase.A;
                        break;
                    case T:
                        base = DNABase.C;
                        break;
                    default:
                        base = DNABase.G;

                }
            }
        }
        else{ //b==2
            if (prev == null)
                base = DNABase.T;
            else {
                switch (prev) {
                    case A:
                        base = DNABase.T;
                        break;
                    case C:
                        base = DNABase.A;
                        break;
                    case G:
                        base = DNABase.C;
                        break;
                    case T:
                        base = DNABase.G;
                        break;
                    default:
                        base = DNABase.T;

                }
            }
        }

        return base;
    }
}
