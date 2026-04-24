package com.example.liveklass.service.enrollment;

import com.example.liveklass.domain.*;
import com.example.liveklass.dto.enrollment.PaymentRequest;
import com.example.liveklass.global.error.CustomException;
import com.example.liveklass.global.error.ErrorCode;
import com.example.liveklass.repository.EnrollmentRepository;
import com.example.liveklass.repository.LectureRepository;
import com.example.liveklass.repository.MemberRepository;
import com.example.liveklass.service.CreatorService;
import com.example.liveklass.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ConfirmEnrollmentTest {

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
                .status(EnrollmentStatus.PENDING).build();
    }
    @Test
    @DisplayName("결제 확정 성공")
    void confirmEnrollment_success() {
        // given
        PaymentRequest request = new PaymentRequest(lecturePrice);
        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(user));
        given(enrollmentRepository.findById(enrollmentId)).willReturn(Optional.of(enrollment));

        // when
        enrollmentService.confirmEnrollment(request, enrollmentId, userName);

        // then
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.CONFIRMED);
        assertThat(enrollment.getPaidAmount()).isEqualTo(lecturePrice);
        assertThat(enrollment.getPaymentAt()).isNotNull();
        assertThat(enrollment.getRefundDeadline()).isNotNull();
    }

    @Test
    @DisplayName("결제 확정 실패 - 본인의 수강 내역이 아님")
    void confirmEnrollment_fail_FORBIDDEN() {
        // given
        PaymentRequest request = new PaymentRequest(lecturePrice);
        Member otherUser = Member.builder().userName("hacker").build();

        given(memberRepository.findByUserName("hacker")).willReturn(Optional.of(otherUser));
        given(enrollmentRepository.findById(enrollmentId)).willReturn(Optional.of(enrollment));

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> enrollmentService.confirmEnrollment(request, enrollmentId, "hacker"));

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
    }

    @Test
    @DisplayName("결제 확정 실패 - 실제 강의 가격과 지불 금액이 다름")
    void confirmEnrollment_fail_INVALID_PAYMENT_AMOUNT() {
        // given
        PaymentRequest request = new PaymentRequest(100L);
        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(user));
        given(enrollmentRepository.findById(enrollmentId)).willReturn(Optional.of(enrollment));

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> enrollmentService.confirmEnrollment(request, enrollmentId, userName));

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.INVALID_PAYMENT_AMOUNT);
    }

    @Test
    @DisplayName("결제 확정 실패 - 대기 상태가 아닌 경우")
    void confirmEnrollment_fail_NOT_PENDING() {
        // given
        PaymentRequest request = new PaymentRequest(lecturePrice);

        Enrollment confirmedEnrollment = Enrollment.builder()
                .id(enrollmentId)
                .member(user)
                .lecture(lecture)
                .status(EnrollmentStatus.CONFIRMED)
                .build();

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(user));
        given(enrollmentRepository.findById(enrollmentId)).willReturn(Optional.of(confirmedEnrollment));

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> enrollmentService.confirmEnrollment(request, enrollmentId, userName));

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.NOT_PENDING);
    }

    @Test
    @DisplayName("결제 확정 실패 -  강의가 OPEN이 아닌 경우")
    void confirmEnrollment_fail_SALE_PERIOD_EXPIRED() {
        // given
        PaymentRequest request = new PaymentRequest(lecturePrice);


        lecture = Lecture.builder()
                .id(1L)
                .title("테스트 강의")
                .basePrice(lecturePrice)
                .lectureStatus(LectureStatus.CLOSED)
                .build();

        enrollment = Enrollment.builder()
                .id(enrollmentId)
                .member(user)
                .lecture(lecture)
                .status(EnrollmentStatus.PENDING).build();

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(user));
        given(enrollmentRepository.findById(enrollmentId)).willReturn(Optional.of(enrollment));

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> enrollmentService.confirmEnrollment(request, enrollmentId, userName));

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.SALE_PERIOD_EXPIRED);
    }
}
