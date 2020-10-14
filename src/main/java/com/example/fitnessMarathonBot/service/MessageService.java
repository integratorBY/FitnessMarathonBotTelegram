package com.example.fitnessMarathonBot.service;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.fitnessDB.bean.*;
import com.example.fitnessMarathonBot.fitnessDB.repository.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Component
public class MessageService {

    static int count = 0;

    @Autowired
    private ListUserGoalsRepository userGoalsRepository;

    @Autowired
    private UserProfileImpl userProfileRepo;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private UserPhotoRepository userPhotoRepository;

    @Autowired
    private ListGoalsRepository listGoalsRepo;

    private Bot myBot;

    public MessageService(@Lazy Bot myBot) {
        this.myBot = myBot;
    }

    @SneakyThrows
    public void sendSignOfLife() {
        myBot.execute(new SendMessage((long) 1331718111, "Я живой!!!!"));
    }

    @SneakyThrows
    public void updateDateInDB() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = formatter.format(date);
        List<ListGoals> goals = listGoalsRepo.findAll();
        ListGoals listGoals = listGoalsRepo.findListGoalsByTimeStamp(currentDate);
        if (goals.size() == 0 && listGoals == null) {
            return;
        }
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

    public void newDayNewListUserGoals() {
        List<User> userList = userRepository.findAll();
        String currentDate = CurrentDate.getCurrentDate();
        ListUserGoals listUserGoals = null;
        for (User user : userList) {
            if (userGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, currentDate) == null) {
                listUserGoals = ListUserGoals.builder()
                        .user(user)
                        .timeStamp(currentDate)
                        .build();
                userGoalsRepository.save(listUserGoals);
            }
        }
    }

    public void newDayNewPhotoUserReport() {
        List<User> userList = userRepository.findAll();

        String currentDate = CurrentDate.getCurrentDate();
        UserPhoto userPhoto = null;
        for (User user : userList) {
            if (userPhotoRepository.findUserPhotoByTimeStampAndUser(currentDate, user) == null) {
                userPhoto = UserPhoto.builder()
                        .user(user)
                        .imageCategory("eat")
                        .timeStamp(currentDate)
                        .build();
                userPhotoRepository.save(userPhoto);
            }
        }
    }

    public void nexDayMarathon() {
        LocalTime now = LocalTime.now();
        List<UserProfile> userProfiles = userProfileRepo.findAll();
        int day = 0;
        if (now.getHour() == 0 && now.getMinute() < 5) {
            for (UserProfile userProfile : userProfiles) {
                day = userProfile.getDaysOfTheMarathon();
                userProfile.setDaysOfTheMarathon(++day);
                userProfileRepo.save(userProfile);
            }
        }
    }

    @SneakyThrows
    public void remindSendPhotoInMonday() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getChatId() != 748582406) {
                myBot.execute(new SendMessage(user.getChatId(), "НАПОМИНАНИЕ!!! \n\nВ понедельник(тоесть сейчас) отправить фото весов на тощак!!!"));
            }
        }
    }

}
