package com.example.liveklass.dto.lecture;

import com.example.liveklass.domain.LectureStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

public record LectureUpdateRequest(

        @NotBlank(message = "강의 제목은 필수입니다.")
        @Schema(description = "강의 제목", example = "Spring Boot 입문")
        String title,

        @Schema(description = "강의 설명", example = "Spring Boot 입문 강의입니다.")
        String description,

        @PositiveOrZero(message = "0이면 제한 없음 / 최소 수강 인원은 1명 이상이어야 합니다.")
        @Schema(description = "수강 정원 / 제한없다면 0", example = "30")
        Integer maxCapacity,

        @PositiveOrZero(message = "가격은 0원 이상이어야 합니다. / 무료면 0")
        @NotNull(message = "가격은 필수입니다.")
        @Schema(description = "강의 가격 / 무료면 0", example = "30000")
        Long basePrice,

        @NotNull(message = "강의 오픈 여부는 필수입니다.(DRAFT = 초안, OPEN = 즉시 공개)")
        LectureStatus lectureStatus,

        @Schema(description = "판매 시작 날짜", example = "2026-05-01T09:00:00")
        LocalDateTime salesStartAt,

        @Schema(description = "판매 종료 날짜", example = "2026-05-05T09:00:00")
        LocalDateTime salesEndAt,

        @Schema(description = "강의 시작 날짜", example = "2026-05-05T09:00:00")
        LocalDateTime lectureStartAt,

        @Schema(description = "강의 종료 날짜", example = "2026-05-10T09:00:00")
        LocalDateTime lectureEndAt
){
}