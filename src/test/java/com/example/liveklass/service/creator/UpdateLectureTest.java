package com.example.liveklass.service.creator;

import com.example.liveklass.domain.*;
import com.example.liveklass.dto.lecture.LectureCreateRequest;
import com.example.liveklass.dto.lecture.LectureUpdateRequest;
import com.example.liveklass.global.error.CustomException;
import com.example.liveklass.global.error.ErrorCode;
import com.example.liveklass.repository.LectureRepository;
import com.example.liveklass.repository.MemberRepository;
import com.example.liveklass.service.CreatorService;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UpdateLectureTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private EnrollmentService enrollmentService;

    @InjectMocks
    CreatorService creatorService;

    String userName = "teacher1";
    Long lectureId = 1L;
    LocalDateTime salesStart = LocalDateTime.parse("2026-05-01T09:00:00");
    LocalDateTime salesEnd = LocalDateTime.parse("2026-05-03T09:00:00");
    LocalDateTime lectureStartAt = LocalDateTime.parse("2026-05-05T09:00:00");
    LocalDateTime lectureEndAt = LocalDateTime.parse("2026-05-10T09:00:00");
    Member creator;
    LectureUpdateRequest request;
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
                .waitCount(0)
                .basePrice(50000L)
                .salesStartAt(salesStart)
                .salesEndAt(salesEnd)
                .lectureStartAt(lectureStartAt)
                .lectureEndAt(lectureEndAt)
                .lectureStatus(LectureStatus.DRAFT)
                .build();

       request = new LectureUpdateRequest(
                "수정된 제목", "설명", 30, 50000L, LectureType.VOD,
                null, null, null, null
        );
    }

    @Test
    @DisplayName("강의 수정 성공")
    void updateLecture_success() {
        given(memberRepository.findByUserName(userName)).willReturn(Optional.of(creator));
        given(lectureRepository.findById(lectureId)).willReturn(Optional.of(lecture));


        Long resultId = creatorService.updateLecture(request, lectureId, userName);

        assertThat(resultId).isEqualTo(lectureId);
        assertThat(lecture.getTitle()).isEqualTo("수정된 제목");
        assertThat(lecture.getMaxCapacity()).isEqualTo(30);
    }

    @Test
    @DisplayName("강의 수정 실패 - 수정할 수강 정원이 현재 신청인원보다 적음")
    void updateLecture_fail_INVALID_CAPACITY_SETTING() {

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

        LocalDateTime salesStart = LocalDateTime.parse("2026-05-05T09:00:00");
        LocalDateTime salesEnd = LocalDateTime.parse("2026-05-08T09:00:00");
        LocalDateTime lectureStartAt = LocalDateTime.parse("2026-05-03T09:00:00");

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

        LocalDateTime salesStart = LocalDateTime.parse("2026-05-05T09:00:00");
        LocalDateTime salesEnd = LocalDateTime.parse("2026-05-03T09:00:00");
        LocalDateTime lectureStartAt = LocalDateTime.parse("2026-05-10T09:00:00");

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

        LocalDateTime salesStart = LocalDateTime.parse("2026-05-01T09:00:00");
        LocalDateTime salesEnd = LocalDateTime.parse("2026-05-03T09:00:00");
        LocalDateTime lectureStartAt = LocalDateTime.parse("2026-05-20T09:00:00");
        LocalDateTime lectureEndAt = LocalDateTime.parse("2026-05-10T09:00:00");

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
