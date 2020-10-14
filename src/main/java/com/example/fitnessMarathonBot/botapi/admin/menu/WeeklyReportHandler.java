package com.example.fitnessMarathonBot.botapi.admin.menu;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.service.ReportService;
import com.example.fitnessMarathonBot.utils.Emojis;
import lombok.SneakyThrows;
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
public class WeeklyReportHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private Bot myBot;
    @Autowired
    private ReportService reportService;

    public WeeklyReportHandler(UserDataCache userDataCache, @Lazy Bot myBot) {
        this.userDataCache = userDataCache;
        this.myBot = myBot;
    }

    @SneakyThrows
    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();

        myBot.execute(new SendMessage(message.getChatId(), reportService.getReports()));
        myBot.execute(new SendMessage(message.getChatId(), ""+Emojis.POINT_DOWN+Emojis.POINT_DOWN+Emojis.POINT_DOWN).
                setReplyMarkup(getButtonDetailedReport()));
        userDataCache.setUsersCurrentBotState(userId, BotState.WEEKLY_REPORT);
        return new SendMessage(message.getChatId(), ""+Emojis.POINT_UP+Emojis.POINT_UP+Emojis.POINT_UP);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.WEEKLY_REPORT;
    }

    private InlineKeyboardMarkup getButtonDetailedReport() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonDetailedReport = new InlineKeyboardButton().setText("Подробный отчёт");

        buttonDetailedReport.setCallbackData("buttonDetailedReport");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonDetailedReport);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
