package com.example.liveklass.dto.auth;

import com.example.liveklass.domain.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignUpRequest(

        @NotBlank(message = "아이디는 필수입니다.")
        @Schema(description = "아이디", example = "user1")
        String userName,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Schema(description = "비밀번호", example = "1234")
        String password,

        @NotBlank(message = "닉네임은 필수입니다")
        @Schema(description = "닉네임", example = "홍길동")
        String name,

        @NotNull(message = "역할은 필수입니다 (STUDENT 또는 CREATOR)")
        @Schema(description = "역할", example = "STUDENT")
        MemberRole role
) {
}
