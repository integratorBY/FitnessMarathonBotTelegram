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

}

