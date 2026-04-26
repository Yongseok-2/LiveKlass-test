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

public interface LectureApiDocument {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의 목록 반환 성공",
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
                                                      "creatorName": "홍길동",
                                                      "title": "Spring Boot 입문",
                                                      "lectureType": "VOD",
                                                      "currentEnrollmentCount": 15,
                                                      "maxCapacity": 30,
                                                      "basePrice": 50000,
                                                      "createdAt": "2026-05-01T09:00:00",
                                                      "salesStartAt": "2026-05-01T09:00:00",
                                                      "salesEndAt": "2026-05-05T09:00:00",
                                                      "lectureStartAt": "2026-05-05T09:00:00",
                                                      "lectureEndAt": "2026-05-10T09:00:00"
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
            @ApiResponse(responseCode = "L001", description = "해당 강의 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "NOT_FOUND",
                                    value = """
                {
                  "success": false,
                  "message": "",
                  "data": null
                }
                """)))
    })
    @interface ListErrorResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "강의 상세정보 반환 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "Success",
                                    value = """
                {
                  "success": true,
                                                "message": "요청이 성공적으로 처리되었습니다.",
                                                "data": {
                                                  "creatorName": "홍길동",
                                                  "title": "Spring Boot 입문",
                                                  "description": "Spring Boot 입문 강의입니다.",
                                                  "lectureStatus": "OPEN",
                                                  "lectureType": "VOD",
                                                  "currentEnrollmentCount": 15,
                                                  "maxCapacity": 30,
                                                  "waitCount": 15,
                                                  "basePrice": 50000,
                                                  "createdAt": "2026-05-01T09:00:00",
                                                  "salesStartAt": "2026-05-01T09:00:00",
                                                  "salesEndAt": "2026-05-05T09:00:00",
                                                  "lectureStartAt": "2026-05-05T09:00:00",
                                                  "lectureEndAt": "2026-05-10T09:00:00",
                                                  "isEnrolled": "true"
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
            @ApiResponse(responseCode = "L010", description = "해당 강의 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "BAD_REQUEST",
                                    value = """
                {
                  "success": false,
                  "message": "아직 공개 되지않은 강의입니다.",
                  "data": null
                }
                """)))
    })
    @interface DetailErrorResponse {
    }
}
