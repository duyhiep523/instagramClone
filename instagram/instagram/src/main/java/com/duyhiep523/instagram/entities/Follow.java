package com.duyhiep523.instagram.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "follow", uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Follow extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "follow_id", length = 50, nullable = false, unique = true)
    private String followId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", referencedColumnName = "user_id", nullable = false)
    private User follower; // Người theo dõi

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", referencedColumnName = "user_id", nullable = false)
    private User following; // Người được theo dõi
}
