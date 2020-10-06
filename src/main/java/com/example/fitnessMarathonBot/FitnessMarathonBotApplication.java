package com.example.fitnessMarathonBot;

import com.example.fitnessMarathonBot.service.BroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;


@SpringBootApplication
public class FitnessMarathonBotApplication {



    public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(FitnessMarathonBotApplication.class, args);
	}

}
