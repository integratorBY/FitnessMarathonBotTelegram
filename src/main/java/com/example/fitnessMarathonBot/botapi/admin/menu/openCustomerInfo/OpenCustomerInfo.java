package com.example.fitnessMarathonBot.botapi.admin.menu.openCustomerInfo;

import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserProfile;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserProfileImpl;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
public class OpenCustomerInfo implements InputMessageHandler {
    private UserDataCache userDataCache;

    @Autowired
    private ReplyMessagesService messagesService;

    @Autowired
    private UserProfileImpl userProfile;

    public OpenCustomerInfo(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @SneakyThrows
    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        if (userDataCache.getUsersCurrentBotState(userId).equals(BotState.OPEN_CUSTOMER_INFO)) {
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_NUMBER_CLIENT);
        }

        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.OPEN_CUSTOMER_INFO;
    }

    @SneakyThrows
    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        long chatId = inputMsg.getChatId();
        String profileInfo = "";
        SendMessage replyToUser = null;
        if (checkUserAnswerOnDigit(usersAnswer)) {
            int number = Integer.parseInt(usersAnswer);
            if (userProfile.findAll().size() != 0) {
                List<UserProfile> userList = userProfile.findAll();
                if (userList.size() >= number) {
                    UserProfile userProfile = userList.get(number - 1);
                    profileInfo = messagesService.getReplyText("reply.profileInfoClient");
                    profileInfo = String.format(profileInfo, userProfile.getFullName(), userProfile.getUserAge(),
                            userProfile.getPk().getBodyParam().getHeight(), userProfile.getPk().getBodyParam().getWeight(),
                            userProfile.getPk().getBodyParam().getArm(), userProfile.getPk().getBodyParam().getStomach(),
                            userProfile.getPk().getBodyParam().getNeck(), userProfile.getPk().getBodyParam().getHips(),
                            userProfile.getPk().getBodyParam().getHip(), userProfile.getPk().getBodyParam().getChest(),
                            userProfile.getPk().getBodyParam().getWaist(), userProfile.getPk().getBodyParam().getShin(),
                            userProfile.getPk().getBodyParam().getDate()).replaceAll("null", "0");
                    replyToUser = new SendMessage(chatId, profileInfo);
                } else {
                    replyToUser = new SendMessage(chatId, "Клиента нет под таким номером: ");
                }
            }
        } else {
            replyToUser = new SendMessage(chatId, "Введите порядковый номер клиента в списке(только цифра): ");
        }
        return replyToUser;
    }

    private boolean checkUserAnswerOnDigit(String userAnswer) {
        return userAnswer.matches("[0-9]");
    }
}
