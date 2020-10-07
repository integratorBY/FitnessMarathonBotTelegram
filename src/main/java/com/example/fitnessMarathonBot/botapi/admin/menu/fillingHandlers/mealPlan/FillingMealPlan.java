package com.example.fitnessMarathonBot.botapi.admin.menu.fillingHandlers.mealPlan;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.botapi.admin.adminButtonHandler.AdminButtonHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.MealPlan;
import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserProfile;
import com.example.fitnessMarathonBot.fitnessDB.repository.MealPlanRepository;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserProfileImpl;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import com.example.fitnessMarathonBot.regex.RegexHandler;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class FillingMealPlan implements InputMessageHandler {
    private UserDataCache userDataCache;
    private Bot myBot;
    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private UserProfileImpl userProfileRepo;

    @Autowired
    private ReplyMessagesService messagesService;

    @Autowired
    private MealPlanRepository mealPlanRepository;

    @Autowired
    private AdminButtonHandler adminButtonHandler;

    private static UserProfile userProfile;

    public FillingMealPlan(UserDataCache userDataCache, ReplyMessagesService messagesService,
                           @Lazy Bot myBot) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.myBot = myBot;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.ASK_ADMIN_MEAL_PLAN)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_ADMIN_NUMBER_USER_PLAN);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_ADMIN_MEAL_PLAN;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        Date dateObj = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = formatForDateNow.format(dateObj);

        SendMessage replyToUser = null;
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        if (botState.equals(BotState.ASK_ADMIN_NUMBER_USER_PLAN)) {
            if (RegexHandler.checkUserAnswerOnDigit(usersAnswer)) {
                List<UserProfile> userProfileList = userProfileRepo.findAll();
                int number = Integer.parseInt(usersAnswer);
                if (userProfileList.size() >= number) {
                    userProfile = userProfileList.get(number - 1);
                    String profile = "Вы выбрали клиента: ";
                    profile = profile.concat(userProfile.getFullName() + ".\n");
                    profile = profile.concat("  - Возраст: " + userProfile.getUserAge() + "\n");
                    profile = profile.concat("  - Рост: " + userProfile.getPk().getBodyParam().getHeight() + "\n");
                    profile = profile.concat("  - Вес: " + userProfile.getPk().getBodyParam().getWeight() + "\n\n");
                    profile = profile.concat("Напишите план питания для клиента: ");
                    userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_PLAN);
                    replyToUser = new SendMessage(chatId, profile);
                } else {
                    replyToUser = new SendMessage(chatId, "Клиента нет под таким номером!");
                }
            } else {
                replyToUser = new SendMessage(chatId, "Введите порядковый номер клиента в списке(только цифра):");
            }
        }
        if (botState.equals(BotState.ASK_ADMIN_PLAN)) {
            User user = userProfile.getPk().getUser();
            MealPlan mealPlan = MealPlan.builder()
                    .plan(usersAnswer)
                    .user(user)
                    .build();
            mealPlanRepository.save(mealPlan);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_NUMBER_USER_PLAN);
            try {
                myBot.execute(new SendMessage(chatId, "План питания для клиента " +
                        userProfile.getFullName() + " успешно добавлен!"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            replyToUser = adminButtonHandler.getMessageAndMealPlanButtons(chatId);
        }
        return replyToUser;
    }

}
