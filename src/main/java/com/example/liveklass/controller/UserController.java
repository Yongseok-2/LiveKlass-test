package com.example.liveklass.controller;

import com.example.liveklass.dto.ApiResponse;
import com.example.liveklass.dto.enrollment.MyEnrollmentRequest;
import com.example.liveklass.dto.enrollment.MyEnrollmentResponse;
import com.example.liveklass.dto.payment.PaymentHistoryRequest;
import com.example.liveklass.dto.payment.PaymentHistoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "일반회원 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    @Operation(summary = "신청한 강의 목록", description = "신청한 강의 목록을 페이징하여 반환합니다.")
    @GetMapping("/lectures")
    public ResponseEntity<ApiResponse<MyEnrollmentResponse>> getEnrollmentList(@Valid @ModelAttribute MyEnrollmentRequest request) {

        // TODO: userService.getEnrollmentList(request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "나의 결제 목록", description = "결제 목록을 페이징하여 반환합니다.")
    @GetMapping("/payments")
    public ResponseEntity<ApiResponse<PaymentHistoryResponse>> getPaymentHistory(@Valid @ModelAttribute PaymentHistoryRequest request) {

        // TODO: userService.getPaymentHistory(request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
