package com.example.fitnessMarathonBot.fitnessDB.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "meal_plan")
public class MealPlan implements Serializable {
    @Id
    @Column(name = "meal_plan_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "plan_one")
    private String planOne;

    @Column(name = "plan_two")
    private String planTwo;

    @Column(name = "plan_three")
    private String planThree;

    @Column(name = "category")
    private String category;

    @Column(name = "day_number")
    private String dayNumber;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
}
