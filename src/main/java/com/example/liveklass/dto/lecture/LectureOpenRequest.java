package com.example.liveklass.dto.lecture;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record LectureOpenRequest(

        @Schema(description = "판매 시작 날짜", example = "2026-05-01T09:00:00")
        LocalDateTime salesStartAt,

        @Schema(description = "판매 종료 날짜", example = "2026-05-05T09:00:00")
        LocalDateTime salesEndAt,

        @Schema(description = "강의 시작 날짜", example = "2026-05-05T09:00:00")
        LocalDateTime lectureStartAt,

        @Schema(description = "강의 종료 날짜", example = "2026-05-10T09:00:00")
        LocalDateTime lectureEndAt
) {
}
