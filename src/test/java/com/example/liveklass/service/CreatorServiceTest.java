package com.example.liveklass.service;

import com.example.liveklass.domain.*;
import com.example.liveklass.dto.lecture.LectureCreateRequest;
import com.example.liveklass.repository.LectureRepository;
import com.example.liveklass.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreatorServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LectureRepository lectureRepository;

    @InjectMocks
    CreatorService creatorService;


    @Test
    @DisplayName("강의 등록 성공 - 초안 상태로 생성")
    void createLecture_success() {

        String userName = "teacher1";

        Member creator = Member.builder()
                .userName(userName)
                .role(MemberRole.CREATOR)
                .build();

        LectureCreateRequest request = new LectureCreateRequest(
                1L,
                "Spring Boot 입문",
                "설명입니다",
                30,
                30000L,
                LectureType.VOD
        );

        Lecture lecture = Lecture.builder().id(100L).build();

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));
        given(lectureRepository.save(any(Lecture.class))).willReturn(lecture);

        Long savedId = creatorService.createLecture(request, userName);

        assertThat(savedId).isEqualTo(100L);

        verify(lectureRepository).save(argThat(l ->
                l.getLectureStatus() == LectureStatus.DRAFT && l.getCreator() == creator
        ));

        verify(lectureRepository, times(1)).save(any(Lecture.class));
    }
}
