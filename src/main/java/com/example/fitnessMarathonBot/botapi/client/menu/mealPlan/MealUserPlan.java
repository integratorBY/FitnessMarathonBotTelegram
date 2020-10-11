package com.example.fitnessMarathonBot.botapi.client.menu.mealPlan;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.botapi.constant.NumberConstants;
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
        MealPlan mealPlanDayOne = null;
        MealPlan mealPlanDayTwo = null;
        MealPlan mealPlanDayThree = null;
        String day = userProfile.getDaysOfTheMarathon()+"";
        double weight = Double.parseDouble(userProfile.getPk().getBodyParam().getWeight());
        category = getCategoryMealPlan(weight);
        if (Integer.parseInt(day) <= 3) {
            mealPlanDayOne = mealPlanRepository.findMealPlanByCategoryAndDayNumber(category, day);
            mealPlanDayTwo = mealPlanRepository.findMealPlanByCategoryAndDayNumber(category, "2");
            mealPlanDayThree = mealPlanRepository.findMealPlanByCategoryAndDayNumber(category, "3");
        }
        String day_number = getFoodBasketByDay(Integer.parseInt(day));
        MealPlan foodBasket = mealPlanRepository.findMealPlanByCategoryAndDayNumber("foodBasket", day_number);
        if (foodBasket != null) {
            myBot.sendPhoto(message.getChatId(), foodBasket.getPlan());
        }
        if (mealPlanDayOne != null) {
            myBot.sendPhoto(message.getChatId(), mealPlanDayOne.getPlan());
            myBot.sendPhoto(message.getChatId(), mealPlanDayTwo.getPlan());
            myBot.sendPhoto(message.getChatId(), mealPlanDayThree.getPlan());
        } else {
            sendMessage = new SendMessage(message.getChatId(), "План питания отсутствует!");
        }
        userDataCache.setUsersCurrentBotState(userId, BotState.MEAL_USER_PLAN);
        return sendMessage;
    }

    private String getFoodBasketByDay(int day) {
        if (day <= NumberConstants.FOOD_BASKET_ONE) {
            return "1-3";
        } else if (day <= NumberConstants.FOOD_BASKET_TWO) {
            return "4-6";
        } else if (day <= NumberConstants.FOOD_BASKET_THREE) {
            return "7-9";
        } else if (day <= NumberConstants.FOOD_BASKET_FOUR) {
            return "10-12";
        } else if (day <= NumberConstants.FOOD_BASKET_FIVE) {
            return "13-15";
        } else if (day <= NumberConstants.FOOD_BASKET_SIX) {
            return "16-18";
        } else if (day <= NumberConstants.FOOD_BASKET_SEVEN) {
            return "19-21";
        } else if (day <= NumberConstants.FOOD_BASKET_EIGHT) {
            return "22-24";
        } else if (day <= NumberConstants.FOOD_BASKET_NINE) {
            return "25-27";
        } else if (day <= NumberConstants.FOOD_BASKET_TEN) {
            return "28-30";
        } else if (day <= NumberConstants.FOOD_BASKET_ELEVEN) {
            return "31-33";
        }
        return null;
    }

    private String getCategoryMealPlan(double weight) {
        if (weight < 63) {
            return "small";
        } else if (weight > 63 && weight < 73) {
            return  "middle";
        } else {
            return "big";
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MEAL_USER_PLAN;
    }

}
