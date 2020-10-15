package com.example.fitnessMarathonBot.fitnessDB.service;

import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserPhotoWeigher;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserPhotoWeigherRepo;
import com.example.fitnessMarathonBot.fitnessDB.repository.UserRepositoryImpl;
import com.example.fitnessMarathonBot.service.CurrentDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class UserPhotoWeigherService {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private UserPhotoWeigherRepo userPhotoWeigherRepo;

    public void save(Message message) {
        User user = userRepository.findUserByChatId(message.getChatId());
        UserPhotoWeigher userPhotoWeigher = userPhotoWeigherRepo
                .findUserPhotoWeigherByTimeStampAndUser(CurrentDate.getCurrentDate(), user);
        List<PhotoSize> photos = message.getPhoto();
        String photo_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null)).getFileId();
        if (userPhotoWeigher != null) {
            if (userPhotoWeigher.getPhoto() == null) {
                userPhotoWeigher.setPhoto(photo_id);
                userPhotoWeigherRepo.save(userPhotoWeigher);
            }
        } else {
            UserPhotoWeigher photoWeigher = UserPhotoWeigher.builder()
                    .timeStamp(CurrentDate.getCurrentDate())
                    .user(user)
                    .photo(photo_id)
                    .build();
            userPhotoWeigherRepo.save(photoWeigher);
        }
    }
}
