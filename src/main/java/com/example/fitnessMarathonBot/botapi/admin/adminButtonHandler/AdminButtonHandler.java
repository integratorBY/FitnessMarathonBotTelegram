package com.example.fitnessMarathonBot.botapi.admin.adminButtonHandler;

import com.example.fitnessMarathonBot.fitnessDB.bean.ListGoals;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserProfile;
import com.example.fitnessMarathonBot.fitnessDB.repository.ListGoalsRepository;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserProfileImpl;
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

    @Autowired
    private UserProfileImpl userProfileRepo;

    @Autowired
    private ListGoalsRepository listGoalsRepository;

    public SendMessage getMessageAndEditGoalButtons(long chatId) {
        List<ListGoals> goals = listGoalsRepository.findAll();
        String selectedGoal = null;
        if (goals.size() != 0) {
            ListGoals goal = goals.get(0);

            selectedGoal = String.format(messagesService.getReplyText("reply.selectedListGoals"),
                    goal.getTimeStamp(), goal.getTaskOne(), goal.getTaskTwo(), goal.getTaskThree(), goal.getTaskFour(),
                    goal.getTaskFive(), goal.getTaskSix());
            return new SendMessage(chatId, selectedGoal).setReplyMarkup(getEditGoalsButton());
        } else {
            return new SendMessage(chatId, "Нет заданий для редактирования, сначала добавьте задание").setReplyMarkup(getGoalsAdminButtons());
        }
    }

    public SendMessage getMessageAndGoalsAdminButtons(long chatId) {
        String message = messagesService.getReplyText("reply.askAdminOperationWithGoals");
        return new SendMessage(chatId, message).setReplyMarkup(getGoalsAdminButtons());
    }

    public SendMessage getMessageAndMealPlanButtons(long chatId) {
        String message = messagesService.getReplyText("reply.askAdminOperationWithGoals");
        return new SendMessage(chatId, message).setReplyMarkup(getMealsPlanButtons());
    }

    public SendMessage getButtonsOperationsWithMealPlanAndMessage(long chatId) {

        return new SendMessage(chatId, "Выберите категорию добавляемого плана").setReplyMarkup(getCategoryMealPlanButtons());
    }

    private InlineKeyboardMarkup getCategoryMealPlanButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonFoodBasket = new InlineKeyboardButton().setText("Добавить продуктовую корзину");
        InlineKeyboardButton buttonOneCategory = new InlineKeyboardButton().setText("0-63 кг.");
        InlineKeyboardButton buttonTwoCategory = new InlineKeyboardButton().setText("63-73 кг.");
        InlineKeyboardButton buttonThreeCategory = new InlineKeyboardButton().setText("73+ кг.");

        //Every button must have callBackData, or else not work !
        buttonFoodBasket.setCallbackData("buttonFoodBasket");
        buttonOneCategory.setCallbackData("buttonOneCategory");
        buttonTwoCategory.setCallbackData("buttonTwoCategory");
        buttonThreeCategory.setCallbackData("buttonThreeCategory");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonFoodBasket);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonOneCategory);
        keyboardButtonsRow2.add(buttonTwoCategory);
        keyboardButtonsRow2.add(buttonThreeCategory);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup getEditGoalsButton() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonEditTimeStamp = new InlineKeyboardButton().setText("Дата");
        InlineKeyboardButton buttonEditTaskOne = new InlineKeyboardButton().setText("Задание 1");
        InlineKeyboardButton buttonEditTaskTwo = new InlineKeyboardButton().setText("Задание 2");
        InlineKeyboardButton buttonEditTaskThree = new InlineKeyboardButton().setText("Задание 3");
        InlineKeyboardButton buttonEditTaskFour = new InlineKeyboardButton().setText("Задание 4");
        InlineKeyboardButton buttonEditTaskFive = new InlineKeyboardButton().setText("Задание 5");
        InlineKeyboardButton buttonEditTaskSix = new InlineKeyboardButton().setText("Задание 6");


        //Every button must have callBackData, or else not work !
        buttonEditTimeStamp.setCallbackData("buttonEditTimeStamp");
        buttonEditTaskOne.setCallbackData("buttonEditTaskOne");
        buttonEditTaskTwo.setCallbackData("buttonEditTaskTwo");
        buttonEditTaskThree.setCallbackData("buttonEditTaskThree");
        buttonEditTaskFour.setCallbackData("buttonEditTaskFour");
        buttonEditTaskFive.setCallbackData("buttonEditTaskFive");
        buttonEditTaskSix.setCallbackData("buttonEditTaskSix");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonEditTimeStamp);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonEditTaskOne);
        keyboardButtonsRow2.add(buttonEditTaskTwo);
        keyboardButtonsRow2.add(buttonEditTaskThree);

        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow3.add(buttonEditTaskFour);
        keyboardButtonsRow3.add(buttonEditTaskFive);
        keyboardButtonsRow3.add(buttonEditTaskSix);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
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

    private InlineKeyboardMarkup getMealsPlanButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonAddMealPlan = new InlineKeyboardButton().setText("Добавить план питания");
        InlineKeyboardButton buttonEditMealPlan = new InlineKeyboardButton().setText("Изменить план питания");
        InlineKeyboardButton buttonDelMealPlan = new InlineKeyboardButton().setText("Удалить план питания");

        //Every button must have callBackData, or else not work !
        buttonAddMealPlan.setCallbackData("buttonAddMealPlan");
        buttonEditMealPlan.setCallbackData("buttonEditMealPlan");
        buttonDelMealPlan.setCallbackData("buttonDelMealPlan");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonAddMealPlan);
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonEditMealPlan);
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        keyboardButtonsRow3.add(buttonDelMealPlan);

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
