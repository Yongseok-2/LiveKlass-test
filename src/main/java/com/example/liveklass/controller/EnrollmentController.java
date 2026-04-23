package com.example.liveklass.controller;

import com.example.liveklass.dto.ApiResponse;
import com.example.liveklass.dto.enrollment.PaymentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Enrollment", description = "강의 신청 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    @Operation(summary = "강의 신청", description = "신청할 강의 ID를 받아 강의를 신청합니다.")
    @PostMapping("/{lectureId}/subscribe")
    public ResponseEntity<ApiResponse<Void>> subEnrollment(@PathVariable Long lectureId) {

        // TODO: userService.subEnrollment(lectureId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @Operation(summary = "신청한 강의 결제", description = "신청ID를 받아 신청한 강의를 최종 결제하여 상태를 변경합니다. (PENDING -> CONFIRMED)")
    @PatchMapping("/{enrollmentId}/confirm")
    public ResponseEntity<ApiResponse<Void>> confirmEnrollment(@Valid @RequestBody PaymentRequest request,
            @PathVariable Long enrollmentId) {

        // TODO: userService.confirmEnrollment(request, enrollmentId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @Operation(summary = "신청한 강의 환불", description = "신청ID를 받아 신청한 강의를 환불합니다.")
    @PatchMapping("/{enrollmentId}/refund")
    public ResponseEntity<ApiResponse<Void>> refundEnrollment(@PathVariable Long enrollmentId) {

        // TODO: userService.refundEnrollment(enrollmentId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @Operation(summary = "신청한 강의 취소", description = "신청ID를 받아 신청한 강의를 취소합니다.")
    @PatchMapping("/{enrollmentId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelEnrollment(@PathVariable Long enrollmentId) {

        // TODO: userService.cancelEnrollment(enrollmentId);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
