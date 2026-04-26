package com.example.liveklass.service.enrollment;

import com.example.liveklass.domain.*;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubEnrollmentTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private String userName = "student1";
    private Long lectureId = 1L;
    private Member user;
    private Member creator;
    private Lecture lecture;

    @BeforeEach
    void setUp() {
        user = Member.builder().userName(userName).role(MemberRole.STUDENT).build();
        creator = Member.builder().userName("teacher").role(MemberRole.CREATOR).build();
        lecture = Lecture.builder()
                .id(lectureId)
                .title("테스트 강의")
                .creator(creator)
                .lectureStatus(LectureStatus.OPEN) // 기본 오픈 상태
                .maxCapacity(10)
                .currentEnrollmentCount(0)
                .salesStartAt(LocalDateTime.now().minusDays(1))
                .salesEndAt(LocalDateTime.now().plusDays(1))
                .build();
    }

    @Test
    @DisplayName("수강 신청 성공")
    void subEnrollment_success() {
        // given
        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(user));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));
        given(enrollmentRepository.existsByMemberAndLectureAndStatusNot(user, lecture, EnrollmentStatus.CANCELLED)).willReturn(false);

        // when
        enrollmentService.subEnrollment(lectureId, userName);

        // then
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
        verify(lectureRepository, times(1)).increaseEnrollmentCountWithCondition(lectureId);
    }

    @Test
    @DisplayName("수강 신청 성공 - 대기열로 들어감")
    void subEnrollment_success_waitList() {

        lecture = Lecture.builder()
                .id(lectureId)
                .title("테스트 강의")
                .creator(creator)
                .lectureStatus(LectureStatus.OPEN) // 기본 오픈 상태
                .maxCapacity(10)
                .currentEnrollmentCount(10)
                .salesStartAt(LocalDateTime.now().minusDays(1))
                .salesEndAt(LocalDateTime.now().plusDays(1))
                .build();

        // given
        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(user));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));
        given(enrollmentRepository.existsByMemberAndLectureAndStatusNot(user, lecture, EnrollmentStatus.CANCELLED)).willReturn(false);
        given(lectureRepository.increaseEnrollmentCountWithCondition(lectureId)).willReturn(0);

        // when
        enrollmentService.subEnrollment(lectureId, userName);

        // then
        assertThat(lecture.getCurrentEnrollmentCount()).isEqualTo(10);
        verify(lectureRepository, times(1)).increaseWaitCount(lectureId);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
        verify(enrollmentRepository).save(argThat(enrollment ->
                enrollment.getStatus() == EnrollmentStatus.WAITLISTED
        ));
    }

    @Test
    @DisplayName("수강 신청 실패 - 본인의 강의를 신청")
    void subEnrollment_fail_CANNOT_ENROLL_OWN_LECTURE() {
        // given
        given(memberRepository.findByUserName("teacher")).willReturn(Optional.of(creator));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> enrollmentService.subEnrollment(lectureId, "teacher"));

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.CANNOT_ENROLL_OWN_LECTURE);
    }

    @Test
    @DisplayName("수강 신청 실패 - 이미 신청한 강의")
    void subEnrollment_fail_ALREADY_ENROLLED() {
        // given
        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(user));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));
        given(enrollmentRepository.existsByMemberAndLectureAndStatusNot(user, lecture, EnrollmentStatus.CANCELLED)).willReturn(true);

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> enrollmentService.subEnrollment(lectureId, userName));

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ALREADY_ENROLLED);
    }

    @Test
    @DisplayName("수강 신청 실패 - 오픈 상태가 아닌 강의")
    void subEnrollment_fail_SALE_PERIOD_EXPIRED() {


        lecture = Lecture.builder()
                .id(lectureId)
                .creator(creator)
                .lectureStatus(LectureStatus.CLOSED)
                .build();

        // given
        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(user));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> enrollmentService.subEnrollment(lectureId, userName));

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.SALE_PERIOD_EXPIRED);
    }
}
