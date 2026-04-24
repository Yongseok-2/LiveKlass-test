package com.example.liveklass.dto.lecture;

import com.example.liveklass.domain.Lecture;
import com.example.liveklass.domain.LectureStatus;
import com.example.liveklass.domain.LectureType;
import com.example.liveklass.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

public record LectureCreateRequest (

        @NotNull(message = "강사 id는 필수입니다.")
        @Schema(description = "강사 id", example = "1")
        Long creatorId,

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

        @NotNull(message = "강의 유형은 필수입니다 (LIVE, VOD)")
        LectureType lectureType
){
        public Lecture toEntity(Member creator) {
                return Lecture.builder()
                        .creator(creator)
                        .title(this.title)
                        .description(this.description)
                        .maxCapacity(this.maxCapacity)
                        .basePrice(this.basePrice)
                        .lectureType(this.lectureType)
                        .lectureStatus(LectureStatus.DRAFT)
                        .currentEnrollmentCount(0)
                        .build();
        }
}
