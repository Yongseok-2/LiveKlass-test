package com.example.liveklass.document;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface CreatorApiDocument {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Validation Error",
                                    value = """
                {
                  "success": false,
                  "message": "강의 제목은 필수입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "A002", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Unauthorized",
                                    value = """
                {
                  "success": false,
                  "message": "로그인이 필요한 서비스입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "A003", description = "권한 없음 (강사 아님)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Forbidden",
                                    value = """
                {
                  "success": false,
                  "message": "해당 메뉴에 대한 접근 권한이 없습니다.",
                  "data": null
                }
                """)))
    })
    @interface CreateLectureErrorResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Validation Error",
                                    value = """
                {
                  "success": false,
                  "message": "강의 제목은 필수입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "A002", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Unauthorized",
                                    value = """
                {
                  "success": false,
                  "message": "로그인이 필요한 서비스입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "A003", description = "권한 없음 (강사 아님)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Forbidden",
                                    value = """
                {
                  "success": false,
                  "message": "해당 메뉴에 대한 접근 권한이 없습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "M002", description = "사용자를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "존재하지 않은 사용자입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L001", description = "해당하는 데이터 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "해당 강의를 찾을 수 없습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L005", description = "날짜 설정 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "판매 시작일은 판매 종료일 이전이어야 합니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L006", description = "날짜 설정 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "강의 시작일은 강의 종료일 이전이어야 합니다.",
                  "data": null
                }
                """)))
    })
    @interface UpdateLectureErrorResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Validation Error",
                                    value = """
                {
                  "success": false,
                  "message": "강의 제목은 필수입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "A002", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Unauthorized",
                                    value = """
                {
                  "success": false,
                  "message": "로그인이 필요한 서비스입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "A003", description = "권한 없음 (강사 아님)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Forbidden",
                                    value = """
                {
                  "success": false,
                  "message": "해당 메뉴에 대한 접근 권한이 없습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "M002", description = "사용자를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "존재하지 않은 사용자입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L001", description = "해당하는 데이터 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "해당 강의를 찾을 수 없습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L002", description = "중복 요청",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "이미 종료된 강의입니다.",
                  "data": null
                }
                """)))
    })
    @interface CloseLectureErrorResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Validation Error",
                                    value = """
                {
                  "success": false,
                  "message": "강의 제목은 필수입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "A002", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Unauthorized",
                                    value = """
                {
                  "success": false,
                  "message": "로그인이 필요한 서비스입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "A003", description = "권한 없음 (강사 아님)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Forbidden",
                                    value = """
                {
                  "success": false,
                  "message": "해당 메뉴에 대한 접근 권한이 없습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "M002", description = "사용자를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "존재하지 않은 사용자입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L001", description = "해당하는 데이터 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "해당 강의를 찾을 수 없습니다.",
                  "data": null
                }
                """)))
    })
    @interface DeleteLectureErrorResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Validation Error",
                                    value = """
                {
                  "success": false,
                  "message": "강의 제목은 필수입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "A002", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Unauthorized",
                                    value = """
                {
                  "success": false,
                  "message": "로그인이 필요한 서비스입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "A003", description = "권한 없음 (강사 아님)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Forbidden",
                                    value = """
                {
                  "success": false,
                  "message": "해당 메뉴에 대한 접근 권한이 없습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "M002", description = "사용자를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "존재하지 않은 사용자입니다.",
                  "data": null
                }
                """))),
    })
    @interface GetLectureListErrorResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Validation Error",
                                    value = """
                {
                  "success": false,
                  "message": "강의 제목은 필수입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "A002", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Unauthorized",
                                    value = """
                {
                  "success": false,
                  "message": "로그인이 필요한 서비스입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "A003", description = "권한 없음 (강사 아님)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Forbidden",
                                    value = """
                {
                  "success": false,
                  "message": "해당 메뉴에 대한 접근 권한이 없습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "M002", description = "사용자를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "존재하지 않은 사용자입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L001", description = "해당하는 데이터 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "해당 강의를 찾을 수 없습니다.",
                  "data": null
                }
                """)))
    })
    @interface GetLectureDetailErrorResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Validation Error",
                                    value = """
                {
                  "success": false,
                  "message": "강의 제목은 필수입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "A002", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Unauthorized",
                                    value = """
                {
                  "success": false,
                  "message": "로그인이 필요한 서비스입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "A003", description = "권한 없음 (강사 아님)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Forbidden",
                                    value = """
                {
                  "success": false,
                  "message": "해당 메뉴에 대한 접근 권한이 없습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "M002", description = "사용자를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "존재하지 않은 사용자입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L001", description = "해당하는 데이터 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "해당 강의를 찾을 수 없습니다.",
                  "data": null
                }
                """)))
    })
    @interface UploadVodErrorResponse {
    }
}
