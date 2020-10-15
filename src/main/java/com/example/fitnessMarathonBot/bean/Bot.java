package com.example.fitnessMarathonBot.bean;

import com.example.fitnessMarathonBot.botapi.admin.telegramAdminFacade.TelegramAdminFacade;
import com.example.fitnessMarathonBot.botapi.client.teleframUserFacade.TelegramUserFacade;
import com.example.fitnessMarathonBot.service.broadcasting.BroadcastService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;


@Getter
@Setter
public class Bot extends TelegramLongPollingBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    BroadcastService broadcastService;

    private TelegramUserFacade telegramUserFacade;
    private TelegramAdminFacade telegramAdminFacade;

    public Bot(TelegramUserFacade telegramUserFacade, TelegramAdminFacade telegramAdminFacade) {
        this.telegramAdminFacade = telegramAdminFacade;
        this.telegramUserFacade = telegramUserFacade;
    }
//
//    @Override
//    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
//
//    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        int userId = 0;
        if (update.getEditedMessage() != null) {
            userId = update.getEditedMessage().getFrom().getId();
        } else if (update.getCallbackQuery() == null) {
            userId = update.getMessage().getFrom().getId();
        } else if (update.getMessage() == null) {
            userId = update.getCallbackQuery().getFrom().getId();
        }

        if (userId == 1331718111 || userId == 748582406) {
            telegramAdminFacade.handleUpdate(update);
        } else {
            telegramUserFacade.handleUpdate(update);
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    @SneakyThrows
    public void sendPhoto(long chatId, String imageCaption, String imagePath) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(imagePath);
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(imageCaption);
        execute(sendPhoto);
    }

    @SneakyThrows
    public void sendDocument(long chatId, String caption, File sendFile) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setCaption(caption);
        sendDocument.setDocument(sendFile);
        execute(sendDocument);
    }

    @SneakyThrows
    public void sendPhoto(long chatId, String photoId) {
        SendPhoto send = new SendPhoto();
        send.setChatId(chatId);
        send.setPhoto(photoId);
        execute(send);
    }

    @SneakyThrows
    public void sendListMessages(List<SendMessage> sendMessageList) {
        for (SendMessage message : sendMessageList) {
            execute(message);
            Thread.sleep( 5000);
        }
    }

}
