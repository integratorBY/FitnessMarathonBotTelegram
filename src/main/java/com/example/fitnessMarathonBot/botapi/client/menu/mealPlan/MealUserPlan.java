package com.example.fitnessMarathonBot.botapi.client.menu.mealPlan;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.MealPlan;
import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserProfile;
import com.example.fitnessMarathonBot.fitnessDB.repository.MealPlanRepository;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserProfileImpl;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MealUserPlan implements InputMessageHandler {
    private UserDataCache userDataCache;
    private Bot myBot;

    @Autowired
    private ReplyMessagesService replyMessagesService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private MealPlanRepository mealPlanRepository;

    @Autowired
    private UserProfileImpl userProfileRepo;

    public MealUserPlan(UserDataCache userDataCache, @Lazy Bot myBot) {
        this.userDataCache = userDataCache;
        this.myBot = myBot;
    }

    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        SendMessage sendMessage = null;
        User user = userRepository.findUserByChatId(userId);
        UserProfile userProfile = userProfileRepo.findUserProfileByPkUser(user);
        String category = "";
        String day = userProfile.getDaysOfTheMarathon()+"";
        double weight = Double.parseDouble(userProfile.getPk().getBodyParam().getWeight());
        if (weight < 63) {
            category = "small";
        } else if (weight > 63 && weight < 73) {
            category = "middle";
        } else {
            category = "big";
        }
        MealPlan mealPlan = mealPlanRepository.findMealPlanByCategoryAndDayNumber(category, day);
        MealPlan foodBasket = mealPlanRepository.findMealPlanByCategory("foodBasket");
        if (mealPlan != null) {
            myBot.sendPhoto(message.getChatId(), foodBasket.getPlan());
            myBot.sendPhoto(message.getChatId(), mealPlan.getPlan());
        } else {
            sendMessage = new SendMessage(message.getChatId(), "Какой план?! Тебе вообще есть нельзя!!!");
        }
        userDataCache.setUsersCurrentBotState(userId, BotState.MEAL_USER_PLAN);
        return sendMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MEAL_USER_PLAN;
    }

}
