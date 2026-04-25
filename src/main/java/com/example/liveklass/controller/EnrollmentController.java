package com.example.liveklass.controller;

import com.example.liveklass.document.EnrollmentApiDocument;
import com.example.liveklass.dto.enrollment.MyEnrollmentListDto;
import com.example.liveklass.dto.enrollment.MyEnrollmentRequest;
import com.example.liveklass.dto.global.ApiResponse;
import com.example.liveklass.dto.enrollment.PaymentRequest;
import com.example.liveklass.dto.global.PagedResponse;
import com.example.liveklass.dto.payment.PaymentHistoryRequest;
import com.example.liveklass.dto.payment.PaymentHistoryResponse;
import com.example.liveklass.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Enrollment", description = "강의 신청 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @EnrollmentApiDocument.SubscribeErrorResponse
    @Operation(summary = "강의 신청", description = "신청할 강의 ID를 받아 강의를 신청합니다.")
    @PostMapping("/{lectureId}/subscribe")
    public ResponseEntity<ApiResponse<Void>> subEnrollment(
            @PathVariable Long lectureId,
            @Parameter(hidden = true) @SessionAttribute(name = "userName") String userName) {

        enrollmentService.subEnrollment(lectureId, userName);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @EnrollmentApiDocument.ConfirmErrorResponse
    @Operation(summary = "신청한 강의 결제", description = "신청ID를 받아 신청한 강의를 최종 결제하여 상태를 변경합니다. (PENDING -> CONFIRMED)")
    @PatchMapping("/{enrollmentId}/confirm")
    public ResponseEntity<ApiResponse<Void>> confirmEnrollment(
            @Valid @RequestBody PaymentRequest request,
            @PathVariable Long enrollmentId,
            @Parameter(hidden = true) @SessionAttribute(name = "userName") String userName) {

        enrollmentService.confirmEnrollment(request, enrollmentId, userName);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @EnrollmentApiDocument.CancelErrorResponse
    @Operation(summary = "신청한 강의 취소", description = "신청ID를 받아 신청한 강의를 취소합니다.")
    @PatchMapping("/{enrollmentId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelEnrollment(
            @PathVariable Long enrollmentId,
            @Parameter(hidden = true) @SessionAttribute(name = "userName") String userName) {

        enrollmentService.cancelEnrollment(enrollmentId, userName);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @EnrollmentApiDocument.GetEnRollmentListErrorResponse
    @Operation(summary = "신청한 강의 목록", description = "신청한 강의 목록을 페이징하여 반환합니다.")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<PagedResponse<MyEnrollmentListDto>>> getEnrollmentList(
            @ParameterObject @Valid @ModelAttribute MyEnrollmentRequest request,
            @Parameter(hidden = true) @SessionAttribute(name = "userName") String userName
    ) {

        Page<MyEnrollmentListDto> page = enrollmentService.getEnrollmentList(request, userName);
        return ResponseEntity.ok(ApiResponse.ok(PagedResponse.from(page)));
    }

    @EnrollmentApiDocument.GetPaymentListErrorResponse
    @Operation(summary = "나의 결제 목록", description = "결제 목록을 반환합니다.")
    @GetMapping("/payments")
    public ResponseEntity<ApiResponse<PagedResponse<PaymentHistoryResponse>>> getPaymentHistory(
            @ParameterObject  @Valid @ModelAttribute PaymentHistoryRequest request,
            @Parameter(hidden = true) @SessionAttribute(name = "userName") String userName
            ) {

        Page<PaymentHistoryResponse> page = enrollmentService.getPaymentHistory(request, userName);

        return ResponseEntity.ok(ApiResponse.ok(PagedResponse.from(page)));
    }
}
