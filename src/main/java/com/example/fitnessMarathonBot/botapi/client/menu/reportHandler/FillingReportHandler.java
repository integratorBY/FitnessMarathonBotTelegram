package com.example.fitnessMarathonBot.botapi.client.menu.reportHandler;

import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class FillingReportHandler implements InputMessageHandler {
    private UserDataCache userDataCache;

    public FillingReportHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.ASK_REPORT)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.FILLING_REPORT);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_REPORT;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();
        SendMessage replyToUser = null;
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        if (botState.equals(BotState.ASK_TASK_ONE)) {
            replyToUser = new SendMessage(chatId, "Успешно записано!");
        }
        if (botState.equals(BotState.ASK_TASK_TWO)) {
            replyToUser = new SendMessage(chatId, "Успешно записано!");
        }
        if (botState.equals(BotState.ASK_TASK_THREE)) {
            replyToUser = new SendMessage(chatId, "Успешно записано!");
        }
        if (botState.equals(BotState.ASK_TASK_FOUR)) {
            replyToUser = new SendMessage(chatId, "Успешно записано!");
        }
        if (botState.equals(BotState.ASK_TASK_FIVE)) {
            replyToUser = new SendMessage(chatId, "Успешно записано!");
        }
        if (botState.equals(BotState.ASK_TASK_SIX)) {
            replyToUser = new SendMessage(chatId, "Успешно записано!");
        }
        return replyToUser;
    }
}
