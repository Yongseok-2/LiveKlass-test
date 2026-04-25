package com.example.liveklass.service.enrollment;

import com.example.liveklass.domain.*;
import com.example.liveklass.dto.enrollment.PaymentRequest;
import com.example.liveklass.global.error.CustomException;
import com.example.liveklass.global.error.ErrorCode;
import com.example.liveklass.repository.EnrollmentRepository;
import com.example.liveklass.repository.LectureRepository;
import com.example.liveklass.repository.MemberRepository;
import com.example.liveklass.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CancelEnrollmentTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    EnrollmentService enrollmentService;

    private String userName = "student1";
    private Long enrollmentId = 100L;
    private Long lecturePrice = 50000L;
    private Member user;
    private Lecture lecture;
    private Enrollment enrollment;
    LocalDateTime paymentAt = LocalDateTime.parse("2026-03-12T09:00:00");
    LocalDateTime refundDeadLine = LocalDateTime.parse("2026-12-12T09:00:00");

    @BeforeEach
    void setUp() {
        user = Member.builder().userName(userName).role(MemberRole.STUDENT).build();

        lecture = Lecture.builder()
                .id(1L)
                .title("테스트 강의")
                .basePrice(lecturePrice)
                .lectureStatus(LectureStatus.OPEN)
                .build();

        enrollment = Enrollment.builder()
                .id(enrollmentId)
                .member(user)
                .lecture(lecture)
                .paidAmount(50000L)
                .paymentAt(paymentAt)
                .refundDeadline(refundDeadLine)
                .status(EnrollmentStatus.CONFIRMED).build();
    }

    @Test
    @DisplayName("결제 취소 성공")
    void cancelEnrollment_success() {
        // given
        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(user));
        given(enrollmentRepository.findById(enrollmentId)).willReturn(Optional.of(enrollment));

        // when
        enrollmentService.cancelEnrollment(enrollmentId, userName);

        // then
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.CANCELLED);
    }

    @Test
    @DisplayName("결제 취소 실패 - 신청건을 찾지 못함")
    void cancelEnrollment_fail_ENROLLMENT_NOT_FOUND() {
        // given
        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(user));
        given(enrollmentRepository.findById(enrollmentId)).willReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class,
                () -> enrollmentService.cancelEnrollment(enrollmentId, userName));

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ENROLLMENT_NOT_FOUND);
    }

    @Test
    @DisplayName("결제 취소 실패 - 본인의 수강 내역이 아님")
    void cancelEnrollment_fail_FORBIDDEN() {

        Member otherUser = Member.builder().userName("hacker").build();

        // given
        given(memberRepository.findByUserName("hacker")).willReturn(Optional.of(otherUser));
        given(enrollmentRepository.findById(enrollmentId)).willReturn(Optional.of(enrollment));

        CustomException ex = assertThrows(CustomException.class,
                () -> enrollmentService.cancelEnrollment(enrollmentId, "hacker"));

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
    }

    @Test
    @DisplayName("결제 취소 실패 - 이미 취소된 신청건")
    void cancelEnrollment_fail_ALREADY_CANCELED() {

        enrollment = Enrollment.builder()
                .id(enrollmentId)
                .member(user)
                .lecture(lecture)
                .paidAmount(50000L)
                .paymentAt(paymentAt)
                .refundDeadline(refundDeadLine)
                .status(EnrollmentStatus.CANCELLED).build();

        // given
        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(user));
        given(enrollmentRepository.findById(enrollmentId)).willReturn(Optional.of(enrollment));

        CustomException ex = assertThrows(CustomException.class,
                () -> enrollmentService.cancelEnrollment(enrollmentId, userName));

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ALREADY_CANCELED);
    }

    @Test
    @DisplayName("결제 취소 실패 - 환불 가능 기간이 지남")
    void cancelEnrollment_fail_REFUND_PERIOD_EXPIRED() {

        LocalDateTime wrongDeadLine = LocalDateTime.parse("2026-03-30T09:00:00");

        enrollment = Enrollment.builder()
                .id(enrollmentId)
                .member(user)
                .lecture(lecture)
                .paidAmount(50000L)
                .paymentAt(paymentAt)
                .refundDeadline(wrongDeadLine)
                .status(EnrollmentStatus.CONFIRMED).build();

        // given
        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(user));
        given(enrollmentRepository.findById(enrollmentId)).willReturn(Optional.of(enrollment));

        CustomException ex = assertThrows(CustomException.class,
                () -> enrollmentService.cancelEnrollment(enrollmentId, userName));

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.REFUND_PERIOD_EXPIRED);
    }
}
