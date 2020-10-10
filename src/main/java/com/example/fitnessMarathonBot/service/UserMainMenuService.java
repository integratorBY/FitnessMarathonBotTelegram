package com.example.fitnessMarathonBot.service;

import com.example.fitnessMarathonBot.bean.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Управляет отображением главного меню в чате.
 *
 * @author has been inspired by Sergei Viacheslaev's project
 */
@Service
public class UserMainMenuService {
    @Autowired
    private ReplyMessagesService messagesService;

    public SendMessage getUserMainMenuMessage(final long chatId) {
        String message = messagesService.getReplyText("reply.ShowUserMainMenu");
        return new SendMessage(chatId, message).setReplyMarkup(getUserMainMenuKeyboard());
    }

    public ReplyKeyboardMarkup getUserMainMenuKeyboard() {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        row1.add(new KeyboardButton("Задание"));
        row1.add(new KeyboardButton("План питания"));
        row2.add(new KeyboardButton("Отчёт"));
        row3.add(new KeyboardButton("Моя информация"));
        row4.add(new KeyboardButton("Ссылка на чат"));
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

//    private SendMessage createMessageWithKeyboard(final long chatId,
//                                                  String textMessage,
//                                                  final ReplyKeyboardMarkup replyKeyboardMarkup) {
//        final SendMessage sendMessage = new SendMessage();
//        sendMessage.enableMarkdown(true);
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(textMessage);
//        if (replyKeyboardMarkup != null) {
//            sendMessage.setReplyMarkup(replyKeyboardMarkup);
//        }
//        return sendMessage;
//    }
}