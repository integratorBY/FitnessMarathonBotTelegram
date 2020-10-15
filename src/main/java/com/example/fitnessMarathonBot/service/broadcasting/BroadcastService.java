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
public class BroadcastService {

    @Autowired
    private MessageService messageService;

    private boolean isBroadcasting = false;

    @SneakyThrows
    private void broadcast() {
        while (true) {
            log.info("while true cycling");
            LocalTime now = LocalTime.now();
            Calendar calendar = Calendar.getInstance();
            Date date = new Date();
            calendar.setTime(date);
            long nowHour = (23 - now.getHour()) * 3600000;
            long nowMinute = (59 - now.getMinute()) * 60000;
            int nowSecond = (60 - now.getSecond()) * 1000;
            long timeToUpdate = nowHour + nowMinute + nowSecond;
            int nowDayWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (nowDayWeek != 6 && nowDayWeek != 1) {
                System.out.println(timeToUpdate);
                Thread.sleep(timeToUpdate);
                messageService.updateDateInDB();
                log.info("updateDateInDB() - WORKED");
                messageService.newDayNewListUserGoals();
                log.info(" newDayNewListUserGoals()  - WORKED");
                messageService.newDayNewPhotoUserReport();
                log.info(" newDayNewPhotoUserReport() - WORKED");
                messageService.nexDayMarathon();
                log.info(" nexDayMarathon() - WORKED");
                if (nowDayWeek == 5) {
                    messageService.newMondayNewPhotoUserReportWeigher();
                    log.info(" newMondayNewPhotoUserReportWeigher() - WORKED");
                }
            } else {
                System.out.println(timeToUpdate);
                Thread.sleep(timeToUpdate);
                messageService.nexDayMarathon();
                log.info("messageService.nexDayMarathon() - WORKED");
                Thread.sleep(28800000 / 2);
            }
        }
    }

    public void startBroadcasting() {
        if (!isBroadcasting) {
            isBroadcasting = true;
            log.info("start broadcasting");
            broadcast();
        }
    }


}