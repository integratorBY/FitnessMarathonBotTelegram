package com.example.fitnessMarathonBot.botapi.admin.telegramAdminFacade;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.BotStateContext;
import com.example.fitnessMarathonBot.botapi.admin.adminButtonHandler.AdminButtonHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.service.MealPlanService;
import com.example.fitnessMarathonBot.service.AdminMainMenuService;
import com.example.fitnessMarathonBot.service.LocaleMessageService;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
@Getter
@Setter
public class TelegramAdminFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private AdminMainMenuService adminMainMenuService;
    private Bot myBot;
    private ReplyMessagesService messagesService;

    @Autowired
    private MealPlanService mealPlanService;

    @Autowired
    private AdminButtonHandler adminButtonHandler;

    public TelegramAdminFacade(BotStateContext botStateContext, UserDataCache userDataCache, AdminMainMenuService adminMainMenuService,
                               @Lazy Bot myBot, ReplyMessagesService messagesService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.adminMainMenuService = adminMainMenuService;
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
        } else if (message != null && message.hasPhoto() && userDataCache.getUsersCurrentBotState(
                message.getFrom().getId()).equals(BotState.ASK_ADMIN_LOAD_MEAL_PLAN1)) {
            replyMessage = saveMealPlanOne(message);
        } else if (message != null && message.hasPhoto() && userDataCache.getUsersCurrentBotState(
                message.getFrom().getId()).equals(BotState.ASK_ADMIN_LOAD_MEAL_PLAN2)) {
            replyMessage = saveMealPlanTwo(message);
        } else if (message != null && message.hasPhoto() && userDataCache.getUsersCurrentBotState(
                message.getFrom().getId()).equals(BotState.ASK_ADMIN_LOAD_MEAL_PLAN3)) {
            replyMessage = saveMealPlanThree(message);
        } else if (message != null && message.hasPhoto() && userDataCache.getUsersCurrentBotState(
                message.getFrom().getId()).equals(BotState.ASK_ADMIN_LOAD_MEAL_PLAN_BASKET)) {
            replyMessage = saveMealPlanFoodBasket(message);
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
            case "Клиенты":
                botState = BotState.VIEW_PARTICIPANTS_ADN_RESULTS;
                break;
            case "Начать марафон":
                botState = BotState.START_NEW_MARATHON;
                break;
            case "Задания":
                botState = BotState.REFRESH_GOALS;
                break;
            case "План питания":
                botState = BotState.MEAL_PLAN;
                break;
            case "Недельный отчёт":
                botState = BotState.WEEKLY_REPORT;
                break;
            case "Отправить сообщение всем":
                botState = BotState.SEND_MESSAGE_ALL;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);
        return replyMessage;
    }

    @SneakyThrows
    private SendMessage saveMealPlanOne(Message message) {
        mealPlanService.saveMealPlanOneCategory(message);
        myBot.execute(new SendMessage(message.getChatId(), "План добавлен!"));
        return adminButtonHandler.getButtonsOperationsWithMealPlanAndMessage(message.getChatId());
    }

    @SneakyThrows
    private SendMessage saveMealPlanTwo(Message message) {
        mealPlanService.saveMealPlanTwoCategory(message);
        myBot.execute(new SendMessage(message.getChatId(), "План добавлен!"));
        return adminButtonHandler.getButtonsOperationsWithMealPlanAndMessage(message.getChatId());
    }

    @SneakyThrows
    private SendMessage saveMealPlanThree(Message message) {
        mealPlanService.saveMealPlanThreeCategory(message);
        myBot.execute(new SendMessage(message.getChatId(), "План добавлен!"));
        return adminButtonHandler.getButtonsOperationsWithMealPlanAndMessage(message.getChatId());
    }

    @SneakyThrows
    private SendMessage saveMealPlanFoodBasket(Message message) {
        mealPlanService.saveMealPlanFoodBasket(message);
        myBot.execute(new SendMessage(message.getChatId(), "План добавлен!"));
        return adminButtonHandler.getButtonsOperationsWithMealPlanAndMessage(message.getChatId());
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int userId = buttonQuery.getFrom().getId();
        LocaleMessageService localeMessageService;

        BotApiMethod<?> callBackAnswer = adminMainMenuService.getAdminMainMenuMessage(chatId, "");

        if (buttonQuery.getData().equals("buttonAddGoal")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTimeStampForTask"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_TASK_ONE);

        } else if (buttonQuery.getData().equals("buttonEditGoal")) {
            callBackAnswer = adminButtonHandler.getMessageAndEditGoalButtons(chatId);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_NUMBER_GOAL);

        } else if (buttonQuery.getData().equals("buttonDelGoal")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminDeleteTask"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);

        } else if (buttonQuery.getData().equals("buttonEditTimeStamp")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTimeStampForTask"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_EDIT_TIMESTAMP);
        } else if (buttonQuery.getData().equals("buttonEditTaskOne")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskOne"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_EDIT_TASK_ONE);
        } else if (buttonQuery.getData().equals("buttonEditTaskTwo")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskTwo"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_EDIT_TASK_TWO);
        } else if (buttonQuery.getData().equals("buttonEditTaskThree")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskThree"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_EDIT_TASK_THREE);
        } else if (buttonQuery.getData().equals("buttonEditTaskFour")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskFour"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_EDIT_TASK_FOUR);
        } else if (buttonQuery.getData().equals("buttonEditTaskFive")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskFive"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_EDIT_TASK_FIVE);
        } else if (buttonQuery.getData().equals("buttonEditTaskSix")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskSix"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_EDIT_TASK_SIX);

        } else if (buttonQuery.getData().equals("buttonAddMealPlan")) {
            callBackAnswer = adminButtonHandler.getButtonsOperationsWithMealPlanAndMessage(chatId);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_ADD_MEAL_PLAN);

        } else if (buttonQuery.getData().equals("buttonOneCategory")) {
            callBackAnswer = new SendMessage(chatId, "Введите норме дня: ");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_NUMBER_FOR_PLAN1);

        }else if (buttonQuery.getData().equals("buttonTwoCategory")) {
            callBackAnswer = new SendMessage(chatId, "Введите норме дня: ");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_NUMBER_FOR_PLAN2);

        } else if (buttonQuery.getData().equals("buttonThreeCategory")) {
            callBackAnswer = new SendMessage(chatId, "Введите норме дня: ");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_NUMBER_FOR_PLAN3);

        } else if (buttonQuery.getData().equals("buttonFoodBasket")) {
            callBackAnswer = new SendMessage(chatId, "Введите дни через тире (Пример 1-3)");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_DAYS_FOR_FOOD_BASKET);
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

}
