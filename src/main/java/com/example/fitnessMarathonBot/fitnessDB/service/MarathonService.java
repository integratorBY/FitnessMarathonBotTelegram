package com.example.fitnessMarathonBot.fitnessDB.service;

import com.example.fitnessMarathonBot.fitnessDB.bean.MarathonPeriod;
import com.example.fitnessMarathonBot.fitnessDB.repository.MarathonRepository;
import com.example.fitnessMarathonBot.service.CurrentDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class MarathonService {
    @Autowired
    private MarathonRepository marathonRepository;

    public String saveMarathon(int month) {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String finishMarathon = date.plusMonths(month).format(formatter);
        MarathonPeriod marathonPeriod = MarathonPeriod.builder()
                .startMarathonDate(CurrentDate.getCurrentDate())
                .finishMarathonDate(finishMarathon)
                .build();
        marathonRepository.save(marathonPeriod);
        return finishMarathon;
    }
}
