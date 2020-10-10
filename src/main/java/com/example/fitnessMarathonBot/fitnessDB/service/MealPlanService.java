package com.example.fitnessMarathonBot.fitnessDB.service;

import com.example.fitnessMarathonBot.fitnessDB.bean.MealPlan;
import com.example.fitnessMarathonBot.fitnessDB.repository.MealPlanRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Getter
@Setter
public class MealPlanService {
    @Autowired
    private MealPlanRepository mealPlanRepository;

    private int countDayCategoryOne;
    private int countDayCategoryTwo;
    private int countDayCategoryThree;
    private String daysFoodBasket;

    public void saveMealPlanOneCategory(Message message) {
        List<PhotoSize> photos = message.getPhoto();
        String photo_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null)).getFileId();
        MealPlan mealPlan = MealPlan.builder()
                .plan(photo_id)
                .category("small")
                .dayNumber(countDayCategoryOne + "")
                .build();
        mealPlanRepository.save(mealPlan);
    }

    public void saveMealPlanTwoCategory(Message message) {
        List<PhotoSize> photos = message.getPhoto();
        String photo_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null)).getFileId();
        MealPlan mealPlan = MealPlan.builder()
                .plan(photo_id)
                .category("middle")
                .dayNumber(countDayCategoryTwo + "")
                .build();
        mealPlanRepository.save(mealPlan);
    }

    public void saveMealPlanThreeCategory(Message message) {
        List<PhotoSize> photos = message.getPhoto();
        String photo_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null)).getFileId();
        MealPlan mealPlan = MealPlan.builder()
                .plan(photo_id)
                .category("big")
                .dayNumber(countDayCategoryThree + "")
                .build();
        mealPlanRepository.save(mealPlan);
    }

    public void saveMealPlanFoodBasket(Message message) {
        List<PhotoSize> photos = message.getPhoto();
        String photo_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null)).getFileId();
        MealPlan mealPlan = MealPlan.builder()
                .plan(photo_id)
                .category("foodBasket")
                .dayNumber(daysFoodBasket)
                .build();
        mealPlanRepository.save(mealPlan);
    }
}
