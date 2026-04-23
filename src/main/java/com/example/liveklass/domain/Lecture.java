package com.example.liveklass.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Lecture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member creator;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer maxCapacity;

    // TODO: maxCapacity 보다 높으면 안됨
    @Builder.Default
    @Column(nullable = false)
    private Integer currentEnrollmentCount = 0;

    @Column(nullable = false)
    private Long basePrice;

    private LocalDateTime salesStartAt;
    private LocalDateTime salesEndAt;
    private LocalDateTime lectureStartAt;
    private LocalDateTime lectureEndAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LectureType lectureType;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LectureStatus lectureStatus = LectureStatus.DRAFT;

    @Builder.Default
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vod> vods = new ArrayList<>();

}
