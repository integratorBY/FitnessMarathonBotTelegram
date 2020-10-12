package com.example.fitnessMarathonBot.bean;

import com.example.fitnessMarathonBot.botapi.admin.telegramAdminFacade.TelegramAdminFacade;
import com.example.fitnessMarathonBot.botapi.client.teleframUserFacade.TelegramUserFacade;
import com.example.fitnessMarathonBot.service.broadcasting.BroadcastService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;


@Getter
@Setter
public class Bot extends TelegramLongPollingBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;

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

    @Override
    public void onUpdateReceived(Update update) {
        int userId = 0;
        if (update.getCallbackQuery() == null) {
            userId = update.getMessage().getFrom().getId();
        } else if (update.getMessage() == null) {
            userId = update.getCallbackQuery().getFrom().getId();
        }

        if (userId == 1331718111) {
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

//    @Override
//    public String getBotPath() {
//        return webHookPath;
//    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    @SneakyThrows
    public void sendPhoto(long chatId, String imageCaption, String imagePath) {
        URL url = ResourceUtils.getURL("classpath:static/images/" + imagePath + ".JPG");
        File image = ResourceUtils.getFile(url);
        SendPhoto sendPhoto = new SendPhoto().setPhoto(image);
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(imageCaption);
        execute(sendPhoto);
    }

    @SneakyThrows
    public void sendAllStartingPhoto(long chatId, LinkedHashMap<String, String> photoInfo) {

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
        SendPhoto sendPhoto = new SendPhoto();
        URL url = null;
        File image = null;
        for (SendMessage message : sendMessageList) {
            execute(message);
            Thread.sleep( 1000);
        }
    }


    @SneakyThrows
    public void sendClientMealPlan(long chatId) {
        SendMessage clientMealPlan = new SendMessage();
        clientMealPlan.setChatId(chatId);
        clientMealPlan.setText("План питания для вас на три дня: \n\n" +
                "Кушай кашу на обед укрепляй иммунитет!");
        Thread.sleep(3000);
        execute(clientMealPlan);
    }

    @SneakyThrows
    public void sendMessageAllParticipantMarathon(List<SendMessage> sendMessageList) {
        for (SendMessage message : sendMessageList) {
            execute(message);
            Thread.sleep(10000);
        }
    }
}
