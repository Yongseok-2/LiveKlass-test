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
            @ApiResponse(responseCode = "200", description = "강의 생성 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Success",
                                    value = """
                {
                  "success": true,
                  "message": "요청이 성공적으로 처리되었습니다.",
                  "data": 100
                }
                """))),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Validation Error",
                                    value = """
                {
                  "success": false,
                  "message": "강의 제목은 필수입니다. / 강사 id는 필수입니다. / 최소 수강 인원은 1명 이상이어야 합니다. / 가격은 0원 이상이어야 합니다. / 강의 유형은 필수입니다.",
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
            @ApiResponse(responseCode = "M002", description = "존재하지 않은 사용자",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "MEMBER_NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "존재하지 않은 사용자입니다.",
                  "data": null
                }
                """)))
    })
    @interface CreateLectureErrorResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의 오픈 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Success",
                                    value = """
                {
                  "success": true,
                  "message": "요청이 성공적으로 처리되었습니다.",
                  "data": 100
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
                """))),
            @ApiResponse(responseCode = "L007", description = "날짜 설정 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "정원은 현재 수강 인원보다 적게 설정할 수 없습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L008", description = "강의 생성자 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "강의 생성자만 수정 가능합니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L009", description = "날짜 설정 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "판매 시작일은 강의 시작 이전이어야 합니다.",
                  "data": null
                }
                """)))
    })
    @interface OpenLectureErrorResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의 수정 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Success",
                                    value = """
                {
                  "success": true,
                  "message": "요청이 성공적으로 처리되었습니다.",
                  "data": 100
                }
                """))),
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
                """))),
            @ApiResponse(responseCode = "L007", description = "날짜 설정 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "정원은 현재 수강 인원보다 적게 설정할 수 없습니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L008", description = "강의 생성자 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "강의 생성자만 수정 가능합니다.",
                  "data": null
                }
                """))),
            @ApiResponse(responseCode = "L009", description = "날짜 설정 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "판매 시작일은 강의 시작 이전이어야 합니다.",
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
                  "message": "강의 제목은 필수입니다. / 최소 수강 인원은 1명 이상이어야 합니다. / 가격은 0원 이상이어야 합니다. / 강의 유형은 필수입니다.",
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
            @ApiResponse(responseCode = "L008", description = "강의 생성자 검증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "강의 생성자만 수정 가능합니다.",
                  "data": null
                }
                """)))
    })
    @interface DeleteLectureErrorResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내가 생성한 강의 목록 반환 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Success",
                                    value = """
                                            {
                                              "success": true,
                                              "message": "요청이 성공적으로 처리되었습니다.",
                                              "data": {
                                                "contents": [
                                                  {
                                                    "id": 1,
                                                    "title": "Spring Boot 입문",
                                                    "creatorName": "홍길동",
                                                    "lectureStatus": "OPEN",
                                                    "lectureType": "VOD",
                                                    "currentEnrollmentCount": 15,
                                                    "maxCapacity": 30,
                                                    "basePrice": 50000,
                                                    "createdAt": "2026-05-01T09:00:00"
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
            @ApiResponse(responseCode = "200", description = "내가 생성한 강의 목록 반환 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Success",
                                    value = """
                                            {
                                                                          "success": true,
                                                                          "message": "요청이 성공적으로 처리되었습니다.",
                                                                          "data": {
                                                                            "title": "Spring Boot 입문",
                                                                            "description": "Spring Boot 입문 강의입니다.",
                                                                            "lectureStatus": "OPEN",
                                                                            "lectureType": "VOD",
                                                                            "currentEnrollmentCount": 15,
                                                                            "enrollmentList": {
                                                                              "contents": [
                                                                                {
                                                                                  "id": 1,
                                                                                  "member_name": "수강자1",
                                                                                  "paymentAt": "2026-05-01T09:00:00"
                                                                                }
                                                                              ],
                                                                              "pageNumber": 0,
                                                                              "pageSize": 10,
                                                                              "totalElements": 100,
                                                                              "totalPages": 10,
                                                                              "isLast": false
                                                                            },
                                                                            "maxCapacity": 30,
                                                                            "waitCount": 15,
                                                                            "basePrice": 50000,
                                                                            "createdAt": "2026-05-01T09:00:00",
                                                                            "salesStartAt": "2026-05-01T09:00:00",
                                                                            "salesEndAt": "2026-05-05T09:00:00",
                                                                            "lectureStartAt": "2026-05-05T09:00:00",
                                                                            "lectureEndAt": "2026-05-10T09:00:00"
                                                                          }
                                                                        }
                """))),

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
}
