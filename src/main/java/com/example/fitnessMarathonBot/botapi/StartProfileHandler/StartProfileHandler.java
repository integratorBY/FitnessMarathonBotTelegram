package com.example.fitnessMarathonBot.botapi.StartProfileHandler;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import com.example.fitnessMarathonBot.service.AdminMainMenuService;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import com.example.fitnessMarathonBot.utils.Emojis;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class StartProfileHandler implements InputMessageHandler {
    private ReplyMessagesService messagesService;
    private UserDataCache userDataCache;
    private AdminMainMenuService adminMainMenuService;
    private Bot myBot;

    @Autowired
    private UserRepositoryImpl userRepository;

    public StartProfileHandler(ReplyMessagesService messagesService, UserDataCache userDataCache,
                               AdminMainMenuService adminMainMenuService, @Lazy Bot myBot) {
        this.messagesService = messagesService;
        this.userDataCache = userDataCache;
        this.adminMainMenuService = adminMainMenuService;
        this.myBot = myBot;
    }

    @SneakyThrows
    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.ASK_START)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_PERSONAL_INFO);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_START;
    }

    @SneakyThrows
    private SendMessage processUsersInput(Message inputMsg) {
        List<SendMessage> sendMessages = new ArrayList<>();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();
        SendMessage replyToUser = null;

        if (userId == 1331718111) {
            final LocalDateTime localNow = LocalDateTime.now(Clock.systemUTC());
            System.out.println(localNow);
            replyToUser = adminMainMenuService.getAdminMainMenuMessage(chatId, "Тут какое то приветствие админа");
        } else {
            User user = User.builder()
                    .firstName(inputMsg.getFrom().getFirstName())
                    .lastName(inputMsg.getFrom().getLastName())
                    .chatId(inputMsg.getFrom().getId()).build();
            if (userRepository.findUserByChatId(inputMsg.getChatId()) == null){
                userRepository.save(user);
            }

            String messageStart = messagesService.getReplyText("reply.askStart");
            String messageLinkInstagram = String.format(messagesService.getReplyText("reply.linkInstagram"),
                    Emojis.POINT_DOWN, Emojis.POINT_DOWN, Emojis.POINT_DOWN, Emojis.POINT_DOWN, Emojis.POINT_DOWN, Emojis.HEART);
            String messageGreeting = String.format(messagesService.getReplyText("reply.greeting"),
                    Emojis.HEART, Emojis.POINT_DOWN);
            String messageWhatAwaitsUs = String.format(messagesService.getReplyText("reply.whatAwaitsUs"),
                    Emojis.ARROW_RIGHT, Emojis.HEAVY_CHECK_MARK, Emojis.HEAVY_CHECK_MARK, Emojis.HEAVY_CHECK_MARK, Emojis.HEAVY_CHECK_MARK, Emojis.HEAVY_CHECK_MARK);
            String messageControl = String.format(messagesService.getReplyText("reply.Control"), Emojis.POINT_DOWN, Emojis.POINT_DOWN, Emojis.POINT_DOWN);
            String messageTrackingHabits = messagesService.getReplyText("reply.TrackingHabits");
            String messageDayOne = String.format(messagesService.getReplyText("reply.DayOne"), Emojis.WARNING, Emojis.HEART,
                    Emojis.ARROW_RIGHT, Emojis.ARROW_RIGHT, Emojis.POINT_RIGHT, Emojis.POINT_RIGHT, Emojis.POINT_RIGHT,
                    Emojis.HEAVY_CHECK_MARK, Emojis.POINT_RIGHT, Emojis.ARROW_RIGHT, Emojis.POINT_RIGHT, Emojis.GRIN,
                    Emojis.ARROW_RIGHT, Emojis.WARNING, Emojis.ARROW_RIGHT);
            String messageToday = String.format(messagesService.getReplyText("reply.Today"), Emojis.POINT_RIGHT, Emojis.POINT_RIGHT,
                    Emojis.POINT_RIGHT);
            String messageIndividualPlanCaution = String.format(messagesService.getReplyText("reply.IndividualPlanCaution"), Emojis.BLUSH);
            String messageDietPlanInstruction = String.format(messagesService.getReplyText("reply.DietPlanInstruction"), Emojis.POINT_RIGHT);
            sendMessages.add(new SendMessage(chatId, messageGreeting));
            sendMessages.add(new SendMessage(chatId, messageStart));
            sendMessages.add(new SendMessage(chatId, messageLinkInstagram));
            sendMessages.add(new SendMessage(chatId, messageWhatAwaitsUs));
            myBot.sendListMessages(sendMessages);

//            myBot.sendPhoto(chatId, "", "whatAwaitsUs");
////            Thread.sleep(5000);
//            myBot.execute(new SendMessage(chatId,
//                    String.format(messagesService.getReplyText("reply.attention"),
//                            Emojis.WARNING, Emojis.NO_ENTRY_SIGN, Emojis.HEAVY_CHECK_MARK, Emojis.HEAVY_CHECK_MARK,
//                            Emojis.WARNING, Emojis.WARNING, Emojis.WARNING, Emojis.WARNING, Emojis.WARNING, Emojis.WARNING)));
////            Thread.sleep(5000);
//            String messageRegulations = String.format(messagesService.getReplyText("reply.fundamentalRiles"), Emojis.ARROW_RIGHT, Emojis.ARROW_RIGHT, Emojis.ARROW_RIGHT);
//            myBot.execute(new SendMessage(chatId, messageRegulations));
//            myBot.sendPhoto(chatId, "", "regulations");
////            Thread.sleep(5000);
//            myBot.execute(new SendMessage(chatId, String.format(messagesService.getReplyText("reply.whatNeedMarathon"),
//                    Emojis.POINT_DOWN, Emojis.POINT_DOWN, Emojis.POINT_DOWN, Emojis.POINT_DOWN, Emojis.POINT_DOWN)));
//            myBot.sendPhoto(chatId, "", "whatNeedMarathon");
////            Thread.sleep(5000);
//            myBot.execute(new SendMessage(chatId,
//                    String.format(messagesService.getReplyText("reply.taskWeek"), Emojis.WARNING, Emojis.ARROW_RIGHT,
//                            Emojis.POINT_RIGHT, Emojis.ARROW_RIGHT, Emojis.POINT_RIGHT, Emojis.WARNING, Emojis.ARROW_RIGHT, Emojis.ARROW_RIGHT,
//                            Emojis.SUNNY, Emojis.BLUSH, Emojis.BLUSH, Emojis.BLUSH, Emojis.ARROW_RIGHT,
//                            Emojis.POINT_DOWN,Emojis.POINT_DOWN,Emojis.POINT_DOWN,Emojis.POINT_DOWN,Emojis.POINT_DOWN,Emojis.POINT_DOWN,
//                            Emojis.POINT_DOWN,Emojis.POINT_DOWN,Emojis.POINT_DOWN,Emojis.POINT_DOWN)));
//            myBot.sendPhoto(chatId, "", "howMuchWater");
////            Thread.sleep(5000);
//
//            myBot.execute(new SendMessage(chatId, messageControl));
//            myBot.execute(new SendMessage(chatId, messageTrackingHabits));
//            myBot.sendPhoto(chatId, "","habitsTrackerAndSentReports");
////            Thread.sleep(5000);
//
//            myBot.execute(new SendMessage(chatId, messageDayOne));
//            myBot.sendPhoto(chatId, "", "reportHint");
//            myBot.execute(new SendMessage(chatId, messageToday));
//            myBot.sendPhoto(chatId, "", "everyMonday");
////            Thread.sleep(5000);
//
//            myBot.execute(new SendMessage(chatId, messageIndividualPlanCaution));
//            myBot.execute(new SendMessage(chatId, messageDietPlanInstruction));
//            myBot.sendPhoto(chatId, "" + Emojis.ARROW_DOWN + Emojis.ARROW_DOWN + Emojis.ARROW_DOWN +
//                    Emojis.ARROW_DOWN + Emojis.ARROW_DOWN,  "howWeightProducts");



            userDataCache.setUsersCurrentBotState(inputMsg.getFrom().getId(), BotState.ASK_PERSONAL_INFO);
            replyToUser = new SendMessage(chatId,
                    String.format(messagesService.getReplyText("reply.requestEnterYourData"), Emojis.MEMO, Emojis.POINT_DOWN));

            replyToUser.setReplyMarkup(getInlineMessageButtons());
        }

        return replyToUser;
    }

    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonInputPersonalInfo = new InlineKeyboardButton().setText("Заполнить личную информацию");

        //Every button must have callBackData, or else not work !
        buttonInputPersonalInfo.setCallbackData("buttonInputPersonalInfo");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonInputPersonalInfo);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
