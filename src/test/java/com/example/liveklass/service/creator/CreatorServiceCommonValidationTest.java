package com.example.liveklass.service.creator;

import com.example.liveklass.domain.*;
import com.example.liveklass.dto.lecture.LectureCreateRequest;
import com.example.liveklass.dto.lecture.LectureUpdateRequest;
import com.example.liveklass.global.error.CustomException;
import com.example.liveklass.global.error.ErrorCode;
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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CreatorServiceCommonValidationTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LectureRepository lectureRepository;

    @InjectMocks
    CreatorService creatorService;

    String userName = "teacher1";
    Long lectureId = 1L;
    Member creator;
    Lecture lecture;
    LectureUpdateRequest request;

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
        LectureUpdateRequest request = new LectureUpdateRequest(
                "수정된 제목", "설명", 30, 50000L, LectureType.VOD,
                null, null, null, null
        );
    }

    @Test
    @DisplayName("공통 에러 - 사용자를 찾지 못함")
    void global_fail_MEMBER_NOT_FOUND() {

        given(memberRepository.findByUserName(userName)).willReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> creatorService.updateLecture(request, lectureId, userName));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("공통 에러 - 강사 권한 없음")
    void global_fail_FORBIDDEN() {
        Member creator = Member.builder()
                .userName(userName)
                .role(MemberRole.STUDENT)
                .build();

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));

        CustomException exception = assertThrows(CustomException.class, () -> creatorService.updateLecture(request, lectureId, userName));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
    }

    @Test
    @DisplayName("공통 에러 - 해당 강의가 존재하지 않음")
    void global_fail_LECTURE_NOT_FOUND() {

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> creatorService.updateLecture(request, lectureId, userName));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.LECTURE_NOT_FOUND);
    }

    @Test
    @DisplayName("공통 에러 - 사용자가 강의 생성자가 아님")
    void global_fail_NOT_LECTURE_CREATOR() {
        Member lectureCreator = Member.builder()
                .userName("lectureOwner")
                .role(MemberRole.CREATOR)
                .build();

        Member requestCreator = Member.builder()
                .userName(userName)
                .role(MemberRole.CREATOR)
                .build();

        Lecture lecture = Lecture.builder()
                .id(1L)
                .creator(lectureCreator)
                .title("기존 제목")
                .currentEnrollmentCount(10)
                .build();

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(requestCreator));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));

        CustomException exception = assertThrows(CustomException.class, () -> creatorService.updateLecture(request, lectureId, userName));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_LECTURE_CREATOR);
    }
}
