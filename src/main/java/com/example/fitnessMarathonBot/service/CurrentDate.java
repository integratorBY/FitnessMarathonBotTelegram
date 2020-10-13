package com.example.fitnessMarathonBot.service;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentDate {

    public static String getCurrentDate(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }
}
