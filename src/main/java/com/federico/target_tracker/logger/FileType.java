package com.federico.target_tracker.logger;



/**
 * Created by federico on 25/05/16.
 */

/**
 *Enumeration class that defines the type of file used to store data
 */
public enum FileType {

    /**
     * Enum value used to tell the Parses to save data on a txt file
     */
    TXT,
    /**
     * Enum value used to tell the Parses to save data on a csv file
     */
    CSV;

    /**
     * Method that translates the Enum value of FileType into a String
     * @param type Extension of the file on which data will be stored
     * @return  String
     */
    public static String toString(FileType type){
        switch (type){
            case TXT:
                return "txt";
            case CSV:
                return "csv";
            default:
                return "";

        }
    }
}
