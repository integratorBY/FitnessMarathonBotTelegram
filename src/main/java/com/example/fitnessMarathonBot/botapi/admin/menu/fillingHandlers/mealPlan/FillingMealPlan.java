package com.example.fitnessMarathonBot.botapi.admin.menu.fillingHandlers.mealPlan;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.botapi.admin.adminButtonHandler.AdminButtonHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.MealPlan;
import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserProfile;
import com.example.fitnessMarathonBot.fitnessDB.repository.MealPlanRepository;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserProfileImpl;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import com.example.fitnessMarathonBot.fitnessDB.service.MealPlanService;
import com.example.fitnessMarathonBot.regex.RegexHandler;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class FillingMealPlan implements InputMessageHandler {
    private UserDataCache userDataCache;
    private Bot myBot;
    @Autowired
    private MealPlanService mealPlanService;

    @Autowired
    private ReplyMessagesService messagesService;

    private static UserProfile userProfile;

    public FillingMealPlan(UserDataCache userDataCache,
                           @Lazy Bot myBot) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.myBot = myBot;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.ASK_ADMIN_MEAL_PLAN)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_ADMIN_NUMBER_FOR_PLAN1);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_ADMIN_MEAL_PLAN;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        Date dateObj = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = formatForDateNow.format(dateObj);

        SendMessage replyToUser = null;
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        if (botState.equals(BotState.ASK_ADMIN_NUMBER_FOR_PLAN1)) {
            if (RegexHandler.checkUserAnswerOnDigit(usersAnswer)) {
                if (Integer.parseInt(usersAnswer) <= 31){
                    mealPlanService.setCountDayCategoryOne(Integer.parseInt(usersAnswer));
                    replyToUser = new SendMessage(chatId, "Загрузите план");
                    userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_LOAD_MEAL_PLAN1);
                }
            } else {
                replyToUser = new SendMessage(chatId, "Введите номер дня, на который хотите загрузить план!");
            }
        }
        if (botState.equals(BotState.ASK_ADMIN_NUMBER_FOR_PLAN2)) {
            if (RegexHandler.checkUserAnswerOnDigit(usersAnswer)) {
                if (Integer.parseInt(usersAnswer) <= 31){
                    mealPlanService.setCountDayCategoryTwo(Integer.parseInt(usersAnswer));
                    replyToUser = new SendMessage(chatId, "Загрузите план");
                    userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_LOAD_MEAL_PLAN2);
                }
            } else {
                replyToUser = new SendMessage(chatId, "Введите номер дня, на который хотите загрузить план!");
            }
        }
        if (botState.equals(BotState.ASK_ADMIN_NUMBER_FOR_PLAN3)) {
            if (RegexHandler.checkUserAnswerOnDigit(usersAnswer)) {
                if (Integer.parseInt(usersAnswer) <= 31){
                    mealPlanService.setCountDayCategoryThree(Integer.parseInt(usersAnswer));
                    replyToUser = new SendMessage(chatId, "Загрузите план");
                    userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_LOAD_MEAL_PLAN3);
                }
            } else {
                replyToUser = new SendMessage(chatId, "Введите номер дня, на который хотите загрузить план!");
            }
        }

        return replyToUser;
    }

}
