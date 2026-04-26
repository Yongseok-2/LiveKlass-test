package com.example.liveklass.domain;

import com.example.liveklass.global.error.CustomException;
import com.example.liveklass.global.error.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Enrollment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    private Long paidAmount;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    private LocalDateTime paymentAt;

    private LocalDateTime refundDeadline;

    public void confirmEnrollment(LocalDateTime paymentTime, Long paidAmount) {

        if(this.status != EnrollmentStatus.PENDING) {
            // 결제 대기 상태에서만 결제가 가능합니다.
            throw new CustomException(ErrorCode.NOT_PENDING);
        }

        if(this.lecture.getCurrentEnrollmentCount() >= this.lecture.getMaxCapacity()) {
            throw new CustomException(ErrorCode.CAPACITY_EXCEEDED);
        }

        if (this.lecture.getLectureStatus() != LectureStatus.OPEN) {
            throw new CustomException(ErrorCode.SALE_PERIOD_EXPIRED);
        }

        this.status = EnrollmentStatus.CONFIRMED;
        this.paidAmount = paidAmount;
        this.paymentAt = paymentTime;
        // 결제일로부터 7일간 환불 가능
        this.refundDeadline = paymentTime.plusDays(7);
    }

    public int cancel(LocalDateTime requestTime) {

        if(this.status == EnrollmentStatus.CANCELLED) {
            //이미 취소된 수강 신청입니다.
            throw new CustomException(ErrorCode.ALREADY_CANCELED);
        }

        if(this.status == EnrollmentStatus.CONFIRMED && requestTime.isAfter(this.refundDeadline)) {
            // 환불 가능 기간이 지났습니다.
            throw new CustomException(ErrorCode.REFUND_PERIOD_EXPIRED);
        }

        if(this.status == EnrollmentStatus.CONFIRMED || this.status == EnrollmentStatus.PENDING) {
            this.status = EnrollmentStatus.CANCELLED;
            return 1;
        }else {
            this.status = EnrollmentStatus.CANCELLED;
            return 0;
        }

    }

    public void updateStatus(EnrollmentStatus enrollmentStatus) {
        this.status = enrollmentStatus;
    }
}
