package com.example.fitnessMarathonBot.fitnessDB.service;

import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserPhoto;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserPhotoRepository;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class UserPhotoService {
    @Autowired
    private UserPhotoRepository userPhotoRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    public int saveUserPhoto(Message message) {
        if (userRepository.findUserByChatId(message.getFrom().getId()) != null) {
            User user = userRepository.findUserByChatId(message.getFrom().getId());
            Date date = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
            String currentDate = formatForDateNow.format(date);
            UserPhoto userPhotos = userPhotoRepository.findUserPhotoByTimeStampAndUser(
                    currentDate, user);
            List<PhotoSize> photos = message.getPhoto();
            String photo_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                    .orElse(null)).getFileId();
            if (userPhotos != null) {

                if (userPhotos.getPhotoOne() == null) {
                    userPhotos.setPhotoOne(photo_id);
                    userPhotos.setImageCategory("eat");
                    userPhotos.setTimeStamp(currentDate);
                    userPhotoRepository.save(userPhotos);
                    return 0;
                }
                if (userPhotos.getPhotoTwo() == null) {
                    userPhotos.setPhotoTwo(photo_id);
                    userPhotos.setTimeStamp(currentDate);
                    userPhotoRepository.save(userPhotos);
                    return 1;
                }
                if (userPhotos.getPhotoThree() == null) {
                    userPhotos.setPhotoThree(photo_id);
                    userPhotos.setTimeStamp(currentDate);
                    userPhotoRepository.save(userPhotos);
                    return 2;
                }
                return 2;
            } else {
                UserPhoto userPhoto = UserPhoto.builder()
                        .user(user)
                        .imageCategory("eat")
                        .photoOne(photo_id)
                        .timeStamp(currentDate)
                        .build();
                userPhotoRepository.save(userPhoto);
                return 0;
            }
        }
        return 2;
    }

    public int getCountUserPhotos(long chatId) {
        if (userRepository.findUserByChatId(chatId) != null) {
            User user = userRepository.findUserByChatId(chatId);
            Date date = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
            UserPhoto userPhotos = userPhotoRepository.findUserPhotoByTimeStampAndUser(
                    formatForDateNow.format(date), user);
            if (userPhotos != null) {
                if (userPhotos.getPhotoOne() == null) {
                    return 0;
                }
                if (userPhotos.getPhotoTwo() == null) {
                    return 1;
                }
                if (userPhotos.getPhotoThree() == null) {
                    return 2;
                } else {
                    return 3;
                }
            }
        }
        return 0;
    }

    public int getCountPhotos(UserPhoto userPhotos) {
        if (userPhotos != null) {
            if (userPhotos.getPhotoOne() == null) {
                return 0;
            }
            if (userPhotos.getPhotoTwo() == null) {
                return 1;
            }
            if (userPhotos.getPhotoThree() == null) {
                return 2;
            } else {
                return 3;
            }
        }
        return 0;
    }

}
