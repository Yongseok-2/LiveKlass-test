package com.example.liveklass.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public void increaseViewCount() {
        this.viewCount++;
    }
}
