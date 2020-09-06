package com.alexplot.getdatafrombase;

import com.alexplot.getdatafrombase.Enums.Commands;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class MainClass {

    public static void main(String[] args) {

        Commands command = Commands.fromValue(args[0]);
        String inFileName;
        String outFileName;
        inFileName = args[1];
        outFileName = args[2];
        JSONParser parser = new JSONParser();
        JSONObject jsonObjectForWriteInFile;

        switch (command) {
            case SEARCH:
                try {
                    FileReader fileReader = new FileReader(inFileName);
                    JSONObject jsonObject = (JSONObject) parser.parse(fileReader);

                    jsonObjectForWriteInFile = ParserForSearch.getResult(jsonObject);

                    FileWriter fileWriter = new FileWriter(outFileName);
                    fileWriter.write(jsonObjectForWriteInFile.toJSONString());
                    fileWriter.flush();
                    fileWriter.close();

                } catch (ParseException e) {
                    e.printStackTrace();

                    JSONObject errorObject = new JSONObject();
                    errorObject.put("Type", "Error");
                    errorObject.put("Message", "Input file contains incorrect data format");
                    try {
                        FileWriter fileWriter = new FileWriter(outFileName);
                        fileWriter.write(errorObject.toJSONString());
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException ioE) {
                        ioE.printStackTrace();
                    }
                } catch (IOException ioE) {
                    ioE.printStackTrace();
                }
                System.out.println("Command SEARCH completed successfully");
                break;
            case STAT:
                try {
                    FileReader fileReaderStat = new FileReader(inFileName);
                    JSONObject jsonObjectStat = (JSONObject) parser.parse(fileReaderStat);
                    jsonObjectForWriteInFile = ParserForStat.getResult(jsonObjectStat);

                    FileWriter fileWriter = new FileWriter(outFileName);
                    fileWriter.write(jsonObjectForWriteInFile.toJSONString());
                    fileWriter.flush();
                    fileWriter.close();


                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
                System.out.println("Command STAT completed successfully");
                break;

            default:
                System.out.println("Unknown command");
                JSONObject jsonObjectError = new JSONObject();
                jsonObjectError.put("type", "Error");
                jsonObjectError.put("Message", "Unknown command");
                try {
                    FileWriter fileWriter = new FileWriter(outFileName);
                    fileWriter.write(jsonObjectError.toJSONString());
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
    }

}

