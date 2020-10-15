package com.example.fitnessMarathonBot.botapi.admin.menu.marathon;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class NewMarathonHandler implements InputMessageHandler {
    private UserDataCache userDataCache;

    public NewMarathonHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message inputMsg) {
        final int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();
        SendMessage replyToUser = null;

        replyToUser = new SendMessage(chatId, "Выберите действие").setReplyMarkup(getInlineMessageButtons());
        userDataCache.setUsersCurrentBotState(userId, BotState.START_NEW_MARATHON);
        return replyToUser;
    }

    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonMarathonOneMonth = new InlineKeyboardButton().setText("1 месяц");
        InlineKeyboardButton buttonMarathonTwoMonth = new InlineKeyboardButton().setText("2 месяца");
        InlineKeyboardButton buttonMarathonThreeMonth = new InlineKeyboardButton().setText("3 месяца");

        //Every button must have callBackData, or else not work !
        buttonMarathonOneMonth.setCallbackData("buttonMarathonOneMonth");
        buttonMarathonTwoMonth.setCallbackData("buttonMarathonTwoMonth");
        buttonMarathonThreeMonth.setCallbackData("buttonMarathonThreeMonth");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonMarathonOneMonth);
        keyboardButtonsRow1.add(buttonMarathonTwoMonth);
        keyboardButtonsRow1.add(buttonMarathonThreeMonth);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.START_NEW_MARATHON;
    }
}
