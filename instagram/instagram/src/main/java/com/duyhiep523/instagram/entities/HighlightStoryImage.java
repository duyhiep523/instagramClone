package com.duyhiep523.instagram.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "highlight_story_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HighlightStoryImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "image_id", length = 50, nullable = false, unique = true)
    private String imageId;

    @ManyToOne
    @JoinColumn(name = "story_id", nullable = false)
    private HighlightStory highlightStory;

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
