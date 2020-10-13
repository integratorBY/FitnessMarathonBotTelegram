package com.example.fitnessMarathonBot.botapi.admin.menu;

import com.example.fitnessMarathonBot.bean.UserProfileData;
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
    private BodyParamRepositoryImpl bodyParamRepository;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserProfileImpl userProfile;

    public ParticipantsAndResultHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        String users = "Список клиентов: \n\n";
        if (userProfile.findAll().size() != 0) {
            User user = null;
            BodyParam bodyParam = null;
            List<UserProfile> userProfiles = userProfile.findAll();
            if (userProfiles.size() != 0) {
                for (int i = 0; i < userProfiles.size(); i++) {
                    user = userProfiles.get(i).getPk().getUser();
                    bodyParam = bodyParamRepository.findBodyParamByUser(user);
                    users = users.concat(i + 1 + ". " + userProfiles.get(i).getFullName() + ".\n");
                    users = users.concat("  - Возраст: " + userProfiles.get(i).getUserAge() + ".\n");
                    users = users.concat("  - Рост: " + bodyParam.getHeight() + ".\n");
                    users = users.concat("  - Вес: " + bodyParam.getWeight() + ".\n");
                    users = users.concat("  - Фото(тело): " + userProfileService.counterUserPhotoBody(user.getChatId()) + " из 3.\n");
                    users = users.concat("  - Фото(весы): " + userProfileService.counterUserPhotoWeigher(user.getChatId()) + " из 2.\n\n");
                }
            }

        }
        userDataCache.setUsersCurrentBotState(userId, BotState.OPEN_CUSTOMER_INFO);
        return new SendMessage(message.getChatId(), users + "\n\nВведите номер клиента чтобы открыть профиль: ");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.VIEW_PARTICIPANTS_ADN_RESULTS;
    }

}
