package com.example.fitnessMarathonBot;

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
