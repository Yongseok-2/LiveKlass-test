package com.example.liveklass.dto.enrollment;

import com.example.liveklass.domain.Enrollment;
import com.example.liveklass.domain.EnrollmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record MyEnrollmentListDto(

        @Schema(description = "수강 신청 ID")
        Long enrollmentId,

        @Schema(description = "강의 제목")
        String title,

        @Schema(description = "강사 이름")
        String creatorName,

        @Schema(description = "수강 신청 상태 (PENDING, COMPLETED, CANCELED)")
        EnrollmentStatus status,

        @Schema(description = "수강 신청 일시")
        LocalDateTime enrolledAt,

        @Schema(description = "강의 시작일")
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