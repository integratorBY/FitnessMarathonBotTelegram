package com.example.fitnessMarathonBot.fitnessDB.repository;

import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserPhotoWeigher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPhotoWeigherRepo extends JpaRepository<UserPhotoWeigher, Long> {
    UserPhotoWeigher findUserPhotoWeigherByTimeStampAndUser(String timeStamp, User user);
}
