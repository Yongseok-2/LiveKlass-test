package com.example.liveklass.dto.creator;

import com.example.liveklass.domain.Lecture;
import com.example.liveklass.domain.LectureStatus;
import com.example.liveklass.domain.LectureType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record MyLectureListDto(

        @Schema(description = "강의 id", example = "1")
        Long id,

        @Schema(description = "강의 제목", example = "Spring Boot 입문")
        String title,

        @Schema(description = "강사 이름", example = "홍길동")
        String creatorName,

        @Schema(description = "강의 상태", example = "OPEN")
        LectureStatus lectureStatus,

        @Schema(description = "강의 유형(LIVE, VOD)", example = "VOD")
        LectureType lectureType,

        @Schema(description = "현재 수강 인원", example = "15")
        Integer currentEnrollmentCount,

        @Schema(description = "최대 수강 정원, 0이면 제한 없음", example = "30")
        Integer maxCapacity,

        @Schema(description = "기본 가격", example = "50000")
        Long basePrice,

        @Schema(description = "강의 생성일", example = "2026-05-01T09:00:00")
        LocalDateTime createdAt
) {
    public static MyLectureListDto from(Lecture lecture) {
        return new MyLectureListDto(
                lecture.getId(),
                lecture.getTitle(),
                lecture.getCreator().getName(),
                lecture.getLectureStatus(),
                lecture.getLectureType(),
                lecture.getCurrentEnrollmentCount(),
                lecture.getMaxCapacity(),
                lecture.getBasePrice(),
                lecture.getCreatedAt()
        );
    }
}