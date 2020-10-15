package com.example.fitnessMarathonBot.fitnessDB.bean;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Data
@Table(name = "photo_weigher")
public class UserPhotoWeigher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_weigher_id")
    private long id;

    @Column(name = "time_stamp")
    private String timeStamp;

    @Column(name = "photo_w_id")
    private String photo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
