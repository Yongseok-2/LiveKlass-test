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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeleteLectureTest {

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
                .basePrice(50000L)
                .lectureStatus(LectureStatus.DRAFT)
                .build();
    }

    @Test
    @DisplayName("강의 삭제 성공")
    void deleteLecture_success() {

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));
        given(lectureRepository.findById(1L)).willReturn(Optional.of(lecture));

        creatorService.deleteLecture(lectureId, userName);

        verify(lectureRepository, times(1)).delete(lecture);
    }
}


