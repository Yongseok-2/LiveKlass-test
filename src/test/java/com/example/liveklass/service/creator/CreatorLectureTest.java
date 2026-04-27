package com.example.liveklass.service.creator;

import com.example.liveklass.domain.*;
import com.example.liveklass.dto.lecture.LectureCreateRequest;
import com.example.liveklass.repository.LectureRepository;
import com.example.liveklass.repository.MemberRepository;
import com.example.liveklass.service.CreatorService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreatorLectureTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LectureRepository lectureRepository;

    @InjectMocks
    CreatorService creatorService;

    String userName = "teacher1";
    Long lectureId = 1L;
    Member creator;
    LectureCreateRequest request;
    Lecture lecture;
    LocalDateTime salesStart = LocalDateTime.parse("2026-05-01T09:00:00");
    LocalDateTime salesEnd = LocalDateTime.parse("2026-05-03T09:00:00");
    LocalDateTime lectureStartAt = LocalDateTime.parse("2026-05-05T09:00:00");
    LocalDateTime lectureEndAt = LocalDateTime.parse("2026-05-10T09:00:00");

    @BeforeEach
    void setUp() {
        creator = Member.builder()
                .userName(userName)
                .role(MemberRole.CREATOR)
                .build();

        lecture = Lecture.builder()
                .id(lectureId)
                .creator(creator)
                .title("기존 제목")
                .currentEnrollmentCount(10)
                .waitCount(0)
                .basePrice(50000L)
                .lectureStatus(LectureStatus.DRAFT)
                .build();

        request = new LectureCreateRequest(
                lectureId,
                "Spring Boot 입문",
                "설명입니다",
                30,
                30000L,
                LectureType.VOD,
                salesStart,
                salesEnd,
                lectureStartAt,
                lectureEndAt
        );
    }

    @Test
    @DisplayName("강의 등록 성공 - 초안 상태로 생성")
    void createLecture_success() {
        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));
        given(lectureRepository.save(any(Lecture.class))).willReturn(lecture);

        Long savedId = creatorService.createLecture(request, userName);

        assertThat(savedId).isEqualTo(lectureId);

        verify(lectureRepository).save(argThat(l ->
                l.getLectureStatus() == LectureStatus.DRAFT && l.getCreator() == creator
        ));

        verify(lectureRepository, times(1)).save(any(Lecture.class));
    }
}
