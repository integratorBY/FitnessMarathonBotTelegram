package com.example.fitnessMarathonBot.bean;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Данные анкеты пользователя
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileData {
    String name;
    int height;
    int weight;
    int age;
    String physique;

    @Override
    public String toString() {
        return String.format("%s%nИмя %s%nВозраст %s%nВес %s%nРост" +
                        " %s%nТелосложение %s",
                "Данные по вашему заказу", getName(), getAge(), getWeight(), getHeight(), getPhysique());
    }
}
