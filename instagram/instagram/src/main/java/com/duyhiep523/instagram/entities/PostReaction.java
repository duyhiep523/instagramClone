package com.duyhiep523.instagram.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_reaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostReaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "reaction_id", length = 50, nullable = false, unique = true)
    private String reactionId;
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
