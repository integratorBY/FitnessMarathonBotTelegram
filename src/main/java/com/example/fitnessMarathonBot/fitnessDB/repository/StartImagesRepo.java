package com.example.fitnessMarathonBot.fitnessDB.repository;

import com.example.fitnessMarathonBot.fitnessDB.bean.StartImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StartImagesRepo extends JpaRepository<StartImages, Long> {
    StartImages findStartImagesByImageName(String path);
}
