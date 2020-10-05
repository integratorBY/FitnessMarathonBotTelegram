package com.example.fitnessMarathonBot.botapi.client.personalInformation;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.botapi.admin.menu.openCustomerInfo.OpenCustomerInfo;
import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserProfile;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserProfileImpl;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * @author has been inspired by Sergei Viacheslaev's work
 */
@Component
public class ShowProfileHandler implements InputMessageHandler {

    @Autowired
    private ReplyMessagesService messagesService;

    private Bot myBot;

    @Autowired
    private UserProfileImpl userProfileRepo;

    @Autowired
    private UserRepositoryImpl userRepository;

    public ShowProfileHandler(@Lazy Bot myBot) {
        this.myBot = myBot;
    }

    @SneakyThrows
    @Override
    public SendMessage handle(Message message) {
        SendMessage sendMessage = null;
        long chatId = message.getChatId();
        User user = userRepository.findUserByChatId(chatId);
        if (userProfileRepo.findUserProfileByPkUser(user) == null) {
            sendMessage = new SendMessage(chatId, "Данные отсутствуют");
        } else {
            UserProfile userProfile = userProfileRepo.findUserProfileByPkUser(user);
            boolean isPhoto = checkUserProfilePhoto(userProfile);
            String profileInfo = messagesService.getReplyText("reply.profileInfo");
            profileInfo = String.format(profileInfo, userProfile.getFullName(), userProfile.getUserAge(),
                    userProfile.getPk().getBodyParam().getHeight(), userProfile.getPk().getBodyParam().getWeight(),
                    userProfile.getPk().getBodyParam().getArm(), userProfile.getPk().getBodyParam().getStomach(),
                    userProfile.getPk().getBodyParam().getNeck(), userProfile.getPk().getBodyParam().getHips(),
                    userProfile.getPk().getBodyParam().getHip(), userProfile.getPk().getBodyParam().getChest(),
                    userProfile.getPk().getBodyParam().getWaist(), userProfile.getPk().getBodyParam().getShin(),
                    userProfile.getPk().getBodyParam().getDate());
            if (profileInfo.contains("null")) {
                profileInfo = profileInfo.replaceAll("null", "0");
                sendMessage = new SendMessage(chatId, profileInfo).setReplyMarkup(getInlineMessageButtons(isPhoto));
            } else {
                sendMessage = new SendMessage(chatId, profileInfo).setReplyMarkup(getButtonEditPersonalInfo(isPhoto));
            }

            OpenCustomerInfo.sendCustomerPhoto(chatId, userProfile, myBot);
        }
        return sendMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MY_INFORMATION;
    }

    private boolean checkUserProfilePhoto(UserProfile userProfile) {
        return userProfile.getPhotoId_1() != null && userProfile.getPhotoId_2() != null &&
                userProfile.getPhotoId_3() != null;
    }

    private InlineKeyboardMarkup getInlineMessageButtons(boolean isPhoto) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonPersonalInfo = new InlineKeyboardButton().setText("Заполнить остальные данные");
        InlineKeyboardButton buttonLoadPhoto = new InlineKeyboardButton().setText("Отправить фото(тело)");

        buttonLoadPhoto.setCallbackData("buttonLoadPhoto");
        buttonPersonalInfo.setCallbackData("buttonPersonalInfo");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();

        if (!isPhoto) {
            keyboardButtonsRow1.add(buttonPersonalInfo);
            List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
            keyboardButtonsRow2.add(buttonLoadPhoto);
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            rowList.add(keyboardButtonsRow1);
            rowList.add(keyboardButtonsRow2);

            inlineKeyboardMarkup.setKeyboard(rowList);
        } else {
            keyboardButtonsRow1.add(buttonPersonalInfo);

            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            rowList.add(keyboardButtonsRow1);

            inlineKeyboardMarkup.setKeyboard(rowList);
        }



        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup getButtonEditPersonalInfo(boolean isPhoto) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonEditPersonalInfo = new InlineKeyboardButton().setText("Изменить данные");
        InlineKeyboardButton buttonLoadPhoto = new InlineKeyboardButton().setText("Отправить фото(тело)");

        buttonLoadPhoto.setCallbackData("buttonLoadPhoto");
        buttonEditPersonalInfo.setCallbackData("buttonEditPersonalInfo");

        System.out.println(isPhoto);

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonEditPersonalInfo);

        if (!isPhoto) {
            keyboardButtonsRow1.add(buttonLoadPhoto);
        }

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}