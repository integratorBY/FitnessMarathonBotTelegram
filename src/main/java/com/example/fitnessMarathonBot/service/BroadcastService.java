package com.example.fitnessMarathonBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class BroadcastService {

    @Autowired
    private MessageService messageService;

    private boolean isBroadcasting = false;

    private void broadcast() {
        while (true) {
            System.out.println("\"while true cycling\" = " + "while true cycling"); // TODO: 14/07/20 replace with logs
            LocalTime now = LocalTime.now();
            if (now.isAfter(LocalTime.of(00, 00)) && now.isBefore(LocalTime.of(04, 00))) {
                messageService.updateDateInDB();

            }try {
//                System.out.println("\"broadcast is sleeping\" = " + "broadcast is sleeping");
                Thread.sleep(28800000 / 8);
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