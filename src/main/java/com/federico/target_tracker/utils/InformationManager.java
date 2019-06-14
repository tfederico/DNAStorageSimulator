package com.federico.target_tracker.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;


/**
 * Created by federico on 27/05/17.
 */
public class InformationManager {

    byte [] data;

    public InformationManager(String path) throws IOException {

        data = Files.readAllBytes(Paths.get(path));

    }

    public BitSet getData(){
        return BitSet.valueOf(data);
    }

}
