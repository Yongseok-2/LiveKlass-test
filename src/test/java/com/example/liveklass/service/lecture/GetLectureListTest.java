package com.example.liveklass.service.lecture;

import com.example.liveklass.domain.Lecture;
import com.example.liveklass.domain.LectureStatus;
import com.example.liveklass.domain.Member;
import com.example.liveklass.domain.MemberRole;
import com.example.liveklass.dto.creator.MyLectureListDto;
import com.example.liveklass.dto.creator.MyLectureSearchRequest;
import com.example.liveklass.dto.lecture.LectureCreateRequest;
import com.example.liveklass.dto.lecture.LectureListDto;
import com.example.liveklass.dto.lecture.LectureSearchRequest;
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
public class GetLectureListTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LectureRepository lectureRepository;

    @InjectMocks
    LectureService lectureService;

    String userName = "teacher1";
    Long lectureId = 1L;
    LocalDateTime salesStart = LocalDateTime.parse("2026-05-01T09:00:00");
    LocalDateTime salesEnd = LocalDateTime.parse("2026-05-03T09:00:00");
    LocalDateTime lectureStartAt = LocalDateTime.parse("2026-05-05T09:00:00");
    LocalDateTime lectureEndAt = LocalDateTime.parse("2026-05-10T09:00:00");
    Member creator;
    LectureCreateRequest request;
    Lecture lecture;

    @BeforeEach
    void setUp() {
        creator = Member.builder()
                .userName(userName)
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
                .lectureStatus(LectureStatus.DRAFT)
                .build();
    }

    @Test
    @DisplayName("강의 목록 불러오기 성공 - 제목 검색어 없이")
    void getLectureList_success_no_title() {

        LectureSearchRequest request = new LectureSearchRequest(null, null, null, "");

        Page<Lecture> lecturePage = new PageImpl<>(List.of(lecture), request.toPageable(), 1);

        given(lectureRepository.findAllByLectureStatus(
                eq(LectureStatus.OPEN), any(Pageable.class)
        )).willReturn(lecturePage);

        Page<LectureListDto> result = lectureService.getLectureList(request);

        assertThat(result).isNotNull();
        assertThat(result.getContent().get(0).title()).isEqualTo("기존 제목");
        assertThat(result.getTotalElements()).isEqualTo(1);

        verify(lectureRepository).findAllByLectureStatus(eq(LectureStatus.OPEN), any(Pageable.class));
    }

    @Test
    @DisplayName("강의 목록 불러오기 성공 - 제목 검색")
    void getLectureList_success_searchTitle() {

        String title = "제목";

        LectureSearchRequest request = new LectureSearchRequest(title, null, null, "");

        Page<Lecture> lecturePage = new PageImpl<>(List.of(lecture), request.toPageable(), 1);

        given(lectureRepository.findAllByTitleContainingAndLectureStatus(
                eq(title), eq(LectureStatus.OPEN), any(Pageable.class)
        )).willReturn(lecturePage);

        Page<LectureListDto> result = lectureService.getLectureList(request);

        assertThat(result).isNotNull();
        assertThat(result.getContent().get(0).title()).isEqualTo("기존 제목");
        assertThat(result.getTotalElements()).isEqualTo(1);

        verify(lectureRepository).findAllByTitleContainingAndLectureStatus(eq(title), eq(LectureStatus.OPEN), any(Pageable.class));
    }
}
