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

public interface EnrollmentApiDocument {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
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
            @ApiResponse(responseCode = "L001", description = "해당 강의 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "해당 강의를 찾을 수 없습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L002", description = "해당 강의 종료됨",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "이미 종료된 강의입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L003", description = "수강 신청 기간이 아님",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "수강 신청 기간이 아닙니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L004", description = "수강 인원 마감",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "수강 정원이 마감되었습니다.",
                  "data": null
                }
                """)))
    })
    @interface SubscribeErrorResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
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
            @ApiResponse(responseCode = "L001", description = "해당 강의 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "해당 강의를 찾을 수 없습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L002", description = "해당 강의 종료됨",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "이미 종료된 강의입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L003", description = "수강 신청 기간이 아님",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "수강 신청 기간이 아닙니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L004", description = "수강 인원 마감",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "수강 정원이 마감되었습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "E001", description = "이미 취소된 수강 신청",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "ALREADY_CANCELED",
                                    value = """
                {
                  "success": false,
                  "message": "이미 취소된 수강 신청입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "E003", description = "결제 대기 상태가 아님",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "NOT_PENDING",
                                    value = """
                {
                  "success": false,
                  "message": "결제 대기 상태에서만 결제가 가능합니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "E004", description = "신청 내역 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "ENROLLMENT_NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "해당 신청내역을 찾을 수 없습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "E005", description = "판매 기간 종료",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "SALE_PERIOD_EXPIRED",
                                    value = """
                {
                  "success": false,
                  "message": "해당 강의는 판매가 종료되었습니다.",
                  "data": null
                }
                """)))
    })
    @interface ConfirmErrorResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
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
            @ApiResponse(responseCode = "L001", description = "해당 강의 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "해당 강의를 찾을 수 없습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L002", description = "해당 강의 종료됨",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "이미 종료된 강의입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L003", description = "수강 신청 기간이 아님",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "수강 신청 기간이 아닙니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L004", description = "수강 인원 마감",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "수강 정원이 마감되었습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "E001", description = "이미 취소된 수강 신청",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "ALREADY_CANCELED",
                                    value = """
                {
                  "success": false,
                  "message": "이미 취소된 수강 신청입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "E002", description = "환불 가능 기간 지남",
            content = @Content(schema = @Schema(implementation = ApiResponse.class),
                    examples = @ExampleObject(name = "ENROLLMENT_NOT_FOUND",
                            value = """
                {
                  "success": false,
                  "message": "환불 가능 기간이 지났습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "E004", description = "신청 내역 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "ENROLLMENT_NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "해당 신청내역을 찾을 수 없습니다.",
                  "data": null
                }
                """)))

    })
    @interface RefundErrorResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
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
            @ApiResponse(responseCode = "L001", description = "해당 강의 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "해당 강의를 찾을 수 없습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L002", description = "해당 강의 종료됨",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "이미 종료된 강의입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L003", description = "수강 신청 기간이 아님",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "수강 신청 기간이 아닙니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L004", description = "수강 인원 마감",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "수강 정원이 마감되었습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "E001", description = "이미 취소된 수강 신청",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "ALREADY_CANCELED",
                                    value = """
                {
                  "success": false,
                  "message": "이미 취소된 수강 신청입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "E004", description = "신청 내역 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "ENROLLMENT_NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "해당 신청내역을 찾을 수 없습니다.",
                  "data": null
                }
                """)))

    })
    @interface CancelErrorResponse {
    }
}
