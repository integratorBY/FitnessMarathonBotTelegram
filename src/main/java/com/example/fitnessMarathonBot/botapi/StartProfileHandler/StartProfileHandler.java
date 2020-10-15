package com.example.fitnessMarathonBot.botapi.StartProfileHandler;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserProfile;
import com.example.fitnessMarathonBot.fitnessDB.repository.StartImagesRepo;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserProfileImpl;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import com.example.fitnessMarathonBot.service.AdminMainMenuService;
import com.example.fitnessMarathonBot.service.ReplyMessagesService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Getter
@Setter
public class StartProfileHandler implements InputMessageHandler {
    private ReplyMessagesService messagesService;
    private UserDataCache userDataCache;
    private AdminMainMenuService adminMainMenuService;
    private Bot myBot;

    public static boolean isAuthorize = false;

    @Autowired
    private StartImagesRepo startImagesRepo;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private UserProfileImpl userProfileRepository;

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
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage replyToUser = new SendMessage(chatId, " ");
        User userExists = userRepository.findUserByChatId(chatId);
        if (userId == 1331718111 || userId == 748582406) {
            replyToUser = adminMainMenuService.getAdminMainMenuMessage(chatId, "Тут какое то приветствие админа");
        } else {
            if (userExists != null) {
                UserProfile userProfile = userProfileRepository.findUserProfileByPkUser(userExists);
                if (userProfile != null) {
                    myBot.execute(new SendMessage(chatId, "Вы уже участник марафона"));
                }
            } else {
                myBot.execute(new SendMessage(chatId, "Введите пароль: "));
                userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.ASK_PASSWORD);
            }
        }
        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_START;
    }

}
