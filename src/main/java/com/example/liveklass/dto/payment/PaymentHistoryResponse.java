package com.example.liveklass.dto.payment;

import com.example.liveklass.domain.EnrollmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record PaymentHistoryResponse(

        @Schema(description = "신청(결제) 고유 번호", example = "1")
        Long enrollmentId,

        @Schema(description = "결제된 강의 제목", example = "Spring Boot 입문")
        String lectureTitle,

        @Schema(description = "결제 금액", example = "30000")
        Long paidAmount,

        @Schema(description = "결제 상태 (CONFIRMED - 결제 완료, CANCELLED - 결제 취소)", example = "CONFIRMED")
        EnrollmentStatus status,

        @Schema(description = "결제 완료 일시", example = "2026-05-01T09:00:00")
        LocalDateTime paymentAt,

        @Schema(description = "환불 기한", example = "2026-05-08T09:00:00")
        LocalDateTime refundDeadline
) {
}
