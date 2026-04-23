package com.example.liveklass.dto.lecture;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VodCreateRequest(

        @NotNull(message = "강의 id는 필수입니다.")
        @Schema(description = "해당 영상이 등록될 강의의 id", example = "1")
        Long lectureId,

        @NotBlank(message = "영상 제목은 필수입니다.")
        @Schema(description = "영상의 제목입니다.", example = "Spring Boot 기초 1")
        String title,

        @NotBlank(message = "영상 설명은 필수입니다.")
        @Schema(description = "영상의 설명입니다.", example = "Spring Boot 기초 1 영상입니다.")
        String description
) {
}
