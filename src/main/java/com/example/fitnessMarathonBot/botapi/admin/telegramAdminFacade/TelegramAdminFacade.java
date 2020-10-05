package com.example.fitnessMarathonBot.botapi.admin.telegramAdminFacade;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.BotStateContext;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.service.AdminMainMenuService;
import com.example.fitnessMarathonBot.service.LocaleMessageService;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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


    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int userId = buttonQuery.getFrom().getId();
        LocaleMessageService localeMessageService;

        BotApiMethod<?> callBackAnswer = adminMainMenuService.getAdminMainMenuMessage(chatId, "");

        if (buttonQuery.getData().equals("buttonAddGoal")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTimeStampForTask"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_TASK_ONE);

        } else if (buttonQuery.getData().equals("buttonEditGoal")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminEditTask"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);

        } else if (buttonQuery.getData().equals("buttonDelGoal")) {
            callBackAnswer = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminDeleteTask"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);

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
