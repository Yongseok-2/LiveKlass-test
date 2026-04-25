package com.example.liveklass.service.lecture;

import com.example.liveklass.domain.*;
import com.example.liveklass.dto.creator.MyLectureDetailResponse;
import com.example.liveklass.dto.lecture.LectureCreateRequest;
import com.example.liveklass.dto.lecture.LectureDetailResponse;
import com.example.liveklass.global.error.CustomException;
import com.example.liveklass.global.error.ErrorCode;
import com.example.liveklass.repository.EnrollmentRepository;
import com.example.liveklass.repository.LectureRepository;
import com.example.liveklass.repository.MemberRepository;
import com.example.liveklass.service.CreatorService;
import com.example.liveklass.service.LectureService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GetLectureDetailTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    LectureService lectureService;

    String userName = "teacher1";
    Long lectureId = 1L;
    LocalDateTime paymentAt = LocalDateTime.parse("2026-05-12T09:00:00");
    Member creator;
    Member student;
    LectureCreateRequest request;
    Lecture lecture;
    Enrollment enrollment;

    @BeforeEach
    void setUp() {
        creator = Member.builder()
                .userName(userName)
                .role(MemberRole.CREATOR)
                .build();

        student = Member.builder()
                .userName("student")
                .role(MemberRole.STUDENT)
                .build();

        lecture = Lecture.builder()
                .id(lectureId)
                .creator(creator)
                .title("기존 제목")
                .currentEnrollmentCount(1)
                .basePrice(50000L)
                .lectureStatus(LectureStatus.OPEN)
                .build();

        enrollment = Enrollment.builder()
                .id(1L).
                member(student).
                lecture(lecture).
                paidAmount(30000L).
                status(EnrollmentStatus.CONFIRMED).
                paymentAt(paymentAt).
                refundDeadline(paymentAt.plusDays(3)).
                build();
    }

    @Test
    @DisplayName("강의 상세보기 성공 - 비로그인")
    void getLectureDetail_success() {

        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));

        LectureDetailResponse response = lectureService.getLectureDetail(lectureId, null);

        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("기존 제목");
        assertThat(response.currentEnrollmentCount()).isEqualTo(1);
        assertThat(response.isEnrolled()).isEqualTo(false);

        verify(lectureRepository).findById(eq(lectureId));
    }

    @Test
    @DisplayName("강의 상세보기 성공 - 로그인 상태에 사용자가 해당 강의를 결제했을 경우")
    void getLectureDetail_success_login() {

        given(memberRepository.findByUserName("student")).willReturn(Optional.of(student));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));
        given(enrollmentRepository.findByMemberAndLecture(student, lecture)).willReturn(Optional.of(enrollment));

        LectureDetailResponse response = lectureService.getLectureDetail(lectureId, "student");

        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("기존 제목");
        assertThat(response.currentEnrollmentCount()).isEqualTo(1);
        assertThat(response.isEnrolled()).isEqualTo(true);

        verify(lectureRepository).findById(eq(lectureId));
    }

    @Test
    @DisplayName("강의 상세보기 실패 - 해당 강의가 존재하지 않음")
    void getLectureDetail_fail_LECTURE_NOT_FOUND() {

        given(lectureRepository.findById(lectureId)).willReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> lectureService.getLectureDetail(lectureId, "student"));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.LECTURE_NOT_FOUND);
    }

    @Test
    @DisplayName("강의 상세보기 실패 - 해당 강의가 초안(DRAFT) 상태")
    void getLectureDetail_fail_LECTURE_NOT_AVAILABLE() {

        lecture = Lecture.builder()
                .id(lectureId)
                .creator(creator)
                .title("기존 제목")
                .currentEnrollmentCount(1)
                .basePrice(50000L)
                .lectureStatus(LectureStatus.DRAFT)
                .build();

        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));

        CustomException exception = assertThrows(CustomException.class, () -> lectureService.getLectureDetail(lectureId, "student"));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.LECTURE_NOT_AVAILABLE);
    }

    @Test
    @DisplayName("강의 상세보기 실패 - 로그인한 사용자가 존재하지않음")
    void getLectureDetail_fail_MEMBER_NOT_FOUND() {

        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));
        given(memberRepository.findByUserName("student")).willReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> lectureService.getLectureDetail(lectureId, "student"));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
    }
}
