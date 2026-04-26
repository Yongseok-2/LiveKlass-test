package com.example.liveklass.dto.enrollment;

import com.example.liveklass.domain.Enrollment;
import com.example.liveklass.domain.EnrollmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record MyEnrollmentListDto(

        @Schema(description = "수강 신청 ID", example = "1")
        Long enrollmentId,

        @Schema(description = "강의 제목", example = "Spring Boot 입문")
        String title,

        @Schema(description = "강사 이름", example = "홍길동")
        String creatorName,

        @Schema(description = "수강 신청 상태 (PENDING, CONFIRMED, CANCELLED)", example = "CONFIRMED")
        EnrollmentStatus status,

        @Schema(description = "수강 신청 일시", example = "2026-05-01T09:00:00")
        LocalDateTime enrolledAt,

        @Schema(description = "강의 시작일", example = "2026-05-03T09:00:00")
        LocalDateTime lectureStartAt
) {
    public static MyEnrollmentListDto from(Enrollment enrollment) {
    return new MyEnrollmentListDto(
            enrollment.getId(),
            enrollment.getLecture().getTitle(),
            enrollment.getLecture().getCreator().getName(),
            enrollment.getStatus(),
            enrollment.getCreatedAt(),
            enrollment.getLecture().getLectureStartAt()
    );
}
}