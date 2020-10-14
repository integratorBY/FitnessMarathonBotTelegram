package com.example.fitnessMarathonBot;

import java.io.FileWriter;
import java.io.IOException;

public class MyFileWriter {

    public static void writeToFile(String text){
        try(FileWriter writer = new FileWriter("report.txt", false))
        {
            writer.write(text);
            writer.append('\n');
            writer.append('E');

            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
}
