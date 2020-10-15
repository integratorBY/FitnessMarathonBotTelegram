package com.example.fitnessMarathonBot.botapi.client.menu.reportHandler;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.*;
import com.example.fitnessMarathonBot.fitnessDB.repository.*;
import com.example.fitnessMarathonBot.fitnessDB.service.ListGoalsService;
import com.example.fitnessMarathonBot.fitnessDB.service.UserPhotoService;
import com.example.fitnessMarathonBot.service.CurrentDate;
import com.example.fitnessMarathonBot.utils.Emojis;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class DailyReportHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private Bot myBot;
    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private ListGoalsService listGoalsService;

    @Autowired
    private UserPhotoService photoService;

    @Autowired
    private UserPhotoWeigherRepo userPhotoWeigherRepo;

    @Autowired
    private ListUserGoalsRepository listUserGoalsRepository;

    public DailyReportHandler(UserDataCache userDataCache, @Lazy Bot myBot) {
        this.userDataCache = userDataCache;
        this.myBot = myBot;
    }

    @SneakyThrows
    @Override
    public SendMessage handle(Message message) {
        long chatId = message.getFrom().getId();
        String userListGoal = "Активность сегодня \n\n";
        SendMessage replyToUser = new SendMessage(chatId, " ");
        User user = userRepository.findUserByChatId(message.getChatId());
        String currentTime = CurrentDate.getCurrentDate();
        int quantityTasks = listGoalsService.countGoalsToday();
        ListUserGoals listUserGoals = listUserGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, currentTime);
        if (listUserGoals != null) {
            if (quantityTasks == 1) {
                userListGoal = userListGoal.concat("Задание 1. " + listUserGoals.isTaskOne() + "\n\n");
                userListGoal = userListGoal.concat("Отправленно фото " + photoService.getCountUserPhotos(chatId) + " из 3\n");
            } else if (quantityTasks == 2) {
                userListGoal = userListGoal.concat("Задание 1. " + listUserGoals.isTaskOne() + "\n");
                userListGoal = userListGoal.concat("Задание 2. " + listUserGoals.isTaskTwo() + "\n\n");
                userListGoal = userListGoal.concat("Отправленно фото " + photoService.getCountUserPhotos(chatId) + " из 3\n");
            } else if (quantityTasks == 3) {
                userListGoal = userListGoal.concat("Задание 1. " + listUserGoals.isTaskOne() + "\n");
                userListGoal = userListGoal.concat("Задание 2. " + listUserGoals.isTaskTwo() + "\n");
                userListGoal = userListGoal.concat("Задание 3. " + listUserGoals.isTaskThree() + "\n\n");
                userListGoal = userListGoal.concat("Отправленно фото " + photoService.getCountUserPhotos(chatId) + " из 3\n");
            } else if (quantityTasks == 4) {
                userListGoal = userListGoal.concat("Задание 1. " + listUserGoals.isTaskOne() + "\n");
                userListGoal = userListGoal.concat("Задание 2. " + listUserGoals.isTaskTwo() + "\n");
                userListGoal = userListGoal.concat("Задание 3. " + listUserGoals.isTaskThree() + "\n");
                userListGoal = userListGoal.concat("Задание 4. " + listUserGoals.isTaskFour() + "\n\n");
                userListGoal = userListGoal.concat("Отправленно фото " + photoService.getCountUserPhotos(chatId) + " из 3\n");
            } else if (quantityTasks == 5) {
                userListGoal = userListGoal.concat("Задание 1. " + listUserGoals.isTaskOne() + "\n");
                userListGoal = userListGoal.concat("Задание 2. " + listUserGoals.isTaskTwo() + "\n");
                userListGoal = userListGoal.concat("Задание 3. " + listUserGoals.isTaskThree() + "\n");
                userListGoal = userListGoal.concat("Задание 4. " + listUserGoals.isTaskFour() + "\n");
                userListGoal = userListGoal.concat("Задание 5. " + listUserGoals.isTaskFive() + "\n\n");
                userListGoal = userListGoal.concat("Отправленно фото " + photoService.getCountUserPhotos(chatId) + " из 3\n");
            } else if (quantityTasks == 6) {
                userListGoal = userListGoal.concat("Задание 1. " + listUserGoals.isTaskOne() + "\n");
                userListGoal = userListGoal.concat("Задание 2. " + listUserGoals.isTaskTwo() + "\n");
                userListGoal = userListGoal.concat("Задание 3. " + listUserGoals.isTaskThree() + "\n");
                userListGoal = userListGoal.concat("Задание 4. " + listUserGoals.isTaskFour() + "\n");
                userListGoal = userListGoal.concat("Задание 5. " + listUserGoals.isTaskFive() + "\n");
                userListGoal = userListGoal.concat("Задание 6. " + listUserGoals.isTaskSix() + "\n\n");
                userListGoal = userListGoal.concat("Отправленно фото " + photoService.getCountUserPhotos(chatId) + " из 3\n");
            }
            userListGoal = userListGoal.replaceAll("false", "Не выполнено " + Emojis.NEGATIVE_MARK);
            userListGoal = userListGoal.replaceAll("true", "Выполнено" + Emojis.WHITE_CHECK_MARK);
            replyToUser = new SendMessage(chatId, userListGoal).setReplyMarkup(getInlineMessageButtonsReport(isExistsPhotoWeigher(user)));
        } else {
            replyToUser = new SendMessage(message.getChatId(), "Выберите действие").setReplyMarkup(getInlineMessageButtonsReport(isExistsPhotoWeigher(user)));
        }
        myBot.execute(replyToUser);
        userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.REPORT_OF_THE_DAY);
        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.REPORT_OF_THE_DAY;
    }

    private InlineKeyboardMarkup getInlineMessageButtonsReport(boolean isExists) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        int nowDayWeek = calendar.get(Calendar.DAY_OF_WEEK);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonReportPhoto = new InlineKeyboardButton().setText("Отправить фото");
        InlineKeyboardButton buttonReportGoals = new InlineKeyboardButton().setText("Отметить задание");
        InlineKeyboardButton buttonReportPhotoWeigher = new InlineKeyboardButton().setText("Отправить фото(весы)");

        //Every button must have callBackData, or else not work !
        buttonReportPhoto.setCallbackData("buttonReportPhoto");
        buttonReportGoals.setCallbackData("buttonReportGoals");
        buttonReportPhotoWeigher.setCallbackData("buttonReportPhotoWeigher");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonReportPhoto);
        keyboardButtonsRow1.add(buttonReportGoals);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonReportPhotoWeigher);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        if (nowDayWeek == 5 && !isExists) {
            rowList.add(keyboardButtonsRow2);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    private boolean isExistsPhotoWeigher(User user) {
        if (userPhotoWeigherRepo.findUserPhotoWeigherByTimeStampAndUser(CurrentDate.getCurrentDate(), user) != null) {
            UserPhotoWeigher userPhotoWeigher = userPhotoWeigherRepo.findUserPhotoWeigherByTimeStampAndUser(CurrentDate.getCurrentDate(), user);
            return userPhotoWeigher.getPhoto() != null;
        } else {
            return false;
        }
    }
}
