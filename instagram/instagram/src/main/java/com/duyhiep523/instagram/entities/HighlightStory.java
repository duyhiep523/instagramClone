package com.duyhiep523.instagram.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "highlight_story")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HighlightStory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "story_id", length = 50, nullable = false, unique = true)
    private String storyId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "story_name", nullable = false, length = 100)
    private String storyName;


    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "highlightStory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HighlightStoryImage> images;
}
