package com.example.liveklass.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common (공통)
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "허용되지 않은 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "서버 내부 오류가 발생했습니다."),

    // Lecture (강의)
    LECTURE_NOT_FOUND(HttpStatus.NOT_FOUND, "L001", "해당 강의를 찾을 수 없습니다."),
    LECTURE_ALREADY_CLOSED(HttpStatus.BAD_REQUEST, "L002", "이미 종료된 강의입니다."),
    ENROLLMENT_PERIOD_ENDED(HttpStatus.BAD_REQUEST, "L003", "수강 신청 기간이 아닙니다."),
    CAPACITY_EXCEEDED(HttpStatus.BAD_REQUEST, "L004", "수강 정원이 마감되었습니다."),
    INCORRECT_SALE_START_DATE(HttpStatus.BAD_REQUEST, "L005", "판매 시작일은 판매 종료일 이전이어야 합니다."),
    INCORRECT_LECTURE_START_DATE(HttpStatus.BAD_REQUEST, "L006", "강의 시작일은 강의 종료일 이전이어야 합니다."),

    // Enrollment (수강 신청/결제)
    ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "E001", "이미 취소된 수강 신청입니다."),
    REFUND_PERIOD_EXPIRED(HttpStatus.BAD_REQUEST, "E002", "환불 가능 기간이 지났습니다."),
    NOT_PENDING(HttpStatus.BAD_REQUEST, "E003", "결제 대기 상태에서만 결제가 가능합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
