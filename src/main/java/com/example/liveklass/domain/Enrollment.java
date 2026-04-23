package com.example.liveklass.domain;

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

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status = EnrollmentStatus.PENDING;

    private LocalDateTime paymentAt;

    private LocalDateTime refundDeadline;

    public boolean canRefund(LocalDateTime requestTime) {

        if (this.refundDeadline == null) {
            return false;
        }

        return !requestTime.isAfter(this.refundDeadline);
    }

    public void confirmEnrollment(LocalDateTime paymentTime, Long paidAmount) {

        if(this.status != EnrollmentStatus.PENDING) {
            // TODO: 결제 대기 상태에서만 확정가능합니다.
        }

        this.status = EnrollmentStatus.CONFIRMED;
        this.paidAmount = paidAmount;
        this.paymentAt = paymentTime;
        // 결제일로부터 7일간 환불 가능
        this.refundDeadline = paymentTime.plusDays(7);
    }

    public void cancel(LocalDateTime requestTime) {

        if(this.status == EnrollmentStatus.CANCELLED) {
            // TODO: 이미 취소된 수강입니다.
        }

        if(this.status == EnrollmentStatus.CONFIRMED && !canRefund(requestTime)) {
            // TODO: 환불 가능기간이 지났습니다.
        }

        this.status = EnrollmentStatus.CANCELLED;
    }
}
