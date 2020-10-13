package com.example.fitnessMarathonBot.fitnessDB.service;

import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserProfile;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserProfileImpl;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileImpl userProfileRepo;

    @Autowired
    private UserRepositoryImpl userRepository;

    public void saveUserProfilesStartPhotoBody(Message message) {
        User user = userRepository.findUserByChatId(message.getChatId());
        UserProfile userProfile = userProfileRepo.findUserProfileByPkUser(user);
        List<PhotoSize> photos = message.getPhoto();
        String photo_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null)).getFileId();
        if (userProfile.getPhotoId_1() == null) {
            userProfile.setPhotoId_1(photo_id);
            userProfileRepo.save(userProfile);
            return;
        }
        if (userProfile.getPhotoId_2() == null) {
            userProfile.setPhotoId_2(photo_id);
            userProfileRepo.save(userProfile);
            return;
        }
        if (userProfile.getPhotoId_3() == null) {
            userProfile.setPhotoId_3(photo_id);
            userProfileRepo.save(userProfile);
        }
    }

    public void saveUserProfilesStartPhotoWeigher(Message message) {
        User user = userRepository.findUserByChatId(message.getChatId());
        UserProfile userProfile = userProfileRepo.findUserProfileByPkUser(user);
        List<PhotoSize> photos = message.getPhoto();
        String photo_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null)).getFileId();
        if (userProfile.getPhotoId_4() == null) {
            userProfile.setPhotoId_4(photo_id);
            userProfileRepo.save(userProfile);
            return;
        }
        if (userProfile.getPhotoId_5() == null) {
            userProfile.setPhotoId_5(photo_id);
            userProfileRepo.save(userProfile);
            return;
        }
    }

    public int counterUserPhotoBody(long chatId) {
        int quantity = 0;
        User user = userRepository.findUserByChatId(chatId);
        if (user != null) {
            UserProfile userProfile = userProfileRepo.findUserProfileByPkUser(user);
            if (userProfile != null) {
                if (userProfile.getPhotoId_1() != null){
                    quantity++;
                }
                if (userProfile.getPhotoId_2() != null){
                    quantity++;
                }
                if (userProfile.getPhotoId_3() != null){
                    quantity++;
                }
            }
        }
        return quantity;
    }

    public int counterUserPhotoWeigher(long chatId) {
        int quantity = 0;
        User user = userRepository.findUserByChatId(chatId);
        if (user != null) {
            UserProfile userProfile = userProfileRepo.findUserProfileByPkUser(user);
            if (userProfile != null) {
                if (userProfile.getPhotoId_4() != null){
                    quantity++;
                }
                if (userProfile.getPhotoId_5() != null){
                    quantity++;
                }
            }
        }
        return quantity;
    }
}
