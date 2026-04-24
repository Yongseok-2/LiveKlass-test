package com.example.liveklass.dto.global;

import io.swagger.v3.oas.annotations.media.Schema;

public record ApiResponse<T>(
        @Schema(description = "성공 여부", example = "true") boolean success,

        @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.") String message,

        @Schema(description = "응답 데이터") T data) {
    // 성공 응답 (데이터가 있는 경우)
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "요청이 성공적으로 처리되었습니다.", data);
    }

    // 성공 응답 (데이터가 없는 경우)
    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(true, "요청이 성공적으로 처리되었습니다.", null);
    }

    // 실패 응답 (데이터가 없는 경우)
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, message, null);
    }
}