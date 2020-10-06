package com.example.fitnessMarathonBot.botapi.admin.menu.fillingHandlers;

import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.ListGoals;
import com.example.fitnessMarathonBot.fitnessDB.repository.ListGoalsRepository;
import com.example.fitnessMarathonBot.regex.RegexHandler;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
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
public class FillingGoalsHandler implements InputMessageHandler {
    private UserDataCache userDataCache;

    @Autowired
    private ListGoalsRepository listGoalsRepository;

    @Autowired
    private ReplyMessagesService messagesService;

    private static String date = "";

    public FillingGoalsHandler(UserDataCache userDataCache, ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.ASK_ADMIN_GOALS)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_ADMIN_TIME_STAMP);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_ADMIN_GOALS;
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

        if (botState.equals(BotState.ASK_ADMIN_TASK_ONE)) {
            if (RegexHandler.checkDate(usersAnswer)) {
                date = usersAnswer;
                if (listGoalsRepository.findListGoalsByTimeStamp(usersAnswer) != null) {
                    return new SendMessage(chatId, "Задания на эту дату уже записаны");
                } else {
                    ListGoals listGoals = ListGoals.builder()
                            .taskOne(usersAnswer)
                            .timeStamp(usersAnswer)
                            .build();
                    listGoalsRepository.save(listGoals);
                    replyToUser = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskOne"));
                    userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_TASK_TWO);
                }
            } else {
                replyToUser = new SendMessage(chatId, "Не верный формат даты!");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_TASK_ONE);
            }
        }

        if (botState.equals(BotState.ASK_ADMIN_TASK_TWO)) {
            ListGoals listGoals = listGoalsRepository.findListGoalsByTimeStamp(date);
            listGoals.setTaskOne(usersAnswer);
            listGoalsRepository.save(listGoals);
            replyToUser = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskTwo"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_TASK_THREE);
        }
        if (botState.equals(BotState.ASK_ADMIN_TASK_THREE)) {
            ListGoals listGoals = listGoalsRepository.findListGoalsByTimeStamp(date);
            listGoals.setTaskTwo(usersAnswer);
            listGoalsRepository.save(listGoals);
            replyToUser = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskThree"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_TASK_FOUR);
        }
        if (botState.equals(BotState.ASK_ADMIN_TASK_FOUR)) {
            ListGoals listGoals = listGoalsRepository.findListGoalsByTimeStamp(date);
            listGoals.setTaskThree(usersAnswer);
            listGoalsRepository.save(listGoals);
            replyToUser = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskFour"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_TASK_FIVE);
        }
        if (botState.equals(BotState.ASK_ADMIN_TASK_FIVE)) {
            ListGoals listGoals = listGoalsRepository.findListGoalsByTimeStamp(date);
            listGoals.setTaskFour(usersAnswer);
            listGoalsRepository.save(listGoals);
            replyToUser = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskFive"));
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ADMIN_TASK_SIX);
        }
        if (botState.equals(BotState.ASK_ADMIN_TASK_SIX)) {
            ListGoals listGoals = listGoalsRepository.findListGoalsByTimeStamp(date);
            listGoals.setTaskFive(usersAnswer);
            listGoalsRepository.save(listGoals);
            replyToUser = new SendMessage(chatId, messagesService.getReplyText("reply.askAdminTaskSix"));
            userDataCache.setUsersCurrentBotState(userId, BotState.GOALS_FILLED);
        }
        if (botState.equals(BotState.GOALS_FILLED)) {
            ListGoals listGoals = listGoalsRepository.findListGoalsByTimeStamp(date);
            listGoals.setTaskSix(usersAnswer);
            listGoalsRepository.save(listGoals);
            replyToUser = new SendMessage(chatId, "Задания на дату: " + date + " успешно записаны!");
            userDataCache.setUsersCurrentBotState(userId, BotState.ADMIN_MAIN_MENU);
        }
        /** Edit tasks */

        if (botState.equals(BotState.ASK_ADMIN_NUMBER_GOAL)) {
            if (RegexHandler.checkUserAnswerOnDigit(usersAnswer)) {
                List<ListGoals> goals = listGoalsRepository.findAll();
                int number = Integer.parseInt(usersAnswer);
                if (goals.size() >= number) {
                    ListGoals goal = goals.get(number - 1);
                    String selectedGoal = String.format(messagesService.getReplyText("reply.selectedListGoals"),
                            goal.getTimeStamp(), goal.getTaskOne(), goal.getTaskTwo(), goal.getTaskThree(), goal.getTaskFour(),
                            goal.getTaskFive(), goal.getTaskSix());
                    replyToUser = new SendMessage(chatId, selectedGoal).setReplyMarkup(getEditGoalsButton());
                } else {
                    replyToUser = new SendMessage(chatId, "Нет списка заданий с таким номером!");
                }
            } else {
                replyToUser = new SendMessage(chatId, "Введите порядковый номер в списке заданий(только цифра):");
            }
        }

        /**-----------------*/
        return replyToUser;
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

}
