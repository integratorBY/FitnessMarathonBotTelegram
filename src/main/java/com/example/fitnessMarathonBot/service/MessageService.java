package com.example.fitnessMarathonBot.service;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.fitnessDB.bean.*;
import com.example.fitnessMarathonBot.fitnessDB.repository.*;
import com.example.fitnessMarathonBot.fitnessDB.service.ListUserGoalsService;
import com.example.fitnessMarathonBot.fitnessDB.service.UserPhotoService;
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
    private ListUserGoalsService listUserGoalsService;

    @Autowired
    private UserProfileImpl userProfileRepo;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private UserPhotoRepository userPhotoRepository;

    @Autowired
    private UserPhotoWeigherRepo userPhotoWeigherRepo;

    @Autowired
    private UserPhotoService userPhotoService;

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

    public void newMondayNewPhotoUserReportWeigher() {
        List<User> userList = userRepository.findAll();

        String currentDate = CurrentDate.getCurrentDate();
        UserPhotoWeigher userPhotoWeigher = null;
        for (User user : userList) {
            if (userPhotoWeigherRepo.findUserPhotoWeigherByTimeStampAndUser(currentDate, user) == null) {
                userPhotoWeigher = UserPhotoWeigher.builder()
                        .user(user)
                        .timeStamp(currentDate)
                        .build();
                userPhotoWeigherRepo.save(userPhotoWeigher);
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

//    @SneakyThrows
//    public void remindDrinkWater() {
//        List<User> users = userRepository.findAll();
//        for (User user : users) {
//            if (user.getChatId() != 748582406) {
//                myBot.execute(new SendMessage(user.getChatId(), "Не забываем пить водичку!"));
//            }
//        }
//    }

    @SneakyThrows
    public void remindToCompleteTasks() {
        List<User> users = userRepository.findAll();
        UserPhoto userPhoto;
        ListUserGoals listUserGoals;
        String textRemind = "Внимание!!!\n\nВы сегодня: \n\n";
        for (User user : users) {
            if (user.getChatId() != 748582406) {
                userPhoto = userPhotoRepository.findUserPhotoByTimeStampAndUser(CurrentDate.getCurrentDate(), user);
                listUserGoals = userGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, CurrentDate.getCurrentDate());
                int quantityPhoto = 0;
                int quantityGoals = 0;
                if (userPhoto != null) {
                    quantityPhoto = userPhotoService.getCountPhotos(userPhoto);
                    if (quantityPhoto < 3) {
                        textRemind = textRemind.concat("Отправили фото " + userPhotoService.getCountPhotos(userPhoto) + " из 3");
                    }
                }
                if (listUserGoals != null) {
                    quantityGoals = listUserGoalsService.countDoneTasks(listUserGoals);
                    if (quantityGoals < 6) {
                        textRemind = textRemind.concat("\nВыполнили заданий " + listUserGoalsService.countDoneTasks(listUserGoals) + " из 6");
                    }
                }
                if (quantityGoals < 6 || quantityPhoto < 3) {
                    myBot.execute(new SendMessage(user.getChatId(), textRemind));
                }
            }
        }
    }

}
