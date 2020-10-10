package com.example.fitnessMarathonBot.botapi.client.personalInformation;


import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.bean.UserProfileData;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.BodyParam;
import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserProfile;
import com.example.fitnessMarathonBot.fitnessDB.bean.embedded.UserProfilesId;
import com.example.fitnessMarathonBot.fitnessDB.repository.BodyParamRepositoryImpl;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserProfileImpl;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import com.example.fitnessMarathonBot.service.UserMainMenuService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
@Getter
@Setter
public class FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private Bot myBot;
    private UserMainMenuService userMainMenuService;

    UserProfilesId userProfilesId = new UserProfilesId();

    @Autowired
    private UserProfileImpl userProfileImpl;

    @Autowired
    private BodyParamRepositoryImpl bodyParamRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    UserProfile userProfile = null;
    BodyParam bodyParam = null;
    User user = null;

    public FillingProfileHandler(UserDataCache userDataCache, UserMainMenuService userMainMenuService,
                                 ReplyMessagesService messagesService, @Lazy Bot myBot) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.myBot = myBot;
        this.userMainMenuService = userMainMenuService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.ASK_PERSONAL_INFO)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_NAME);
        }

        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_PERSONAL_INFO;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

//        ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_AGE)) {
            user = userRepository.findUserByChatId(chatId);
            if (userProfileImpl.findUserProfileByPkUser(user) != null) {
                userProfile = userProfileImpl.findUserProfileByPkUser(user);
                userProfile.setFullName(usersAnswer);
                userProfileImpl.save(userProfile);
            } else {
                bodyParam = new BodyParam();
                bodyParam.setUser(user);
                userProfile = new UserProfile();
                bodyParamRepository.save(bodyParam);
                userProfile.setFullName(usersAnswer);
                userProfilesId.setUser(user);
                userProfilesId.setBodyParam(bodyParam);
                userProfile.setPk(userProfilesId);
                userProfileImpl.save(userProfile);
            }
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askAge");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_HEIGHT);
        }
        if (botState.equals(BotState.ASK_HEIGHT)) {
            if (userAnswerIsCorrect(usersAnswer)) {
                user = userRepository.findUserByChatId(chatId);
                if (userProfileImpl.findUserProfileByPkUser(user) != null) {
                    userProfile = userProfileImpl.findUserProfileByPkUser(user);
                    userProfile.setUserAge(usersAnswer);
                    userProfile.setDaysOfTheMarathon(1);
                    userProfileImpl.save(userProfile);
                } else {
                    userProfile = new UserProfile();
                    userProfile.setUserAge(usersAnswer);
                    userProfileImpl.save(userProfile);
                }
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askHeight");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_WEIGHT);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askAge");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_HEIGHT);
            }
        }
        if (botState.equals(BotState.ASK_WEIGHT)) {
            if (userAnswerIsCorrect(usersAnswer)) {
                user = userRepository.findUserByChatId(chatId);
                if (bodyParamRepository.findBodyParamByUser(user) != null) {
                    bodyParam = bodyParamRepository.findBodyParamByUser(user);
                    bodyParam.setHeight(usersAnswer);
                    bodyParamRepository.save(bodyParam);
                } else {
                    bodyParam = new BodyParam();
                    bodyParam.setHeight(usersAnswer);
                    bodyParamRepository.save(bodyParam);
                }
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askWeight");
                userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askHeight");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_WEIGHT);
            }
        }

        if (botState.equals(BotState.PROFILE_FILLED)) {
            Date date = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
            if (userAnswerIsCorrect(usersAnswer)) {
                user = userRepository.findUserByChatId(chatId);
                if (bodyParamRepository.findBodyParamByUser(user) != null) {
                    bodyParam = bodyParamRepository.findBodyParamByUser(user);
                    bodyParam.setWeight(usersAnswer);
                    bodyParam.setUser(userRepository.findUserByChatId(inputMsg.getChatId()));
                    bodyParam.setDate(formatForDateNow.format(date));
                    bodyParamRepository.save(bodyParam);
                    userProfilesId.setUser(user);
                    userProfilesId.setBodyParam(bodyParam);
                    if (userProfileImpl.findUserProfileByPkUser(user) != null) {
                        userProfile = userProfileImpl.findUserProfileByPkUser(user);
                        userProfile.setPk(userProfilesId);
                        userProfileImpl.save(userProfile);
                    } else {
                        userProfile.setPk(userProfilesId);
                        userProfileImpl.save(userProfile);
                    }
                } else {
                    bodyParam = BodyParam.builder()
                            .weight(usersAnswer)
                            .user(userRepository.findUserByChatId(inputMsg.getChatId()))
                            .date(formatForDateNow.format(date))
                            .build();
                    bodyParamRepository.save(bodyParam);
                    userProfilesId.setUser(user);
                    userProfilesId.setBodyParam(bodyParam);
                    userProfile = new UserProfile();
                    userProfile.setPk(userProfilesId);
                    userProfileImpl.save(userProfile);
                }
                try {
                    myBot.execute(new SendMessage(inputMsg.getChatId(), messagesService.getReplyText("reply.profileFilled"))
                            .setReplyMarkup(userMainMenuService.getUserMainMenuKeyboard()));
                    Thread.sleep(20000);
                    myBot.execute(new SendMessage(inputMsg.getChatId(), messagesService.getReplyText("reply.remindSendPhoto")));
                } catch (TelegramApiException | InterruptedException e) {
                    e.printStackTrace();
                }
//                myBot.sendClientMealPlan(inputMsg.getChatId());
                userDataCache.setUsersCurrentBotState(userId, BotState.MAIN_MENU);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askWeight");
                userDataCache.setUsersCurrentBotState(userId, BotState.PROFILE_FILLED);
            }

        }

        userDataCache.saveUserProfileData(userId, profileData);
        return replyToUser;
    }

    private boolean userAnswerIsCorrect(String userAnswer) {
        return userAnswer.matches("[-+]?[0-9]*(\\.|,|)?[0-9]+([eE][-+]?[0-9]+)?");
    }

}
