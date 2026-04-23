package com.example.liveklass.dto.enrollment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnrollmentRequest(

        @NotNull(message = "강의 아이디는 필수입니다.")
        @Schema(description = "등록한 강의 아이디")
        Long lectureId,

        @NotBlank(message = "사용자 이름은 필수입니다.")
        @Schema(description = "등록한 사용자의 이름")
        String userName

) {
}
