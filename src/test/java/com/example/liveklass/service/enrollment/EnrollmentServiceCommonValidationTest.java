package com.example.liveklass.service.enrollment;

import com.example.liveklass.domain.*;
import com.example.liveklass.dto.lecture.LectureUpdateRequest;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceCommonValidationTest {

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
    @DisplayName("공통 에러 - 사용자를 찾지 못함")
    void global_fail_MEMBER_NOT_FOUND() {

        given(memberRepository.findByUserName(userName)).willReturn(Optional.empty());

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> enrollmentService.subEnrollment(lectureId, userName));

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("공통 에러 - 강의를 찾지 못함")
    void global_fail_LECTURE_NOT_FOUND() {

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(user));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.empty());
        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> enrollmentService.subEnrollment(lectureId, userName));

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.LECTURE_NOT_FOUND);
    }
}
