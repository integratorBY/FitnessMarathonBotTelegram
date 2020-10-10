package com.example.fitnessMarathonBot.fitnessDB.bean;

import com.example.fitnessMarathonBot.fitnessDB.bean.embedded.UserProfilesId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_profile")
@AssociationOverride(name = "pk.bodyParam",
        joinColumns = @JoinColumn(name = "userBodyParam_id"))
@AssociationOverride(name = "pk.user",
        joinColumns = @JoinColumn(name = "user_id"))
public class UserProfile implements Serializable {
    @EmbeddedId
    private UserProfilesId pk;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "user_age")
    private String userAge;

    @Column(name = "days_of_theMarathon")
    private int daysOfTheMarathon;

    @Column(name = "photo_1")
    private String photoId_1;

    @Column(name = "photo_2")
    private String photoId_2;

    @Column(name = "photo_3")
    private String photoId_3;

    @Column(name = "photo_4")
    private String photoId_4;

    @Column(name = "photo_5")
    private String photoId_5;
}

