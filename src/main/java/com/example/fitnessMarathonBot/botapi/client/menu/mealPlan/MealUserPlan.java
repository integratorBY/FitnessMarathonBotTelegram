package com.example.fitnessMarathonBot.botapi.client.menu.mealPlan;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.botapi.constant.NumberConstants;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.BodyParam;
import com.example.fitnessMarathonBot.fitnessDB.bean.MealPlan;
import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserProfile;
import com.example.fitnessMarathonBot.fitnessDB.repository.BodyParamRepositoryImpl;
import com.example.fitnessMarathonBot.fitnessDB.repository.MealPlanRepository;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserProfileImpl;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class MealUserPlan implements InputMessageHandler {
    private UserDataCache userDataCache;
    private Bot myBot;

    @Autowired
    private BodyParamRepositoryImpl bodyParamRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private MealPlanRepository mealPlanRepository;

    @Autowired
    private UserProfileImpl userProfileRepo;

    public static boolean isNextPlan = false;

    public MealUserPlan(UserDataCache userDataCache, @Lazy Bot myBot) {
        this.userDataCache = userDataCache;
        this.myBot = myBot;
    }

    @SneakyThrows
    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        SendMessage sendMessage = new SendMessage(message.getChatId(), " ");
        User user = userRepository.findUserByChatId(userId);
        BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
        UserProfile userProfile = userProfileRepo.findUserProfileByPkUser(user);

        String category = "";
        String day = ""; // проверку сделать
        if (userProfile != null) {
            day = userProfile.getDaysOfTheMarathon() + "";
        }
        if (bodyParam.getWeight() != null) {
            double weight = Double.parseDouble((bodyParam.getWeight()).replace(",", "."));
            category = getCategoryMealPlan(weight);
        } else {
            try {
                myBot.execute(new SendMessage(message.getChatId(), "Чтобы получить план питания, необходимо заполнить параметры тела!"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        MealPlan mealPlan = mealPlanRepository
                .findMealPlanByCategoryAndDayNumber(category, getFoodBasketByDay(Integer.parseInt(day)));
        MealPlan foodBasket = mealPlanRepository
                .findMealPlanByCategoryAndDayNumber("foodBasket", getFoodBasketByDay(Integer.parseInt(day)));

        getSendFoodBasket(message.getChatId(), foodBasket);
        if (mealPlan != null) {
            sendMealPlanToUser(message.getChatId(), myBot, mealPlan);
            if (isCurrentDay(Integer.parseInt(day))) {
                myBot.execute(new SendMessage(message.getChatId(), "Доступен план на следующие 3 дня").setReplyMarkup(getButtonsNextPlan()));

            }
        } else {
            sendMessage = new SendMessage(message.getChatId(), "План питания отсутствует!");
        }
        isNextPlan = false;
        userDataCache.setUsersCurrentBotState(userId, BotState.MEAL_USER_PLAN);
        return sendMessage;
    }

    private void sendMealPlanToUser(long chatId, Bot myBot, MealPlan mealPlan) {
        if (mealPlan.getPlanOne() != null) {
            myBot.sendPhoto(chatId, mealPlan.getPlanOne());
        }
        if (mealPlan.getPlanTwo() != null) {
            myBot.sendPhoto(chatId, mealPlan.getPlanTwo());
        }
        if (mealPlan.getPlanThree() != null) {
            myBot.sendPhoto(chatId, mealPlan.getPlanThree());
        }
    }

    @SneakyThrows
    private void getSendFoodBasket(long chatId, MealPlan foodBasket) {
        if (foodBasket != null) {
            myBot.sendPhoto(chatId, foodBasket.getPlanOne());
        } else {
            myBot.execute(new SendMessage(chatId, "Продуктовая корзина отсутсвует"));
        }
    }

    private boolean isCurrentDay(int day) {
        return day == NumberConstants.FOOD_BASKET_ONE || day == NumberConstants.FOOD_BASKET_TWO ||
                day == NumberConstants.FOOD_BASKET_THREE || day == NumberConstants.FOOD_BASKET_FOUR ||
                day == NumberConstants.FOOD_BASKET_FIVE || day == NumberConstants.FOOD_BASKET_SIX ||
                day == NumberConstants.FOOD_BASKET_SEVEN || day == NumberConstants.FOOD_BASKET_EIGHT ||
                day == NumberConstants.FOOD_BASKET_NINE || day == NumberConstants.FOOD_BASKET_TEN ||
                day == NumberConstants.FOOD_BASKET_ELEVEN;
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

    private InlineKeyboardMarkup getButtonsNextPlan() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonNextPlan = new InlineKeyboardButton().setText("План на следующие 3 дня");

        //Every button must have callBackData, or else not work !
        buttonNextPlan.setCallbackData("buttonNextPlan");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonNextPlan);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    private String getCategoryMealPlan(double weight) {
        if (weight < 63) {
            return "small";
        } else if (weight > 63 && weight < 73) {
            return "middle";
        } else {
            return "big";
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MEAL_USER_PLAN;
    }

    @SneakyThrows
    public void getPlanOnNextThreeDays(long chatId) {
        User user = userRepository.findUserByChatId(chatId);
        BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
        UserProfile userProfile = userProfileRepo.findUserProfileByPkUser(user);

        String category = "";
        String day = "";
        if (userProfile != null) {
            day = userProfile.getDaysOfTheMarathon() + "";
        }
        if (bodyParam.getWeight() != null) {
            double weight = Double.parseDouble((bodyParam.getWeight()).replace(",", "."));
            category = getCategoryMealPlan(weight);
        } else {
            try {
                myBot.execute(new SendMessage(chatId, "Чтобы получить план питания, необходимо заполнить параметры тела!"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        MealPlan mealPlan = mealPlanRepository
                .findMealPlanByCategoryAndDayNumber(category, getFoodBasketByDay(Integer.parseInt(day) + 1));
        MealPlan foodBasket = mealPlanRepository
                .findMealPlanByCategoryAndDayNumber("foodBasket", getFoodBasketByDay(Integer.parseInt(day) + 1));
        getSendFoodBasket(chatId, foodBasket);
        if (mealPlan != null) {
            sendMealPlanToUser(chatId, myBot, mealPlan);
        } else {
            myBot.execute(new SendMessage(chatId, "План питания отсутствует!"));
        }
        isNextPlan = false;
        userDataCache.setUsersCurrentBotState((int) chatId, BotState.MEAL_USER_PLAN);
    }

}
