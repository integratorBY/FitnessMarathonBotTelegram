package com.example.fitnessMarathonBot.botapi.client.menu;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ShowChatHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private Bot myBot;

    public ShowChatHandler(UserDataCache userDataCache, @Lazy Bot myBot) {
        this.userDataCache = userDataCache;
        this.myBot = myBot;
    }

    @SneakyThrows
    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();

        userDataCache.setUsersCurrentBotState(userId, BotState.LINK_TO_CHAT);
        myBot.execute(new SendMessage(message.getChatId(), "https://t.me/joinchat/LJ52BhXtyVnAeZK8SZ3OUQ"));
        return new SendMessage(message.getChatId(), "https://t.me/joinchat/LJ52BhXtyVnAeZK8SZ3OUQ");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.LINK_TO_CHAT;
    }
}
