package com.example.fitnessMarathonBot.botapi.client.teleframUserFacade;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.bean.UserProfileData;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.BotStateContext;
import com.example.fitnessMarathonBot.botapi.client.userButtonHandlers.UserButtonHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.service.ListUserGoalsService;
import com.example.fitnessMarathonBot.fitnessDB.service.UserPhotoService;
import com.example.fitnessMarathonBot.fitnessDB.service.UserProfileService;
import com.example.fitnessMarathonBot.service.LocaleMessageService;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import com.example.fitnessMarathonBot.service.UserMainMenuService;
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
    private UserButtonHandler userButtonHandler;

    @Autowired
    private UserPhotoService userPhotoService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private ListUserGoalsService listUserGoalsService;

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
            List<PhotoSize> photos = update.getMessage().getPhoto();
            String photo_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                    .orElse(null)).getFileId();
            log.info("New photo from User:{}, userId: {}, chatId: {},  photo_id: {}",
                    message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), photo_id);
            if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.ASK_START_PHOTO)) {
                replyMessage = counterOfSendStartPhotosBody(message);
            } else if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.ASK_START_PHOTO_WEIGHER)) {
                replyMessage = counterOfSendStartPhotosWeigher(message);
            } else {
                replyMessage = counterOfSentPhotos(message);
//                    counterOfSentPhotos(message);
            }

        }

        return replyMessage;
    }

    private SendMessage counterOfSendStartPhotosBody(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        userProfileService.saveUserProfilesStartPhotoBody(message);
        return new SendMessage(message.getChatId(), "Фото принято");
    }

    private SendMessage counterOfSendStartPhotosWeigher(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        userProfileService.saveUserProfilesStartPhotoWeigher(message);
        return new SendMessage(message.getChatId(), "Фото принято");
    }

    private SendMessage counterOfSentPhotos(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        int count = userPhotoService.saveUserPhoto(message);
        if (count >= 2) {
            sendMessage.setText(messagesService.getReplyText("reply.allPhotoSent"));
        } else {
            sendMessage.setText("Фото принято!");
        }
        return sendMessage;
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
            case "План питания":
                botState = BotState.MEAL_USER_PLAN;
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

        } else if (buttonQuery.getData().equals("buttonEditPersonalInfo")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askNeck"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ARM);

        } else if (buttonQuery.getData().equals("buttonReportPhoto")) {
            int count = userPhotoService.getCountUserPhotos(chatId);

            if (count == 3) {
                callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.allPhotoSent"));
            } else if (count < 3) {
                callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askPhoto"));
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PHOTO);
            }
//            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_PHOTO);
        } else if (buttonQuery.getData().equals("buttonReportGoals")) {
            callBackAnswer = userButtonHandler.getMessageAndGoalsButton(chatId);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_GOALS);

        } else if (buttonQuery.getData().equals("buttonLoadPhotoBody")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askStartPhotoBody"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_START_PHOTO);

        } else if (buttonQuery.getData().equals("buttonLoadWeigher")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askStartPhotoWeigher"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_START_PHOTO_WEIGHER);

        } else if (buttonQuery.getData().equals("buttonTaskOne")) {
            if (listUserGoalsService.markTargetOne(chatId)) {
                callBackAnswer = sendAnswerCallbackQuery("Это задание уже отмеченно!", true, buttonQuery);
            } else {
                callBackAnswer = sendAnswerCallbackQuery("Успешно записано!", true, buttonQuery);
            }
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_TASK_ONE);

        } else if (buttonQuery.getData().equals("buttonTaskTwo")) {
            if (listUserGoalsService.markTargetTwo(chatId)) {
                callBackAnswer = sendAnswerCallbackQuery("Это задание уже отмеченно!", true, buttonQuery);
            } else {
                callBackAnswer = sendAnswerCallbackQuery("Успешно записано!", true, buttonQuery);
            }
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_TASK_TWO);

        } else if (buttonQuery.getData().equals("buttonTaskThree")) {
            if (listUserGoalsService.markTargetThree(chatId)) {
                callBackAnswer = sendAnswerCallbackQuery("Это задание уже отмеченно!", true, buttonQuery);
            } else {
                callBackAnswer = sendAnswerCallbackQuery("Успешно записано!", true, buttonQuery);
            }
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_TASK_THREE);

        } else if (buttonQuery.getData().equals("buttonTaskFour")) {
            if (listUserGoalsService.markTargetFour(chatId)) {
                callBackAnswer = sendAnswerCallbackQuery("Это задание уже отмеченно!", true, buttonQuery);
            } else {
                callBackAnswer = sendAnswerCallbackQuery("Успешно записано!", true, buttonQuery);
            }
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_TASK_FOUR);
        } else if (buttonQuery.getData().equals("buttonTaskFive")) {
            if (listUserGoalsService.markTargetFive(chatId)) {
                callBackAnswer = sendAnswerCallbackQuery("Это задание уже отмеченно!", true, buttonQuery);
            } else {
                callBackAnswer = sendAnswerCallbackQuery("Успешно записано!", true, buttonQuery);
            }
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_TASK_FIVE);
        } else if (buttonQuery.getData().equals("buttonTaskSix")) {
            if (listUserGoalsService.markTargetSix(chatId)) {
                callBackAnswer = sendAnswerCallbackQuery("Это задание уже отмеченно!", true, buttonQuery);
            } else {
                callBackAnswer = sendAnswerCallbackQuery("Успешно записано!", true, buttonQuery);
            }
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_TASK_SIX);
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