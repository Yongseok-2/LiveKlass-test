package com.example.liveklass.dto.payment;

import com.example.liveklass.domain.EnrollmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record PaymentHistoryResponse(

        @Schema(description = "결제 고유 번호")
        Long paymentId,

        @Schema(description = "결제된 강의 제목")
        String lectureTitle,

        @Schema(description = "결제 금액")
        Long paidAmount,

        @Schema(description = "결제 상태 (CONFIRMED - 결제 완료, CANCELLED - 결제 취소)")
        EnrollmentStatus status,

        @Schema(description = "결제 완료 일시")
        LocalDateTime paymentAt,

        @Schema(description = "환불 기한")
        LocalDateTime refundDeadline
) {
}
