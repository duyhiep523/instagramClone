package com.duyhiep523.instagram.entities;

import com.duyhiep523.instagram.common.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", length = 50, nullable = false, unique = true)
    private String userId;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Builder.Default
    @Column(name = "profile_picture_url", length = 255, nullable = false)
    private String profilePictureUrl= "https://i.pinimg.com/236x/08/35/0c/08350cafa4fabb8a6a1be2d9f18f2d88.jpg";

    @Column(name = "bio", length = 255)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "gender", nullable = false)
    private Gender gender = Gender.OTHER;

    @Column(name = "hometown", length = 255)
    private String hometown;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted= false;

}