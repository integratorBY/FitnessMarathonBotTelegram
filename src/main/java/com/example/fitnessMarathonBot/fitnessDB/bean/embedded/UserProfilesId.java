package com.example.fitnessMarathonBot.fitnessDB.bean.embedded;

import com.example.fitnessMarathonBot.fitnessDB.bean.User;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserProfilesId implements Serializable {

    @ManyToOne
    private User user;

}
