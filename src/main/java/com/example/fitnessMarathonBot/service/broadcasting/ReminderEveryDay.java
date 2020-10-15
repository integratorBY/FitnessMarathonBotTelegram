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
public class ReminderEveryDay {

    @Autowired
    private MessageService messageService;

    private boolean isReminder = false;

    @SneakyThrows
    private void broadcast() {
        while (true) {
            log.info("reminder every day start");
            LocalTime now = LocalTime.now();
            Calendar calendar = Calendar.getInstance();
            Date date = new Date();
            calendar.setTime(date);
            long timeUntilReminder = 0;
            int nowDayWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (nowDayWeek != 6 && nowDayWeek != 1) {
                if (now.getHour() < 22) {
                    long nowHour = (21 - now.getHour()) * 3600000;
                    long nowMinute = (59 - now.getMinute()) * 60000;
                    int nowSecond = (60 - now.getSecond()) * 1000;
                    timeUntilReminder = nowHour + nowMinute + nowSecond;
                    log.info("reminder every day is sleeping");
                    Thread.sleep(timeUntilReminder);
                    messageService.remindToCompleteTasks();
                    Thread.sleep(3600000 * 2);
                }
            } else {
                messageService.remindDrinkWater();
                Thread.sleep(3600000 * 2);
            }
        }

    }

    public void startReminder() {
        if (!isReminder) {
            isReminder = true;
            log.info("START REMINDER EVERY DAY");
            broadcast();
        }
    }
}
