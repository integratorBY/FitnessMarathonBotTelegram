package com.example.fitnessMarathonBot.botapi.admin.menu;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
public class Sender implements InputMessageHandler {
    private UserDataCache userDataCache;
    private Bot myBot;
    @Autowired
    private UserRepositoryImpl userRepository;

    public Sender(UserDataCache userDataCache, @Lazy Bot myBot) {
        this.userDataCache = userDataCache;
        this.myBot = myBot;
    }

    @SneakyThrows
    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        if (userDataCache.getUsersCurrentBotState(userId).equals(BotState.ASK_MESSAGE_FOR_USER)) {
            userDataCache.setUsersCurrentBotState(userId, BotState.INPUT_MESSAGE_OT_ALL);
        }

        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {

        return BotState.ASK_MESSAGE_FOR_USER;
    }

    @SneakyThrows
    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        long chatId = inputMsg.getChatId();
        SendMessage replyToUser = null;
        List<User> userList = userRepository.findAll();
        if (userList.size() != 0) {
            for (User user : userList) {
                myBot.execute(new SendMessage(user.getChatId(), usersAnswer));
            }
        }
        replyToUser = new SendMessage(chatId, "Сообщение отправленно!");

        return replyToUser;
    }
}
