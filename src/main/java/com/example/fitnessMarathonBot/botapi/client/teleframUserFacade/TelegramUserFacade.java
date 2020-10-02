package com.example.fitnessMarathonBot.botapi.client.teleframUserFacade;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.bean.UserProfileData;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.BotStateContext;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.service.UserPhotoService;
import com.example.fitnessMarathonBot.service.LocaleMessageService;
import com.example.fitnessMarathonBot.service.UserMainMenuService;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author get inspired by Sergei Viacheslaev's video
 */
@Component
@Slf4j
@Getter
@Setter
public class TelegramUserFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private UserMainMenuService userMainMenuService;
    private Bot myBot;
    private ReplyMessagesService messagesService;

    @Autowired
    private UserPhotoService userPhotoService;

    public TelegramUserFacade(BotStateContext botStateContext, UserDataCache userDataCache, UserMainMenuService userMainMenuService,
                              @Lazy Bot myBot, ReplyMessagesService messagesService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.userMainMenuService = userMainMenuService;
        this.myBot = myBot;
        this.messagesService = messagesService;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }


        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
                    message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        } else if (message != null && message.hasPhoto()) {
            long chatId = update.getMessage().getChatId();
            List<PhotoSize> photos = update.getMessage().getPhoto();
            String photo_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                    .orElse(null)).getFileId();
            log.info("New photo from User:{}, userId: {}, chatId: {},  photo_id: {}",
                    message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), photo_id);
            userPhotoService.saveUserPhoto(message);
            replyMessage = new SendMessage(chatId, "Фото принято!");
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start":
                botState = BotState.ASK_START;
                break;
            case "Ввод антропометрических данных":
                botState = BotState.ASK_PERSONAL_INFO;
                break;
            case "Задание":
                botState = BotState.TASK;
                break;
            case "Отчёт":
                botState = BotState.REPORT_OF_THE_DAY;
                break;
            case "Моя информация":
                botState = BotState.MY_INFORMATION;
                break;
            case "Ссылка на чат":
                botState = BotState.LINK_TO_CHAT;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);
        return replyMessage;
    }


    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int userId = buttonQuery.getFrom().getId();
        LocaleMessageService localeMessageService;
        BotApiMethod<?> callBackAnswer = userMainMenuService.getUserMainMenuMessage(chatId);

        if (buttonQuery.getData().equals("buttonInputPersonalInfo")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askName"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);
        } else if (buttonQuery.getData().equals("buttonPersonalInfo")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askNeck"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ARM);
        } else if (buttonQuery.getData().equals("buttonReportPhoto")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askPhoto"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PHOTO);
        } else if(buttonQuery.getData().equals("buttonReportGoals")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askGoals"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_GOALS);
        }

        return callBackAnswer;


    }


    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }

    @SneakyThrows
    public File getUsersProfile(int userId) {
        UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
        File profileFile = ResourceUtils.getFile("classpath:static/docs/Your_order.TXT");

        try (FileWriter fw = new FileWriter(profileFile.getAbsoluteFile());
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(userProfileData.toString());
        }


        return profileFile;

    }

}