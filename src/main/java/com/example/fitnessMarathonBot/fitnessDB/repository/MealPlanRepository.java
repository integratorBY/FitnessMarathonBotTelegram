package com.example.fitnessMarathonBot.fitnessDB.repository;

import com.example.fitnessMarathonBot.fitnessDB.bean.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {
}
