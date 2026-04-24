package com.example.liveklass.service;

import com.example.liveklass.domain.*;
import com.example.liveklass.dto.lecture.LectureCreateRequest;
import com.example.liveklass.dto.lecture.LectureUpdateRequest;
import com.example.liveklass.global.error.CustomException;
import com.example.liveklass.global.error.ErrorCode;
import com.example.liveklass.repository.LectureRepository;
import com.example.liveklass.repository.MemberRepository;
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

    @Test
    @DisplayName("강의 등록 실패 - 사용자를 찾지 못함")
    void createLecture_fail_MEMBER_NOT_FOUND() {

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

        given(memberRepository.findByUserName(userName)).willReturn(Optional.empty());


        CustomException exception = assertThrows(CustomException.class, () -> creatorService.createLecture(request, userName));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("강의 등록 실패 - 강사 권한이 없음")
    void createLecture_fail_FORBIDDEN() {

        String userName = "teacher1";

        Member creator = Member.builder()
                .userName(userName)
                .role(MemberRole.STUDENT)
                .build();

        LectureCreateRequest request = new LectureCreateRequest(
                1L,
                "Spring Boot 입문",
                "설명입니다",
                30,
                30000L,
                LectureType.VOD
        );

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));


        CustomException exception = assertThrows(CustomException.class, () -> creatorService.createLecture(request, userName));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
    }

    @Test
    @DisplayName("강의 수정 성공")
    void updateLecture_success() {

        String userName = "teacher1";

        Long lectureId = 1L;

        Member creator = Member.builder()
                .userName(userName)
                .role(MemberRole.CREATOR)
                .build();

        Lecture lecture = Lecture.builder()
                .id(lectureId)
                .creator(creator)
                .title("기존 제목")
                .currentEnrollmentCount(10)
                .build();

        LectureUpdateRequest request = new LectureUpdateRequest(
                "수정된 제목", "설명", 30, 50000L, LectureType.VOD,
                null, null, null, null
        );

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));


        Long resultId = creatorService.updateLecture(request, lectureId, userName);

        assertThat(resultId).isEqualTo(lectureId);
        assertThat(lecture.getTitle()).isEqualTo("수정된 제목");
        assertThat(lecture.getMaxCapacity()).isEqualTo(30);
    }

    @Test
    @DisplayName("강의 수정 실패 - 사용자를 찾지 못함")
    void updateLecture_fail_MEMBER_NOT_FOUND() {

        String userName = "teacher1";

        Long lectureId = 1L;

        Member creator = Member.builder()
                .userName(userName)
                .role(MemberRole.CREATOR)
                .build();

        Lecture lecture = Lecture.builder()
                .id(lectureId)
                .creator(creator)
                .title("기존 제목")
                .currentEnrollmentCount(10)
                .build();

        LectureUpdateRequest request = new LectureUpdateRequest(
                "수정된 제목", "설명", 30, 50000L, LectureType.VOD,
                null, null, null, null
        );

        given(memberRepository.findByUserName(userName)).willReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> creatorService.updateLecture(request, lectureId, userName));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND);
    }

    @Test
    @DisplayName("강의 수정 실패 - 강사 권한 없음")
    void updateLecture_fail_FORBIDDEN() {

        String userName = "teacher1";

        Long lectureId = 1L;

        Member creator = Member.builder()
                .userName(userName)
                .role(MemberRole.STUDENT)
                .build();

        Lecture lecture = Lecture.builder()
                .id(lectureId)
                .creator(creator)
                .title("기존 제목")
                .currentEnrollmentCount(10)
                .build();

        LectureUpdateRequest request = new LectureUpdateRequest(
                "수정된 제목", "설명", 30, 50000L, LectureType.VOD,
                null, null, null, null
        );

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));

        CustomException exception = assertThrows(CustomException.class, () -> creatorService.updateLecture(request, lectureId, userName));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
    }

    @Test
    @DisplayName("강의 수정 실패 - 해당 강의가 존재하지 않음")
    void updateLecture_fail_LECTURE_NOT_FOUND() {

        String userName = "teacher1";

        Long lectureId = 1L;

        Member creator = Member.builder()
                .userName(userName)
                .role(MemberRole.CREATOR)
                .build();

        Lecture lecture = Lecture.builder()
                .id(100L)
                .creator(creator)
                .title("기존 제목")
                .currentEnrollmentCount(10)
                .build();

        LectureUpdateRequest request = new LectureUpdateRequest(
                "수정된 제목", "설명", 30, 50000L, LectureType.VOD,
                null, null, null, null
        );

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> creatorService.updateLecture(request, lectureId, userName));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.LECTURE_NOT_FOUND);
    }

    @Test
    @DisplayName("강의 수정 실패 - 해당 강의가 존재하지 않음")
    void updateLecture_fail_NOT_LECTURE_CREATOR() {

        String userName = "teacher1";

        Long lectureId = 1L;

        Member creator = Member.builder()
                .userName("user1")
                .role(MemberRole.CREATOR)
                .build();

        Lecture lecture = Lecture.builder()
                .id(1L)
                .creator(creator)
                .title("기존 제목")
                .currentEnrollmentCount(10)
                .build();

        LectureUpdateRequest request = new LectureUpdateRequest(
                "수정된 제목", "설명", 30, 50000L, LectureType.VOD,
                null, null, null, null
        );

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));

        CustomException exception = assertThrows(CustomException.class, () -> creatorService.updateLecture(request, lectureId, userName));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_LECTURE_CREATOR);
    }

    @Test
    @DisplayName("강의 수정 실패 - 수정할 수강 정원이 현재 신청인원보다 적음")
    void updateLecture_fail_INVALID_CAPACITY_SETTING() {

        String userName = "teacher1";

        Long lectureId = 1L;

        Member creator = Member.builder()
                .userName(userName)
                .role(MemberRole.CREATOR)
                .build();

        Lecture lecture = Lecture.builder()
                .id(1L)
                .creator(creator)
                .title("기존 제목")
                .currentEnrollmentCount(30)
                .build();

        LectureUpdateRequest request = new LectureUpdateRequest(
                "수정된 제목", "설명", 10, 50000L, LectureType.VOD,
                null, null, null, null
        );

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));

        CustomException exception = assertThrows(CustomException.class, () -> creatorService.updateLecture(request, lectureId, userName));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_CAPACITY_SETTING);
    }

    @Test
    @DisplayName("강의 수정 실패 - 판매 시작일이 수강 시작일 이후")
    void updateLecture_fail_SALE_START_DATE_AFTER_LECTURE() {

        String userName = "teacher1";

        Long lectureId = 1L;

        LocalDateTime salesStart = LocalDateTime.parse("2026-05-05T09:00:00");
        LocalDateTime salesEnd = LocalDateTime.parse("2026-05-08T09:00:00");
        LocalDateTime lectureStartAt = LocalDateTime.parse("2026-05-03T09:00:00");

        Member creator = Member.builder()
                .userName(userName)
                .role(MemberRole.CREATOR)
                .build();

        Lecture lecture = Lecture.builder()
                .id(1L)
                .creator(creator)
                .title("기존 제목")
                .currentEnrollmentCount(10)
                .build();

        LectureUpdateRequest request = new LectureUpdateRequest(
                "수정된 제목", "설명", 30, 50000L, LectureType.VOD,
                salesStart, salesEnd, lectureStartAt, null
        );

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));

        CustomException exception = assertThrows(CustomException.class, () -> creatorService.updateLecture(request, lectureId, userName));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.SALE_START_DATE_AFTER_LECTURE);
    }

    @Test
    @DisplayName("강의 수정 실패 - 판매 시작일이 판매 종료일 이후")
    void updateLecture_fail_INCORRECT_SALE_START_DATE() {

        String userName = "teacher1";

        Long lectureId = 1L;

        LocalDateTime salesStart = LocalDateTime.parse("2026-05-05T09:00:00");
        LocalDateTime salesEnd = LocalDateTime.parse("2026-05-03T09:00:00");
        LocalDateTime lectureStartAt = LocalDateTime.parse("2026-05-10T09:00:00");

        Member creator = Member.builder()
                .userName(userName)
                .role(MemberRole.CREATOR)
                .build();

        Lecture lecture = Lecture.builder()
                .id(1L)
                .creator(creator)
                .title("기존 제목")
                .currentEnrollmentCount(10)
                .build();

        LectureUpdateRequest request = new LectureUpdateRequest(
                "수정된 제목", "설명", 30, 50000L, LectureType.VOD,
                salesStart, salesEnd, lectureStartAt, null
        );

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));

        CustomException exception = assertThrows(CustomException.class, () -> creatorService.updateLecture(request, lectureId, userName));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INCORRECT_SALE_START_DATE);
    }

    @Test
    @DisplayName("강의 수정 실패 - 강의 시작일이 강의 종료일 이후")
    void updateLecture_fail_INCORRECT_LECTURE_START_DATE() {

        String userName = "teacher1";

        Long lectureId = 1L;

        LocalDateTime salesStart = LocalDateTime.parse("2026-05-01T09:00:00");
        LocalDateTime salesEnd = LocalDateTime.parse("2026-05-03T09:00:00");
        LocalDateTime lectureStartAt = LocalDateTime.parse("2026-05-20T09:00:00");
        LocalDateTime lectureEndAt = LocalDateTime.parse("2026-05-10T09:00:00");

        Member creator = Member.builder()
                .userName(userName)
                .role(MemberRole.CREATOR)
                .build();

        Lecture lecture = Lecture.builder()
                .id(1L)
                .creator(creator)
                .title("기존 제목")
                .currentEnrollmentCount(10)
                .build();

        LectureUpdateRequest request = new LectureUpdateRequest(
                "수정된 제목", "설명", 30, 50000L, LectureType.VOD,
                salesStart, salesEnd, lectureStartAt, lectureEndAt
        );

        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));

        CustomException exception = assertThrows(CustomException.class, () -> creatorService.updateLecture(request, lectureId, userName));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INCORRECT_LECTURE_START_DATE);
    }
}
