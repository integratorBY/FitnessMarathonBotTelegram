package com.example.fitnessMarathonBot.service.broadcasting;

import com.example.fitnessMarathonBot.service.MessageService;
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

    private void broadcast() {
        while (true) {
            log.info("while true cycling");
            LocalTime now = LocalTime.now();
            Calendar calendar = Calendar.getInstance();
            Date date = new Date();
            calendar.setTime(date);
            int nowDayWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (now.isAfter(LocalTime.of(00, 00)) && now.isBefore(LocalTime.of(04, 00))) {
                if (nowDayWeek != 6 && nowDayWeek != 1) {
                    messageService.updateDateInDB();
                    messageService.newDayNewListUserGoals();
                    messageService.newDayNewPhotoUserReport();
                }
//                messageService.nexDayMarathon();
            }try {
                log.info("broadcast is sleeping");
                Thread.sleep(28800000 / 8);
            } catch (InterruptedException e) {
                e.printStackTrace();
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