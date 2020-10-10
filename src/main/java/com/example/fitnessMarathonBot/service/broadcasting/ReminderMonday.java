package com.example.fitnessMarathonBot.service.broadcasting;

import com.example.fitnessMarathonBot.service.MessageService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

@Service
@Slf4j
public class ReminderMonday {

    @Autowired
    private MessageService messageService;

    private boolean isReminder = false;

    @SneakyThrows
    private void broadcast() {
        while (true) {
            log.info("reminder while true cycling");
            LocalTime now = LocalTime.now();
            System.out.println(now.getHour() + ":" + now.getMinute());
            Calendar calendar = Calendar.getInstance();
            Date date = new Date();
            calendar.setTime(date);
            long timeUntilReminder = 0;
            int nowDayWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (nowDayWeek != 7) {
                log.info("reminder is sleeping");
                Thread.sleep(43200000);
            } else {
                if (now.getHour() < 6) {
                    long nowHour = (5 - now.getHour()) * 3600000;
                    long nowMinute = (59 - now.getMinute()) * 60000;
                    int nowSecond = (59 - now.getSecond()) * 1000;
                    timeUntilReminder = nowHour + nowMinute + nowSecond;
                    System.out.println(timeUntilReminder);
                    Thread.sleep(timeUntilReminder);
                    messageService.remindSendPhotoInMonday();
                    Thread.sleep(86400000);
                }
            }
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
