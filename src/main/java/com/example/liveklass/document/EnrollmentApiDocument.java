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
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Unauthorized",
                                    value = """
                {
                  "success": false,
                  "message": "로그인이 필요한 서비스입니다.",
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
            @ApiResponse(responseCode = "E005", description = "강의가 오픈 되어있지 않음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "해당 강의 신청 기간이 아닙니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "E006", description = "본인 강의 신청 불가",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "이미 수강 신청한 강의입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "E007", description = "본인 강의 신청 불가",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "본인의 강의는 신청할 수 없습니다.",
                  "data": null
                }
                """)))
    })
    @interface SubscribeErrorResponse {
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
                  "message": "결제 금액은 필수입니다. / 결제 금액은 양수여야 합니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Unauthorized",
                                    value = """
                {
                  "success": false,
                  "message": "로그인이 필요한 서비스입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "A003", description = "본인의 신청 내역이 아님",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Unauthorized",
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
                  "message": "해당 강의 신청 기간이 아닙니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "E008", description = "결제 금액 불일치",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "SALE_PERIOD_EXPIRED",
                                    value = """
                {
                  "success": false,
                  "message": "결제 금액이 맞지 않습니다.",
                  "data": null
                }
                """)))
    })
    @interface ConfirmErrorResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Unauthorized",
                                    value = """
                {
                  "success": false,
                  "message": "로그인이 필요한 서비스입니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "A003", description = "본인의 신청 내역이 아님",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Unauthorized",
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
            @ApiResponse(responseCode = "E001", description = "환불 기간 지남",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "REFUND_PERIOD_EXPIRED",
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
    @interface CancelErrorResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신청한 강의 목록 반환 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Success",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "요청이 성공적으로 처리되었습니다.",
                                              "data": {
                                                "contents": [
                                                  {
                                                    "enrollmentId": 1,
                                                    "title": "Spring Boot 입문",
                                                    "creatorName": "홍길동",
                                                    "status": "COMPLETED",
                                                    "enrolledAt": "2026-05-01T09:00:00",
                                                    "lectureStartAt": "2026-05-03T09:00:00"
                                                  }
                                                ],
                                                "pageNumber": 0,
                                                "pageSize": 10,
                                                "totalElements": 100,
                                                "totalPages": 10,
                                                "isLast": false
                                              }
                                            }
                """))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Unauthorized",
                                    value = """
                {
                  "success": false,
                  "message": "로그인이 필요한 서비스입니다.",
                  "data": null
                }
                """)))
    })
    @interface GetEnRollmentListErrorResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 목록 반환 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Success",
                                    value = """
                              {
                                                    "success": true,
                                                    "message": "요청이 성공적으로 처리되었습니다.",
                                                    "data": {
                                                      "contents": [
                                                        {
                                                          "enrollmentId": 1,
                                                          "title": "Spring Boot 입문",
                                                          "paidAmount": 30000,
                                                          "status": "CONFIRMED",
                                                          "paymentAt": "2026-05-01T09:00:00",
                                                          "refundDeadline": "2026-05-08T09:00:00"
                                                        }
                                                      ],
                                                      "pageNumber": 0,
                                                      "pageSize": 10,
                                                      "totalElements": 100,
                                                      "totalPages": 10,
                                                      "isLast": false
                                                    }
                                                  }
                        """))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Unauthorized",
                                    value = """
                {
                  "success": false,
                  "message": "로그인이 필요한 서비스입니다.",
                  "data": null
                }
                """)))
    })
    @interface GetPaymentListErrorResponse {
    }
}
