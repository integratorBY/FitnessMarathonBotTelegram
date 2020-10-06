package com.example.fitnessMarathonBot.appconfig;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.admin.telegramAdminFacade.TelegramAdminFacade;
import com.example.fitnessMarathonBot.botapi.client.teleframUserFacade.TelegramUserFacade;
import com.example.fitnessMarathonBot.service.BroadcastService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;


@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Autowired
    BroadcastService broadcastService;

    @Bean
    public Bot myBot(TelegramUserFacade telegramUserFacade, TelegramAdminFacade telegramAdminFacade) throws TelegramApiRequestException {
        Bot myBot = new Bot(telegramUserFacade, telegramAdminFacade);
        myBot.setBotUserName(botUserName);
        myBot.setBotToken(botToken);
        myBot.setWebHookPath(webHookPath);
        new Thread(broadcastService::startBroadcasting).start();
        return myBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
