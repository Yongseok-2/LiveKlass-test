package com.example.liveklass.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "아이디는 필수입니다.")
        @Schema(description = "아이디", example = "user1")
        String userName,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Schema(description = "비밀번호", example = "1234")
        String password
) {
}
