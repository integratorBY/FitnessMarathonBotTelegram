package com.example.fitnessMarathonBot.fitnessDB.service;

import com.example.fitnessMarathonBot.fitnessDB.bean.ListGoals;
import com.example.fitnessMarathonBot.fitnessDB.repository.ListGoalsRepository;
import com.example.fitnessMarathonBot.service.CurrentDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListGoalsService {
    @Autowired
    private ListGoalsRepository listUserGoals;

    @Autowired
    private ListGoalsRepository listGoalsRepository;

    public int countGoalsToday() {
        int count = 0;
        ListGoals listGoals = listUserGoals.findListGoalsByTimeStamp(CurrentDate.getCurrentDate());
        if (listGoals != null) {
            if (listGoals.getTaskOne() != null && !listGoals.getTaskOne().equals("")) {
                count++;
            }
            if (listGoals.getTaskTwo() != null && !listGoals.getTaskTwo().equals("")) {
                count++;
            }
            if (listGoals.getTaskThree() != null && !listGoals.getTaskThree().equals("")) {
                count++;
            }
            if (listGoals.getTaskFour() != null && !listGoals.getTaskFour().equals("")) {
                count++;
            }
            if (listGoals.getTaskFive() != null && !listGoals.getTaskFive().equals("")) {
                count++;
            }
            if (listGoals.getTaskSix() != null && !listGoals.getTaskSix().equals("")) {
                count++;
            }
        }
        return count;
    }

    public void deleteTask(int numberTask) {
        ListGoals listGoals = listGoalsRepository.findListGoalsByTimeStamp(CurrentDate.getCurrentDate());
        if (numberTask == 1) {
            listGoals.setTaskOne("");
        }
        if (numberTask == 2) {
            listGoals.setTaskTwo("");
        }
        if (numberTask == 3) {
            listGoals.setTaskThree("");
        }
        if (numberTask == 4) {
            listGoals.setTaskFour("");
        }
        if (numberTask == 5) {
            listGoals.setTaskFive("");
        }
        if (numberTask == 6) {
            listGoals.setTaskSix("");
        }
        listGoalsRepository.save(listGoals);
    }
}
