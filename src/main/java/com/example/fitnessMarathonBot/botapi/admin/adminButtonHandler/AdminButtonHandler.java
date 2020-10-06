package com.example.fitnessMarathonBot.botapi.admin.adminButtonHandler;

import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AdminButtonHandler {
    @Autowired
    private ReplyMessagesService messagesService;

    public SendMessage getMessageAndGoalsAdminButtons(long chatId) {
        String message = messagesService.getReplyText("reply.askAdminOperationWithGoals");
        return new SendMessage(chatId, message).setReplyMarkup(getGoalsAdminButtons());
    }

    private InlineKeyboardMarkup getGoalsAdminButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonAddGoal = new InlineKeyboardButton().setText("Добавить задание");
        InlineKeyboardButton buttonEditGoal = new InlineKeyboardButton().setText("Изменить задание");
        InlineKeyboardButton buttonDelGoal = new InlineKeyboardButton().setText("Удалить задание");

        //Every button must have callBackData, or else not work !
        buttonAddGoal.setCallbackData("buttonAddGoal");
        buttonEditGoal.setCallbackData("buttonEditGoal");
        buttonDelGoal.setCallbackData("buttonDelGoal");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonAddGoal);
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonEditGoal);
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow3.add(buttonDelGoal);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

//    public SendMessage getMessageAndGoalsButton(long chatId) {
//        String message = messagesService.getReplyText("reply.selectNumberGoalButton");
//        return new SendMessage(chatId, message).setReplyMarkup(getEditGoalsButton());
//    }


}
