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

public interface UserApiDocument {

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
            @ApiResponse(responseCode = "A003", description = "권한 없음 (본인 아님)",
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
    @interface GetEnRollmentListErrorResponse {
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
            @ApiResponse(responseCode = "A003", description = "권한 없음 (본인 아님)",
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
    @interface GetPaymentListErrorResponse {
    }
}
