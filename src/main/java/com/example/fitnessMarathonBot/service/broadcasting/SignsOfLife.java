package com.example.fitnessMarathonBot.service.broadcasting;

import com.example.fitnessMarathonBot.service.MessageService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SignsOfLife {
    @Autowired
    private MessageService messageService;

    private boolean isReminder = false;

    @SneakyThrows
    private void broadcast() {
        while (true) {

            messageService.sendSignOfLife();
            Thread.sleep(3600000);
        }
    }

    public void startReminder() {
        if (!isReminder) {
            isReminder = true;
            log.info("START REMINDER MONDAY");
            broadcast();
        }
    }
}
