package com.example.liveklass.dto.creator;

import com.example.liveklass.domain.Lecture;
import com.example.liveklass.domain.LectureStatus;
import com.example.liveklass.domain.LectureType;
import com.example.liveklass.dto.lecture.VodListDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record MyLectureDetailResponse(

        @Schema(description = "강의에 있는 vod 목록")
        List<VodListDto> vodList,

        @Schema(description = "강의 제목", example = "Spring Boot 입문")
        String title,

        @Schema(description = "강의 설명", example = "Spring Boot 입문 강의입니다.")
        String description,

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
    public static MyLectureDetailResponse from(Lecture lecture) {
        return new MyLectureDetailResponse(
                lecture.getVods().stream()
                        .map(VodListDto::from)
                        .toList(),
                lecture.getTitle(),
                lecture.getDescription(),
                lecture.getLectureStatus(),
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