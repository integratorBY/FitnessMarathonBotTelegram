package com.example.fitnessMarathonBot.botapi.client.menu;

import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.ListGoals;
import com.example.fitnessMarathonBot.fitnessDB.repository.ListGoalsRepository;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TaskHandler implements InputMessageHandler {
    private UserDataCache userDataCache;

    @Autowired
    private ReplyMessagesService replyMessagesService;

    @Autowired
    private ListGoalsRepository listGoalsRepository;

    public TaskHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();

        userDataCache.setUsersCurrentBotState(userId, BotState.TASK);
        return getMessageAndGoalsButton(message.getChatId());
    }

    @Override
    public BotState getHandlerName() {
        return BotState.TASK;
    }

    public SendMessage getMessageAndGoalsButton(long chatId) {
        Date date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");

        if (listGoalsRepository.findListGoalsByTimeStamp(formatForDateNow.format(date)) != null) {
            ListGoals listGoals = listGoalsRepository.findListGoalsByTimeStamp(formatForDateNow.format(date));
            String message = String.format(replyMessagesService.getReplyText("reply.goalsForToday"),
                    listGoals.getTaskOne(), listGoals.getTaskTwo(), listGoals.getTaskThree(),
                    listGoals.getTaskFour(), listGoals.getTaskFive(), listGoals.getTaskSix());
            return new SendMessage(chatId, message);
        }
        return new SendMessage(chatId, "Список целей пуст, сегодня выходной!");
    }
}
