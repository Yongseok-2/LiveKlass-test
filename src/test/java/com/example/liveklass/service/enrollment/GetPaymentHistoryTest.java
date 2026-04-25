package com.example.liveklass.service.enrollment;

import com.example.liveklass.domain.*;
import com.example.liveklass.dto.enrollment.MyEnrollmentListDto;
import com.example.liveklass.dto.enrollment.MyEnrollmentRequest;
import com.example.liveklass.dto.lecture.LectureCreateRequest;
import com.example.liveklass.dto.payment.PaymentHistoryRequest;
import com.example.liveklass.dto.payment.PaymentHistoryResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GetPaymentHistoryTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    EnrollmentService enrollmentService;

    String userName = "teacher1";
    Long lectureId = 1L;
    Long enrollmentId = 100L;
    LocalDateTime salesStart = LocalDateTime.parse("2026-05-01T09:00:00");
    LocalDateTime salesEnd = LocalDateTime.parse("2026-05-03T09:00:00");
    LocalDateTime lectureStartAt = LocalDateTime.parse("2026-05-05T09:00:00");
    LocalDateTime lectureEndAt = LocalDateTime.parse("2026-05-10T09:00:00");
    Member user;
    Member creator;
    LectureCreateRequest request;
    Lecture lecture;
    Enrollment enrollment;

    @BeforeEach
    void setUp() {
        creator = Member.builder()
                .id(1L)
                .userName(userName)
                .role(MemberRole.CREATOR)
                .build();

        user = Member.builder()
                .id(1L)
                .userName("user1")
                .role(MemberRole.STUDENT)
                .build();

        lecture = Lecture.builder()
                .id(lectureId)
                .creator(creator)
                .title("기존 제목")
                .currentEnrollmentCount(10)
                .basePrice(50000L)
                .salesStartAt(salesStart)
                .salesEndAt(salesEnd)
                .lectureStartAt(lectureStartAt)
                .lectureEndAt(lectureEndAt)
                .lectureStatus(LectureStatus.OPEN)
                .build();

        enrollment = Enrollment.builder()
                .id(enrollmentId)
                .member(user)
                .lecture(lecture)
                .status(EnrollmentStatus.CONFIRMED).build();
    }

    @Test
    @DisplayName("신청 목록 불러오기 성공")
    void getPaymentHistory_success() {

        PaymentHistoryRequest request = new PaymentHistoryRequest(null, null, "");

        Page<Enrollment> enrollmentePage = new PageImpl<>(List.of(enrollment), request.toPageable(), 1);

        given(memberRepository.findByUserName("user1")).willReturn(Optional.of(user));
        given(enrollmentRepository.findAllByMemberIdAndStatusNotAndPaidAmountIsNotNull(eq(1L), eq(EnrollmentStatus.PENDING), any(Pageable.class))).willReturn(enrollmentePage);

        Page<PaymentHistoryResponse> result = enrollmentService.getPaymentHistory(request, "user1");

        assertThat(result).isNotNull();
        assertThat(result.getContent().get(0).title()).isEqualTo("기존 제목");
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).status()).isEqualTo(EnrollmentStatus.CONFIRMED);

        verify(enrollmentRepository).findAllByMemberIdAndStatusNotAndPaidAmountIsNotNull(eq(1L), eq(EnrollmentStatus.PENDING), any(Pageable.class));
    }
}
