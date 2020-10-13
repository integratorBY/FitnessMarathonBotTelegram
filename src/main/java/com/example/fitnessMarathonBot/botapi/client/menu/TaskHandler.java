package com.example.fitnessMarathonBot.botapi.client.menu;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.ListGoals;
import com.example.fitnessMarathonBot.fitnessDB.repository.ListGoalsRepository;
import com.example.fitnessMarathonBot.fitnessDB.service.ListGoalsService;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TaskHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private Bot myBot;
    @Autowired
    private ReplyMessagesService replyMessagesService;

    @Autowired
    private ListGoalsService listGoalsService;

    @Autowired
    private ListGoalsRepository listGoalsRepository;

    public TaskHandler(UserDataCache userDataCache, @Lazy Bot myBot) {
        this.userDataCache = userDataCache;
        this.myBot = myBot;
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

    @SneakyThrows
    private SendMessage getMessageAndGoalsButton(long chatId) {
        Date date = new Date();
        SendMessage replyToUser = null;
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        String message = "";
        int quantityTasks = listGoalsService.countGoalsToday();
        if (listGoalsRepository.findListGoalsByTimeStamp(formatForDateNow.format(date)) != null) {
            ListGoals listGoals = listGoalsRepository.findListGoalsByTimeStamp(formatForDateNow.format(date));
            if (quantityTasks == 1){
                message = String.format(replyMessagesService.getReplyText("reply.goalsForTodayOne"),
                        listGoals.getTaskOne());
            } else if (quantityTasks == 2){
                message = String.format(replyMessagesService.getReplyText("reply.goalsForTodayTwo"),
                        listGoals.getTaskOne(), listGoals.getTaskTwo());
            } else if (quantityTasks == 3){
                message = String.format(replyMessagesService.getReplyText("reply.goalsForTodayThree"),
                        listGoals.getTaskOne(), listGoals.getTaskTwo(), listGoals.getTaskThree());
            } else if (quantityTasks == 4){
                message = String.format(replyMessagesService.getReplyText("reply.goalsForTodayFour"),
                        listGoals.getTaskOne(), listGoals.getTaskTwo(), listGoals.getTaskThree(),
                        listGoals.getTaskFour());
            } else if (quantityTasks == 5){
                message = String.format(replyMessagesService.getReplyText("reply.goalsForTodayFive"),
                        listGoals.getTaskOne(), listGoals.getTaskTwo(), listGoals.getTaskThree(),
                        listGoals.getTaskFour(), listGoals.getTaskFive());
            } else if (quantityTasks == 6){
                message = String.format(replyMessagesService.getReplyText("reply.goalsForToday"),
                        listGoals.getTaskOne(), listGoals.getTaskTwo(), listGoals.getTaskThree(),
                        listGoals.getTaskFour(), listGoals.getTaskFive(), listGoals.getTaskSix());
            }

           replyToUser = new SendMessage(chatId, message);
        } else {
            replyToUser = new SendMessage(chatId, "Список целей пуст, сегодня выходной!");
        }
        myBot.execute(replyToUser);
        return replyToUser;
    }
}
