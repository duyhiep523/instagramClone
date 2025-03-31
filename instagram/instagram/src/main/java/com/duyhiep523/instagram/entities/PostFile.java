package com.duyhiep523.instagram.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_file")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "file_id", length = 50, nullable = false, unique = true)
    private String fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String fileUrl;
    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted= false;
}
