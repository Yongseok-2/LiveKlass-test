package com.example.liveklass.service.creator;

import com.example.liveklass.domain.*;
import com.example.liveklass.dto.creator.MyLectureDetailResponse;
import com.example.liveklass.dto.creator.MyLectureListDto;
import com.example.liveklass.repository.EnrollmentRepository;
import com.example.liveklass.repository.LectureRepository;
import com.example.liveklass.repository.MemberRepository;
import com.example.liveklass.service.CreatorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
public class GetMyLectureDetailTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    CreatorService creatorService;

    @Test
    @DisplayName("내가 생성한 강의 상세보기 성공")
    void getMyLectureDetail_success() {

        String userName = "teacher1";

        Long lectureId = 1L;

        LocalDateTime paymentAt = LocalDateTime.parse("2026-05-12T09:00:00");

        Pageable pageable = PageRequest.of(0, 10);

        Member creator = Member.builder()
                .userName(userName)
                .role(MemberRole.CREATOR)
                .build();

        Member student = Member.builder()
                .userName("student")
                .role(MemberRole.STUDENT)
                .build();

        Lecture lecture = Lecture.builder()
                .id(lectureId)
                .creator(creator)
                .title("기존 제목")
                .currentEnrollmentCount(1)
                .basePrice(50000L)
                .lectureStatus(LectureStatus.DRAFT)
                .build();

        Enrollment enrollment = Enrollment.builder()
                .id(1L).
                member(student).
                lecture(lecture).
                paidAmount(30000L).
                status(EnrollmentStatus.CONFIRMED).
                paymentAt(paymentAt).
                refundDeadline(paymentAt.plusDays(3)).
                build();

        Page<Enrollment> enrollmentPage = new PageImpl<>(List.of(enrollment), pageable, 1);

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));
        given(enrollmentRepository.findAllByLectureId(
                eq(lectureId), any(Pageable.class)
        )).willReturn(enrollmentPage);

        MyLectureDetailResponse response = creatorService.getMyLecture(lectureId, userName, pageable);

        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("기존 제목");
        assertThat(response.enrollmentList()).isNotNull();
        assertThat(response.currentEnrollmentCount()).isEqualTo(1);

        verify(enrollmentRepository).findAllByLectureId(eq(lectureId), any(Pageable.class));
    }

}
