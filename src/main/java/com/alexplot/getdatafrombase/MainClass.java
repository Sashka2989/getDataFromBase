package com.alexplot.getdatafrombase;

import com.alexplot.getdatafrombase.Enums.Commands;


public class MainClass {

    public static void main(String[] args) {


        Commands command = Commands.fromValue(args[0]);
        String inFileName;
        String outFileName;
        inFileName = args[1];
        outFileName = args[2];

        switch (command) {
            case SEARCH:

                break;
            case STAT:

                break;
            default:

        }
    }

}

