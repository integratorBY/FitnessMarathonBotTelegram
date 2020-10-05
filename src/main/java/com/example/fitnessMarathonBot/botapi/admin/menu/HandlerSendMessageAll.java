package com.example.fitnessMarathonBot.botapi.admin.menu;

import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class HandlerSendMessageAll implements InputMessageHandler {
    private UserDataCache userDataCache;

    public HandlerSendMessageAll(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @SneakyThrows
    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        SendMessage replMessage = new SendMessage(message.getChatId(), "Введите сообщение ");
        userDataCache.setUsersCurrentBotState(userId,BotState.INPUT_MESSAGE_OT_ALL);
        return replMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SEND_MESSAGE_ALL;
    }
}
