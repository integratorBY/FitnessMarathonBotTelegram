package com.example.fitnessMarathonBot.botapi.admin.menu;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.service.broadcasting.BroadcastService;
import com.example.fitnessMarathonBot.service.MessageService;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Bot myBot;
    private ReplyMessagesService messagesService;

    @Autowired
    private MessageService service;

    @Autowired
    private BroadcastService broadcastService;

    public NewMarathonHandler(UserDataCache userDataCache, @Lazy Bot myBot, ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.myBot = myBot;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message inputMsg) {
        final int userId = inputMsg.getFrom().getId();
        service.updateDateInDB();
        service.newDayNewListUserGoals();
        service.newDayNewPhotoUserReport();
//        service.nexDayMarathon();
//        final UserProfileData profileData = userDataCache.getUserProfileData(userId);
        long chatId = inputMsg.getChatId();
////        Date date = new Date();
////        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
////        formatForDateNow.format(date);
//
//        BotState botState = userDataCache.getUsersCurrentBotState(userId);
//
        SendMessage replyToUser = null;
//
//        String messageSetDateToStartMarathon = messagesService.getReplyText("reply.SetDateToStartMarathon");
//        String messageSetDateToFinishMarathon = messagesService.getReplyText("reply.SetDateToFinishMarathon");
//        if (botState.equals(BotState.START_NEW_MARATHON)) {
//
//            replyToUser = messagesService.getReplyMessage(chatId, "reply.askMarathonStart");
//            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_DATE_START_MARATHON);
//        } else if (botState.equals(BotState.ASK_DATE_START_MARATHON)) {
//            System.out.println(inputMsg);
//            replyToUser = messagesService.getReplyMessage(chatId, "reply.SetDateToFinishMarathon");
//            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_DATE_FINISH_MARATHON);
//        }

        replyToUser = new SendMessage(chatId, "Начинаем марафон");
        userDataCache.setUsersCurrentBotState(userId, BotState.START_NEW_MARATHON);
        return replyToUser;
    }
    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonInputPersonalInfo = new InlineKeyboardButton().setText("Заполнить личную информацию");

        //Every button must have callBackData, or else not work !
        buttonInputPersonalInfo.setCallbackData("buttonInputPersonalInfo");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonInputPersonalInfo);


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
