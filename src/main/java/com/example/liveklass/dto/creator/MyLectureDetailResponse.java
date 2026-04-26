package com.example.liveklass.dto.creator;

import com.example.liveklass.domain.Lecture;
import com.example.liveklass.domain.LectureStatus;
import com.example.liveklass.domain.LectureType;
import com.example.liveklass.dto.global.PagedResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public record MyLectureDetailResponse(

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

        @Schema(description = "현재 수강 인원 리스트 (페이징)")
        PagedResponse<CurrentEnrollmentListDto> enrollmentList,

        @Schema(description = "최대 수강 정원, 0이면 제한 없음", example = "30")
        Integer maxCapacity,

        @Schema(description = "신청 대기 인원", example = "15")
        Integer waitCount,

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
    public static MyLectureDetailResponse from(Lecture lecture, Page<CurrentEnrollmentListDto> enrollmentList) {
        return new MyLectureDetailResponse(
                lecture.getTitle(),
                lecture.getDescription(),
                lecture.getLectureStatus(),
                lecture.getLectureType(),
                lecture.getCurrentEnrollmentCount(),
                PagedResponse.from(enrollmentList),
                lecture.getMaxCapacity(),
                lecture.getWaitCount(),
                lecture.getBasePrice(),
                lecture.getCreatedAt(),
                lecture.getSalesStartAt(),
                lecture.getSalesEndAt(),
                lecture.getLectureStartAt(),
                lecture.getLectureEndAt()
        );
    }
}