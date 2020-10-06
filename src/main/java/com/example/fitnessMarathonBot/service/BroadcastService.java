package com.example.fitnessMarathonBot.service;

import com.example.fitnessMarathonBot.bean.Bot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BroadcastService {

    private Bot myBot;

    private boolean isBroadcasting = false;

    private void broadcast() {
        while (true) {
            System.out.println("\"while true cycling\" = " + "while true cycling"); // TODO: 14/07/20 replace with logs
            LocalTime now = LocalTime.now();
            if (now.isAfter(LocalTime.of(18, 15)) && now.isBefore(LocalTime.of(18, 20))) {
//                try {
//                    myBot.execute(new SendMessage(1331718111L, "Таймер сработал на ура!!!!"));
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
                System.out.println("----------------11111------------------");
            }
            try {
                System.out.println("\"broadcast is sleeping\" = " + "broadcast is sleeping");
                Thread.sleep(200000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void startBroadcasting() {
        if (!isBroadcasting) {
            isBroadcasting = true;
            System.out.println("\"start broadcasting\" = " + "start broadcasting");
            broadcast();
        }
    }


}