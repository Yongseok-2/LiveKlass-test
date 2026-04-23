package com.example.liveklass.dto.lecture;

import com.example.liveklass.domain.Lecture;
import com.example.liveklass.domain.LectureType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record LectureListDto(

        @Schema(description = "강의 id", example = "1")
        Long id,

        @Schema(description = "강사 이름", example = "홍길동")
        String creatorName,

        @Schema(description = "강의 제목", example = "Spring Boot 입문")
        String title,

        @Schema(description = "강의 유형(LIVE, VOD)", example = "VOD")
        LectureType lectureType,

        @Schema(description = "현재 수강 인원", example = "15")
        Integer currentEnrollmentCount,

        @Schema(description = "최대 수강 정원, 0이면 제한 없음", example = "30")
        Integer maxCapacity,

        @Schema(description = "기본 가격", example = "50000")
        Long basePrice,

        @Schema(description = "강의 생성일", example = "2026-05-01T09:00:00")
        LocalDateTime createdAt,

        @Schema(description = "판매 시작일", example = "2026-05-01T09:00:00")
        LocalDateTime salesStartAt,

        @Schema(description = "판매 종료일", example = "2026-05-05T09:00:00")
        LocalDateTime salesEndAt,

        @Schema(description = "강의 시작일", example = "2026-05-05T09:00:00")
        LocalDateTime lectureStartAt,

        @Schema(description = "강의 종료일", example = "2026-05-10T09:00:00")
        LocalDateTime lectureEndAt

) {
    public static LectureListDto from(Lecture lecture) {
        return new LectureListDto(
                lecture.getId(),
                lecture.getCreator().getName(),
                lecture.getTitle(),
                lecture.getLectureType(),
                lecture.getCurrentEnrollmentCount(),
                lecture.getMaxCapacity(),
                lecture.getBasePrice(),
                lecture.getCreatedAt(),
                lecture.getSalesStartAt(),
                lecture.getSalesEndAt(),
                lecture.getLectureStartAt(),
                lecture.getLectureEndAt()
        );
    }
}
