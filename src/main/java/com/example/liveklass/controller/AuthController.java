package com.example.liveklass.controller;

import com.example.liveklass.document.AuthApiDocument;
import com.example.liveklass.dto.ApiResponse;
import com.example.liveklass.dto.auth.LoginRequest;
import com.example.liveklass.dto.auth.SignUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    @AuthApiDocument.SignUpErrorResponse
    @Operation(summary = "회원가입", description = "회원가입을 통해 회원을 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signUp(@Valid @RequestBody SignUpRequest request) {

        // TODO: authService.signUp(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok());
    }

    @AuthApiDocument.LoginErrorResponse
    @Operation(summary = "로그인", description = "아이디와 비밀번호를 통해 로그인합니다.(현재는 세션에 저장)")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(@Valid @RequestBody LoginRequest request, HttpSession session) {

        // TODO:
        // 1. 서비스에서 닉네임 가져오기
        // 2. 세션에 정보 저장

        return ResponseEntity.ok(ApiResponse.ok());
    }

    @Operation(summary = "로그아웃", description = "세션을 만료시켜 로그아웃 처리합니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(ApiResponse.ok());
    }

}
