package com.example.liveklass.service.creator;

import com.example.liveklass.domain.*;
import com.example.liveklass.dto.creator.MyLectureListDto;
import com.example.liveklass.dto.creator.MyLectureSearchRequest;
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
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GetMyLectureListTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LectureRepository lectureRepository;

    @InjectMocks
    CreatorService creatorService;

    @Test
    @DisplayName("내가 생성한 강의 목록 불러오기 성공")
    void getMyLectureList_success() {

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

        MyLectureSearchRequest request = new MyLectureSearchRequest("제목", null, 0, 10, "");

        Page<Lecture> lecturePage = new PageImpl<>(List.of(lecture), request.toPageable(), 1);

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));
        given(lectureRepository.findAllByCreator_UserNameAndLectureStatus(
                eq(userName), eq(null), any(Pageable.class)
        )).willReturn(lecturePage);

        Page<MyLectureListDto> result = creatorService.getMyLectureList(request, userName, null);

        assertThat(result).isNotNull();
        assertThat(result.getContent().get(0).title()).isEqualTo("기존 제목");
        assertThat(result.getTotalElements()).isEqualTo(1);

        verify(lectureRepository).findAllByCreator_UserNameAndLectureStatus(eq(userName), eq(null), any(Pageable.class));
    }
}
