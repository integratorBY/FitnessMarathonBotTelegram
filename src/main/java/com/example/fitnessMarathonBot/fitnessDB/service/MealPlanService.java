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

    private String countDayCategoryOne;
    private String countDayCategoryTwo;
    private String countDayCategoryThree;
    private String daysFoodBasket;

    public void saveMealPlanOneCategory(Message message) {
        List<PhotoSize> photos = message.getPhoto();
        String photo_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null)).getFileId();
        if (mealPlanRepository.findMealPlanByCategoryAndDayNumber("small", countDayCategoryOne) != null) {
            MealPlan mealPlan = mealPlanRepository.findMealPlanByCategoryAndDayNumber("small", countDayCategoryOne);
            if (mealPlan.getPlanOne() == null) {
                mealPlan.setPlanOne(photo_id);
                mealPlanRepository.save(mealPlan);
                return;
            }
            if (mealPlan.getPlanTwo() == null) {
                mealPlan.setPlanTwo(photo_id);
                mealPlanRepository.save(mealPlan);
                return;
            }
            if (mealPlan.getPlanThree() == null) {
                mealPlan.setPlanThree(photo_id);
                mealPlanRepository.save(mealPlan);
            }
        } else {
            MealPlan mealPlan = MealPlan.builder()
                    .planOne(photo_id)
                    .category("small")
                    .dayNumber(countDayCategoryOne)
                    .build();
            mealPlanRepository.save(mealPlan);
        }
    }

    public void saveMealPlanTwoCategory(Message message) {
        List<PhotoSize> photos = message.getPhoto();
        String photo_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null)).getFileId();
        if (mealPlanRepository.findMealPlanByCategoryAndDayNumber("middle", countDayCategoryTwo) != null) {
            MealPlan mealPlan = mealPlanRepository.findMealPlanByCategoryAndDayNumber("middle", countDayCategoryTwo);
            if (mealPlan.getPlanOne() == null) {
                mealPlan.setPlanOne(photo_id);
                mealPlanRepository.save(mealPlan);
                return;
            }
            if (mealPlan.getPlanTwo() == null) {
                mealPlan.setPlanTwo(photo_id);
                mealPlanRepository.save(mealPlan);
                return;
            }
            if (mealPlan.getPlanThree() == null) {
                mealPlan.setPlanThree(photo_id);
                mealPlanRepository.save(mealPlan);
            }
        } else {
            MealPlan mealPlan = MealPlan.builder()
                    .planOne(photo_id)
                    .category("middle")
                    .dayNumber(countDayCategoryTwo)
                    .build();
            mealPlanRepository.save(mealPlan);
        }
    }

    public void saveMealPlanThreeCategory(Message message) {
        List<PhotoSize> photos = message.getPhoto();
        String photo_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null)).getFileId();
        if (mealPlanRepository.findMealPlanByCategoryAndDayNumber("big", countDayCategoryThree) != null) {
            MealPlan mealPlan = mealPlanRepository.findMealPlanByCategoryAndDayNumber("big", countDayCategoryThree);
            if (mealPlan.getPlanOne() == null) {
                mealPlan.setPlanOne(photo_id);
                mealPlanRepository.save(mealPlan);
                return;
            }
            if (mealPlan.getPlanTwo() == null) {
                mealPlan.setPlanTwo(photo_id);
                mealPlanRepository.save(mealPlan);
                return;
            }
            if (mealPlan.getPlanThree() == null) {
                mealPlan.setPlanThree(photo_id);
                mealPlanRepository.save(mealPlan);
            }
        } else {
            MealPlan mealPlan = MealPlan.builder()
                    .planOne(photo_id)
                    .category("big")
                    .dayNumber(countDayCategoryThree)
                    .build();
            mealPlanRepository.save(mealPlan);
        }
    }

    public void saveMealPlanFoodBasket(Message message) {
        List<PhotoSize> photos = message.getPhoto();
        String photo_id = Objects.requireNonNull(photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null)).getFileId();
        if (mealPlanRepository.findMealPlanByCategoryAndDayNumber("foodBasket", daysFoodBasket) != null) {
            MealPlan mealPlan = mealPlanRepository.findMealPlanByCategoryAndDayNumber("foodBasket", daysFoodBasket);
            if (mealPlan.getPlanOne() != null) {
                mealPlan.setPlanOne(photo_id);
                mealPlanRepository.save(mealPlan);
            }
        } else {
            MealPlan mealPlan = MealPlan.builder()
                    .planOne(photo_id)
                    .category("foodBasket")
                    .dayNumber(daysFoodBasket)
                    .build();
            mealPlanRepository.save(mealPlan);
        }
    }
}
