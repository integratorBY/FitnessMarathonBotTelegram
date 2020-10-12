package com.example.fitnessMarathonBot.botapi.client.personalInformation;

import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.BodyParam;
import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserProfile;
import com.example.fitnessMarathonBot.fitnessDB.repository.BodyParamRepositoryImpl;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserProfileImpl;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import com.example.fitnessMarathonBot.fitnessDB.service.UserProfileService;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class SupplementProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserProfileImpl userProfileRepo;

    @Autowired
    private ReplyMessagesService messagesService;

    @Autowired
    private BodyParamRepositoryImpl bodyParamRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    public SupplementProfileHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.ASK_SUPPLEMENT_PERSONAL_INFO)) {
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_NECK);
        }
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_SUPPLEMENT_PERSONAL_INFO;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();
        SendMessage replyToUser = null;
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        User user = userRepository.findUserByChatId(inputMsg.getChatId());
        UserProfile userProfile = userProfileRepo.findUserProfileByPkUser(user);

        if (botState.equals(BotState.ASK_ARM)) {
            if(userAnswerIsCorrect(usersAnswer)) {
                if (bodyParamRepository.findBodyParamByUser(userProfile.getPk().getUser()) != null) {
                    BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
                    bodyParam.setNeck(usersAnswer);
                    bodyParamRepository.save(bodyParam);
                }
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askArm");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_CHEST);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askNeck");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ARM);
            }
        }
        if (botState.equals(BotState.ASK_CHEST)) {
            if(userAnswerIsCorrect(usersAnswer)) {
                if (bodyParamRepository.findBodyParamByUser(userProfile.getPk().getUser()) != null) {
                    BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
                    bodyParam.setArm(usersAnswer);
                    bodyParamRepository.save(bodyParam);
                }
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askChest");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_WAIST);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askArm");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_CHEST);
            }
        }
        if (botState.equals(BotState.ASK_WAIST)) {
            if(userAnswerIsCorrect(usersAnswer)) {
                if (bodyParamRepository.findBodyParamByUser(userProfile.getPk().getUser()) != null) {
                    BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
                    bodyParam.setChest(usersAnswer);
                    bodyParamRepository.save(bodyParam);
                }
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askWaist");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_BELLY);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askChest");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_WAIST);
            }

        }
        if (botState.equals(BotState.ASK_BELLY)) {
            if(userAnswerIsCorrect(usersAnswer)) {
                if (bodyParamRepository.findBodyParamByUser(userProfile.getPk().getUser()) != null) {
                    BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
                    bodyParam.setWaist(usersAnswer);
                    bodyParamRepository.save(bodyParam);
                }
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askBelly");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_HIPS);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askWaist");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_BELLY);
            }

        }
        if (botState.equals(BotState.ASK_HIPS)) {
            if(userAnswerIsCorrect(usersAnswer)) {
                if (bodyParamRepository.findBodyParamByUser(userProfile.getPk().getUser()) != null) {
                    BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
                    bodyParam.setStomach(usersAnswer);
                    bodyParamRepository.save(bodyParam);
                }
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askThighs");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_HIP);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askBelly");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_HIPS);
            }
        }
        if (botState.equals(BotState.ASK_HIP)) {
            if(userAnswerIsCorrect(usersAnswer)) {
                if (bodyParamRepository.findBodyParamByUser(userProfile.getPk().getUser()) != null) {
                    BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
                    bodyParam.setHips(usersAnswer);
                    bodyParamRepository.save(bodyParam);
                }
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askThigh");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_SHIN);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askThighs");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_HIP);
            }
        }
        if (botState.equals(BotState.ASK_SHIN)) {
            if(userAnswerIsCorrect(usersAnswer)) {
                if (bodyParamRepository.findBodyParamByUser(userProfile.getPk().getUser()) != null) {
                    BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
                    bodyParam.setHip(usersAnswer);
                    bodyParamRepository.save(bodyParam);
                }
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askShin");
                userDataCache.setUsersCurrentBotState(userId, BotState.PERSONAL_INFO_FILLED);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askThigh");
                userDataCache.setUsersCurrentBotState(userId, BotState.ASK_SHIN);
            }
        }
        if (botState.equals(BotState.PERSONAL_INFO_FILLED)) {
            if(userAnswerIsCorrect(usersAnswer)) {
                Date date = new Date();
                SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
                if (bodyParamRepository.findBodyParamByUser(userProfile.getPk().getUser()) != null) {
                    BodyParam bodyParam = bodyParamRepository.findBodyParamByUser(user);
                    bodyParam.setShin(usersAnswer);
                    bodyParam.setDate(formatForDateNow.format(date));
                    bodyParamRepository.save(bodyParam);
                }
                replyToUser = messagesService.getReplyMessage(chatId, "reply.personalInfoFilled");
                userDataCache.setUsersCurrentBotState(userId, BotState.MAIN_MENU);
            } else {
                replyToUser = messagesService.getReplyMessage(chatId, "reply.askShin");
                userDataCache.setUsersCurrentBotState(userId, BotState.PERSONAL_INFO_FILLED);
            }
        }

        return replyToUser;
    }

    private boolean userAnswerIsCorrect(String userAnswer) {
        return userAnswer.matches("[-+]?[0-9]*(\\.|,|)?[0-9]+([eE][-+]?[0-9]+)?");
    }

}
