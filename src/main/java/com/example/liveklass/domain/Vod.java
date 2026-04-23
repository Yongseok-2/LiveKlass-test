package com.example.liveklass.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE vod SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class Vod extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable =false)
    private Lecture lecture;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private long viewCount = 0;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public void increaseViewCount() {
        this.viewCount++;
    }
}
