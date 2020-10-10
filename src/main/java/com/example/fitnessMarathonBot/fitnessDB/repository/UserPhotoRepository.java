package com.example.fitnessMarathonBot.fitnessDB.repository;

import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import com.example.fitnessMarathonBot.fitnessDB.bean.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {

    List<UserPhoto> findUserPhotoByUser(User user);

    UserPhoto findUserPhotoByTimeStampAndUser(String timeStamp, User user);
}
