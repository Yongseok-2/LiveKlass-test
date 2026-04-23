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

public interface AuthApiDocument {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "M001", description = "아이디 중복",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "DUPLICATE_ID",
                                    value = """
                {
                  "success": false,
                  "message": "이미 존재하는 아이디입니다.",
                  "data": null
                }
                """)))
    })
    @interface SignUpErrorResponse {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "A001", description = "아이디 / 비밀번호 불일치",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "LOGIN_FAILED",
                                    value = """
                {
                  "success": false,
                  "message": "아이디 또는 비밀번호가 일치하지 않습니다.",
                  "data": null
                }
                """)))
    })
    @interface LoginErrorResponse {
    }
}
