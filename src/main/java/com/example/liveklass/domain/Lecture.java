package com.example.liveklass.domain;

import com.sun.jdi.ClassType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    @JoinColumn(name = "creator_id", nullable = false)
    private Member creator;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer maxCapacity;

    private Integer currentUserCount = 0;

    @Column(nullable = false)
    private Long basePrice;

    private LocalDateTime salesStartAt;
    private LocalDateTime salesEndAt;
    private LocalDateTime classStartAt;
    private LocalDateTime classEndAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LectureType classType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LectureStatus classStatus = LectureStatus.DRAFT;

}
