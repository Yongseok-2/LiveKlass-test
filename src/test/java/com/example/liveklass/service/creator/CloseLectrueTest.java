package com.example.liveklass.service.creator;

import com.example.liveklass.domain.Lecture;
import com.example.liveklass.domain.LectureStatus;
import com.example.liveklass.domain.Member;
import com.example.liveklass.domain.MemberRole;
import com.example.liveklass.global.error.CustomException;
import com.example.liveklass.global.error.ErrorCode;
import com.example.liveklass.repository.LectureRepository;
import com.example.liveklass.repository.MemberRepository;
import com.example.liveklass.service.CreatorService;
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
public class CloseLectrueTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LectureRepository lectureRepository;

    @InjectMocks
    CreatorService creatorService;


    @Test
    @DisplayName("강의 종료 성공")
    void closeLecture_success() {

        String userName = "teacher1";

        Long lectureId = 1L;

        LocalDateTime salesStart = LocalDateTime.parse("2026-05-01T09:00:00");
        LocalDateTime salesEnd = LocalDateTime.parse("2026-05-03T09:00:00");
        LocalDateTime lectureStartAt = LocalDateTime.parse("2026-05-05T09:00:00");
        LocalDateTime lectureEndAt = LocalDateTime.parse("2026-05-10T09:00:00");

        Member creator = Member.builder()
                .userName(userName)
                .role(MemberRole.CREATOR)
                .build();

        Lecture lecture = Lecture.builder()
                .id(lectureId)
                .creator(creator)
                .title("기존 제목")
                .currentEnrollmentCount(10)
                .basePrice(50000L)
                .salesStartAt(salesStart)
                .salesEndAt(salesEnd)
                .lectureStartAt(lectureStartAt)
                .lectureEndAt(lectureEndAt)
                .lectureStatus(LectureStatus.DRAFT)
                .build();

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));
        given(lectureRepository.findById(1L)).willReturn(Optional.of(lecture));

        creatorService.closeLecture(1L, userName);

        assertThat(lecture.getLectureStatus()).isEqualTo(LectureStatus.CLOSED);
    }

    @Test
    @DisplayName("강의 종료 실패 - 이미 종료된 강의")
    void closeLecture_fail_LECTURE_ALREADY_CLOSED() {

        String userName = "teacher1";

        Long lectureId = 1L;

        LocalDateTime salesStart = LocalDateTime.parse("2026-05-01T09:00:00");
        LocalDateTime salesEnd = LocalDateTime.parse("2026-05-03T09:00:00");
        LocalDateTime lectureStartAt = LocalDateTime.parse("2026-05-05T09:00:00");
        LocalDateTime lectureEndAt = LocalDateTime.parse("2026-05-10T09:00:00");

        Member creator = Member.builder()
                .userName(userName)
                .role(MemberRole.CREATOR)
                .build();

        Lecture lecture = Lecture.builder()
                .id(lectureId)
                .creator(creator)
                .title("기존 제목")
                .currentEnrollmentCount(10)
                .basePrice(50000L)
                .salesStartAt(salesStart)
                .salesEndAt(salesEnd)
                .lectureStartAt(lectureStartAt)
                .lectureEndAt(lectureEndAt)
                .lectureStatus(LectureStatus.CLOSED)
                .build();

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));

        CustomException exception = assertThrows(CustomException.class, () -> creatorService.closeLecture(lectureId, userName));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.LECTURE_ALREADY_CLOSED);
    }
}
