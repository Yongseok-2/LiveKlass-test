package com.example.liveklass.domain;

import com.example.liveklass.global.error.CustomException;
import com.example.liveklass.global.error.ErrorCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE lecture SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
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

    @NotNull(message = "수강 정원을 입력해주세요.")
    private Integer maxCapacity;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LectureStatus lectureStatus;

    @Builder.Default
    @Column(nullable = false)
    private boolean isDeleted = false;

    public void updateBasicInfo(String title, String description, Integer maxCapacity, Long basePrice, LectureType lectureType) {

        if(maxCapacity < this.currentEnrollmentCount) {
            throw new CustomException(ErrorCode.INVALID_CAPACITY_SETTING);
        }
        this.title = title;
        this.description = description;
        this.maxCapacity = maxCapacity;
        this.basePrice = basePrice;
        this.lectureType = lectureType;
    }

    public void updateDates(LocalDateTime salesStartAt, LocalDateTime salesEndAt, LocalDateTime lectureStartAt, LocalDateTime lectureEndAt) {

        if(lectureStartAt == null) {
            this.lectureStartAt = LocalDateTime.now();
        }else {
            this.lectureStartAt = lectureStartAt;
        }

        this.lectureEndAt = lectureEndAt;
        this.salesStartAt = salesStartAt;

        if (this.lectureEndAt != null) {
            if(salesEndAt == null || salesEndAt.isAfter(this.lectureEndAt)) {
                this.salesEndAt = this.lectureEndAt;
            }else {
                this.salesEndAt = salesEndAt;
            }
        } else {
            this.salesEndAt = salesEndAt;
        }

        validateDate();
    }

    public void validateDate() {

        if (this.salesStartAt != null && this.salesStartAt.isAfter(this.lectureStartAt)) {
            // 판매 시작일은 강의 시작 이전이어야 합니다.
            throw new CustomException(ErrorCode.SALE_START_DATE_AFTER_LECTURE);
        }

        if(this.salesStartAt != null && this.salesEndAt != null && this.salesStartAt.isAfter(this.salesEndAt)) {
            // 판매 시작일은 판매 종료일 이전이어야 합니다.
            throw new CustomException(ErrorCode.INCORRECT_SALE_START_DATE);
        }

        if(this.salesStartAt != null && this.lectureEndAt != null && this.lectureStartAt.isAfter(this.lectureEndAt)) {
            // 강의 시작일은 강의 종료일 이전이어야 합니다.
            throw new CustomException(ErrorCode.INCORRECT_LECTURE_START_DATE);
        }
    }

    public void openLecture() {

        validateDate();
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
