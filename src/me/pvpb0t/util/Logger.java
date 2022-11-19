package me.pvpb0t.util;

import me.pvpb0t.EngineCore;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.FileHandler;

public class Logger {


    //private static List<String> messages = new ArrayList<>();
    private static List<String> messages = new ArrayList<>();


    public static void print(String message){
        messages.add(message);
        System.out.println("[" + EngineCore.GameTitle+"] - " + message);
    }

    public static void writeToLog(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-ddHH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            new File("logs").mkdir();
            String filename =  "logs"+ File.separator +dtf.format(now)+".txt";
            File myObj = new File(filename);

            FileWriter myWriter = new FileWriter(filename);
            for(String string : messages){
                myWriter.write(string+ "\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }



}
