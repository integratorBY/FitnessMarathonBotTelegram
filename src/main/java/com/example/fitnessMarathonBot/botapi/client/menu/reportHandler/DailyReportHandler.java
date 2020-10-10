package com.example.fitnessMarathonBot.botapi.client.menu.reportHandler;

import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.ListUserGoals;
import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserPhoto;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserProfile;
import com.example.fitnessMarathonBot.fitnessDB.repository.ListUserGoalsRepository;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserPhotoRepository;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserProfileImpl;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import com.example.fitnessMarathonBot.fitnessDB.service.UserPhotoService;
import com.example.fitnessMarathonBot.utils.Emojis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DailyReportHandler  implements InputMessageHandler {
    private UserDataCache userDataCache;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private UserPhotoService photoService;

    @Autowired
    private UserPhotoRepository userPhotoRepo;

    @Autowired
    private ListUserGoalsRepository listUserGoalsRepository;

    public DailyReportHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        long chatId = message.getFrom().getId();
        String userListGoal = "Активность сегодня \n\n";
        SendMessage replyToUser = null;
        User user = userRepository.findUserByChatId(message.getChatId());
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentTime = dateFormat.format(date);
        ListUserGoals listUserGoals = listUserGoalsRepository.findListUserGoalsByUserAndTimeStamp(user, currentTime);
        UserPhoto userPhoto = userPhotoRepo.findUserPhotoByTimeStampAndUser(currentTime, user);
        if (listUserGoals != null) {
            userListGoal = userListGoal.concat("Задание 1. " + listUserGoals.isTaskOne() + "\n");
            userListGoal = userListGoal.concat("Задание 2. " + listUserGoals.isTaskTwo() + "\n");
            userListGoal = userListGoal.concat("Задание 3. " + listUserGoals.isTaskThree() + "\n");
            userListGoal = userListGoal.concat("Задание 4. " + listUserGoals.isTaskFour() + "\n");
            userListGoal = userListGoal.concat("Задание 5. " + listUserGoals.isTaskFive() + "\n");
            userListGoal = userListGoal.concat("Задание 6. " + listUserGoals.isTaskSix() + "\n\n");
            userListGoal = userListGoal.concat("Отправленно фото " + photoService.getCountUserPhotos(chatId) + " из 3\n");
            userListGoal = userListGoal.replaceAll("false", "Не выполнено " + Emojis.NEGATIVE_MARK);
            userListGoal = userListGoal.replaceAll("true", "Выполнено" + Emojis.WHITE_CHECK_MARK);
            replyToUser = new SendMessage(chatId, userListGoal).setReplyMarkup(getInlineMessageButtonsReport());
        } else {
            replyToUser = new SendMessage(message.getChatId(), "Выберите действие").setReplyMarkup(getInlineMessageButtonsReport());
        }
        userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.REPORT_OF_THE_DAY);
        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.REPORT_OF_THE_DAY;
    }

    private InlineKeyboardMarkup getInlineMessageButtonsReport() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonReportPhoto = new InlineKeyboardButton().setText("Отправить фото");
        InlineKeyboardButton buttonReportGoals = new InlineKeyboardButton().setText("Отметить задание");

        //Every button must have callBackData, or else not work !
        buttonReportPhoto.setCallbackData("buttonReportPhoto");
        buttonReportGoals.setCallbackData("buttonReportGoals");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonReportPhoto);
        keyboardButtonsRow1.add(buttonReportGoals);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
