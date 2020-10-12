package com.example.fitnessMarathonBot.fitnessDB.bean;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "body_param")
public class BodyParam implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userBodyParam_id")
    private long id;

    private String hip;
    private String shin;
    private String waist;
    private String stomach;
    private String height;
    private String weight;
    private String chest;
    private String neck;
    private String arm;
    private String hips;
    private String date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.bodyParam", cascade=CascadeType.ALL)
//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    private Set<UserProfile> userProfiles;
}
