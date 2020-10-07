package com.example.fitnessMarathonBot.fitnessDB.service;

import com.example.fitnessMarathonBot.fitnessDB.bean.ListUserGoals;
import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.repository.ListUserGoalsRepository;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ListUserGoalsService {

    @Autowired
    private ListUserGoalsRepository listUserGoalsRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    public boolean markTargetOne(long chatId) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = dateFormat.format(date);
        if (userRepository.findUserByChatId(chatId) != null) {
            User user = userRepository.findUserByChatId(chatId);
            if (listUserGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, currentDate) != null) {
                ListUserGoals listUserGoals = listUserGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, currentDate);
                if (listUserGoals.isTaskOne()) {
                    return true;
                } else {
                    listUserGoals.setTaskOne(true);
                    listUserGoals.setTimeStamp(currentDate);
                    listUserGoalsRepository.save(listUserGoals);
                }
            } else {
                ListUserGoals listUserGoals = ListUserGoals.builder()
                        .user(user)
                        .taskOne(true)
                        .timeStamp(currentDate)
                        .build();
                listUserGoalsRepository.save(listUserGoals);
            }
        }
        return false;
    }

    public boolean markTargetTwo(long chatId) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = dateFormat.format(date);
        if (userRepository.findUserByChatId(chatId) != null) {
            User user = userRepository.findUserByChatId(chatId);
            if (listUserGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, currentDate) != null) {
                ListUserGoals listUserGoals = listUserGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, currentDate);
                if (listUserGoals.isTaskTwo()) {
                    return true;
                } else {
                    listUserGoals.setTaskTwo(true);
                    listUserGoals.setTimeStamp(currentDate);
                    listUserGoalsRepository.save(listUserGoals);
                }
            } else {
                ListUserGoals listUserGoals = ListUserGoals.builder()
                        .user(user)
                        .taskTwo(true)
                        .timeStamp(currentDate)
                        .build();
                listUserGoalsRepository.save(listUserGoals);
            }
        }
        return false;
    }

    public boolean markTargetThree(long chatId) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = dateFormat.format(date);
        if (userRepository.findUserByChatId(chatId) != null) {
            User user = userRepository.findUserByChatId(chatId);
            if (listUserGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, currentDate) != null) {
                ListUserGoals listUserGoals = listUserGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, currentDate);
                if (listUserGoals.isTaskThree()) {
                    return true;
                } else {
                    listUserGoals.setTaskThree(true);
                    listUserGoals.setTimeStamp(currentDate);
                    listUserGoalsRepository.save(listUserGoals);
                }
            } else {
                ListUserGoals listUserGoals = ListUserGoals.builder()
                        .user(user)
                        .taskThree(true)
                        .timeStamp(currentDate)
                        .build();
                listUserGoalsRepository.save(listUserGoals);
            }
        }
        return false;
    }

    public boolean markTargetFour(long chatId) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = dateFormat.format(date);
        if (userRepository.findUserByChatId(chatId) != null) {
            User user = userRepository.findUserByChatId(chatId);
            if (listUserGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, currentDate) != null) {
                ListUserGoals listUserGoals = listUserGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, currentDate);
                if (listUserGoals.isTaskFour()) {
                    return true;
                } else {
                    listUserGoals.setTaskFour(true);
                    listUserGoals.setTimeStamp(currentDate);
                    listUserGoalsRepository.save(listUserGoals);
                }
            } else {
                ListUserGoals listUserGoals = ListUserGoals.builder()
                        .user(user)
                        .taskFour(true)
                        .timeStamp(currentDate)
                        .build();
                listUserGoalsRepository.save(listUserGoals);
            }
        }
        return false;
    }

    public boolean markTargetFive(long chatId) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = dateFormat.format(date);
        if (userRepository.findUserByChatId(chatId) != null) {
            User user = userRepository.findUserByChatId(chatId);
            if (listUserGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, currentDate) != null) {
                ListUserGoals listUserGoals = listUserGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, currentDate);
                if (listUserGoals.isTaskFive()) {
                    return true;
                } else {
                    listUserGoals.setTaskFive(true);
                    listUserGoals.setTimeStamp(currentDate);
                    listUserGoalsRepository.save(listUserGoals);
                }
            } else {
                ListUserGoals listUserGoals = ListUserGoals.builder()
                        .user(user)
                        .taskFive(true)
                        .timeStamp(currentDate)
                        .build();
                listUserGoalsRepository.save(listUserGoals);
            }
        }
        return false;
    }

    public boolean markTargetSix(long chatId) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = dateFormat.format(date);
        if (userRepository.findUserByChatId(chatId) != null) {
            User user = userRepository.findUserByChatId(chatId);
            if (listUserGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, currentDate) != null) {
                ListUserGoals listUserGoals = listUserGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, currentDate);
                if (listUserGoals.isTaskSix()) {
                    return true;
                } else {
                    listUserGoals.setTaskSix(true);
                    listUserGoals.setTimeStamp(currentDate);
                    listUserGoalsRepository.save(listUserGoals);
                }
            } else {
                ListUserGoals listUserGoals = ListUserGoals.builder()
                        .user(user)
                        .taskSix(true)
                        .timeStamp(currentDate)
                        .build();
                listUserGoalsRepository.save(listUserGoals);
            }
        }
        return false;
    }
}
