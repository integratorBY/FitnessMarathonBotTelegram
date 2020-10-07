package com.example.fitnessMarathonBot.service;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.fitnessDB.bean.ListGoals;
import com.example.fitnessMarathonBot.fitnessDB.repository.ListGoalsRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
public class MessageService {

    static int count = 0;
    @Autowired
    private ListGoalsRepository listGoalsRepo;

    private Bot myBot;

    public MessageService(@Lazy Bot myBot) {
        this.myBot = myBot;
    }

    @SneakyThrows
    void sendMessageTimer() {
        myBot.execute(new SendMessage((long) 1331718111, "Таймер сработал на ура!!!!"));
    }

    @SneakyThrows
    public void updateDateInDB() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = formatter.format(date);
        List<ListGoals> goals = listGoalsRepo.findAll();
        ListGoals listGoals = listGoalsRepo.findListGoalsByTimeStamp(currentDate);
        if (listGoals != null && goals.size() > 1) {
            listGoalsRepo.deleteAll();
            listGoalsRepo.save(listGoals);
        } else {
            listGoals = goals.get(0);
            Date date2 = formatter.parse(listGoals.getTimeStamp());
            if (date.compareTo(date2) > 0) {
                listGoals.setTimeStamp(currentDate);
                listGoalsRepo.save(listGoals);
            }
        }
    }

}
