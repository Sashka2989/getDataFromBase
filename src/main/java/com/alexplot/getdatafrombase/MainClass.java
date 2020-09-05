package com.alexplot.getdatafrombase;

import com.alexplot.getdatafrombase.Enums.Commands;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class MainClass {

    public static void main(String[] args) {


        Commands command = Commands.fromValue(args[0]);
        String inFileName;
        String outFileName;
        inFileName = args[1];
        outFileName = args[2];

        switch (command) {
            case SEARCH:
                JSONParser parser = new JSONParser();
                try {
                    FileReader fileReader = new FileReader(inFileName);
                    JSONObject jsonObject = (JSONObject) parser.parse(fileReader);
                    JSONArray jsonCriteriasArray = (JSONArray) jsonObject.get("criterias");
                    for(int i = 0; i < jsonCriteriasArray.size(); i++) {
                        JSONObject jsonObject1 = ParserForSearch.getResultByCriteria((JSONObject) jsonCriteriasArray.get(i));


                    }

                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
                break;
            case STAT:

                break;
            default:

        }
    }

}

