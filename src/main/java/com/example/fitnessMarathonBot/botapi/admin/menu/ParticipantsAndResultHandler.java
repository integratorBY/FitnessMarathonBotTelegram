package com.example.fitnessMarathonBot.botapi.admin.menu;

import com.example.fitnessMarathonBot.bean.Bot;
import com.example.fitnessMarathonBot.bean.UserProfileData;
import com.example.fitnessMarathonBot.botapi.BotState;
import com.example.fitnessMarathonBot.botapi.InputMessageHandler;
import com.example.fitnessMarathonBot.cache.UserDataCache;
import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserProfile;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserProfileImpl;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
public class ParticipantsAndResultHandler implements InputMessageHandler {
    private UserDataCache userDataCache;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private UserProfileImpl userProfile;

    public ParticipantsAndResultHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        final UserProfileData profileData = userDataCache.getUserProfileData(userId);
        String users = "Список клиентов: \n\n";
        if (userProfile.findAll().size() != 0) {
//            List<User> userList = userRepository.findAll();
            List<UserProfile> userProfiles = userProfile.findAll();
            for (int i = 0; i < userProfiles.size(); i++) {
                users = users.concat(i + 1 + ". " + userProfiles.get(i).getFullName() + ".\n");
            }

        }
        userDataCache.setUsersCurrentBotState(userId, BotState.OPEN_CUSTOMER_INFO);
        return new SendMessage(message.getChatId(), users + "\n\nВведите норме клиента чтобы открить профиль: ");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.VIEW_PARTICIPANTS_ADN_RESULTS;
    }

}
