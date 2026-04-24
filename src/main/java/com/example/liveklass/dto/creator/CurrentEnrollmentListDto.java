package com.example.liveklass.dto.creator;

import com.example.liveklass.domain.Enrollment;
import com.example.liveklass.domain.Lecture;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CurrentEnrollmentListDto(

        @Schema(description = "신청 아이디", example = "1")
        Long id,

        @Schema(description = "수강자 이름", example = "수강자1")
        String member_name,

        @Schema(description = "결제(확정) 일자", example = "2026-05-01T09:00:00")
        LocalDateTime paymentAt
) {
    public static CurrentEnrollmentListDto from(Enrollment enrollment) {
        return new CurrentEnrollmentListDto(
                enrollment.getId(),
                enrollment.getMember().getName(),
                enrollment.getPaymentAt()
        );
    }
}
