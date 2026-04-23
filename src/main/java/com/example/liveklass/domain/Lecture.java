package com.example.liveklass.domain;

import com.example.liveklass.global.error.CustomException;
import com.example.liveklass.global.error.ErrorCode;
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

    @Builder.Default
    @Column(nullable = false)
    private Integer currentEnrollmentCount = 0;

    @Column(nullable = false)
    private Long basePrice;

    private LocalDateTime salesStartAt;
    private LocalDateTime salesEndAt;

    @Column(nullable = false)
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

    public void updateDates(LocalDateTime salesStartAt, LocalDateTime salesEndAt, LocalDateTime lectureStartAt, LocalDateTime lectureEndAt) {

        if(lectureStartAt == null) {
            this.lectureStartAt = LocalDateTime.now();
        }else {
            this.lectureStartAt = lectureStartAt;
        }

        if (salesStartAt == null || salesStartAt.isAfter(this.lectureStartAt)) {
            this.salesStartAt = this.lectureStartAt;
        }else {
            this.salesStartAt = salesStartAt;
        }

        this.lectureEndAt = lectureEndAt;

        if (this.lectureEndAt != null) {
            if(salesEndAt == null || salesEndAt.isAfter(this.lectureEndAt)) {
                this.salesEndAt = this.lectureEndAt;
            }else {
                this.salesEndAt = salesEndAt;
            }
        } else {
            this.salesEndAt = salesEndAt;
        }
    }

    public void openLecture() {

        if(this.salesStartAt != null && this.salesEndAt != null && this.salesStartAt.isAfter(this.salesEndAt)) {
                // 판매 시작일은 판매 종료일 이전이어야 합니다.
                throw new CustomException(ErrorCode.INCORRECT_SALE_START_DATE);
        }

        if(this.lectureStartAt != null && this.lectureEndAt != null && this.lectureStartAt.isAfter(this.lectureEndAt)) {
                // 강의 시작일은 강의 종료일 이전이어야 합니다.
                throw new CustomException(ErrorCode.INCORRECT_LECTURE_START_DATE);
        }

        this.lectureStatus = LectureStatus.OPEN;
    }

    public void closeLecture() {
        if (this.lectureStatus == LectureStatus.CLOSED) {
            // 이미 종료된 강의입니다.
            throw new CustomException(ErrorCode.LECTURE_ALREADY_CLOSED);
        }
        this.lectureStatus = LectureStatus.CLOSED;
    }

    public void increaseCurrentEnrollmentCount(LocalDateTime requestTime) {

        if (this.salesStartAt != null && requestTime.isBefore(this.salesStartAt)) {
            // 수강 신청 기간이 아닙니다.
            throw new CustomException(ErrorCode.ENROLLMENT_PERIOD_ENDED);
        }

        if (this.salesEndAt != null && requestTime.isAfter(this.salesEndAt)) {
            // 수강 신청 기간이 아닙니다.
            throw new CustomException(ErrorCode.ENROLLMENT_PERIOD_ENDED);
        }

        if (this.maxCapacity != null && this.maxCapacity > 0 && this.currentEnrollmentCount >= this.maxCapacity) {
            // 수강 정원이 마감되었습니다.
            throw new CustomException(ErrorCode.CAPACITY_EXCEEDED);
        }

        this.currentEnrollmentCount++;
    }

}
