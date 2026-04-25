package com.example.liveklass.service;

import com.example.liveklass.domain.*;
import com.example.liveklass.dto.lecture.LectureDetailResponse;
import com.example.liveklass.dto.lecture.LectureListDto;
import com.example.liveklass.dto.lecture.LectureSearchRequest;
import com.example.liveklass.global.error.CustomException;
import com.example.liveklass.global.error.ErrorCode;
import com.example.liveklass.repository.EnrollmentRepository;
import com.example.liveklass.repository.LectureRepository;
import com.example.liveklass.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureService {

    private final LectureRepository lectureRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final MemberRepository memberRepository;

    public Page<LectureListDto> getLectureList(@Valid LectureSearchRequest request) {

        Pageable pageable = request.toPageable();

        Page<Lecture> lecturePage;
        String title = request.title();

        if (title == null || title.isBlank()) {
            lecturePage = lectureRepository.findAllByLectureStatus(LectureStatus.OPEN, pageable);
        } else {
            lecturePage = lectureRepository.findAllByTitleContainingAndLectureStatus(
                    title, LectureStatus.OPEN, pageable);
        }

        return lecturePage.map(LectureListDto::from);
    }

    public LectureDetailResponse getLectureDetail(Long lectureId, String userName) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new CustomException(ErrorCode.LECTURE_NOT_FOUND));

        if (lecture.getLectureStatus() == LectureStatus.DRAFT) {
            throw new CustomException(ErrorCode.LECTURE_NOT_AVAILABLE);
        }

        boolean isEnrolled = false;

        if (userName != null) {
            Member member = memberRepository.findByUserName(userName)
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

            isEnrolled = enrollmentRepository.findByMemberAndLecture(member, lecture)
                    .map(enrollment -> enrollment.getStatus() == EnrollmentStatus.CONFIRMED)
                    .orElse(false);
        }

        return LectureDetailResponse.from(lecture, isEnrolled);
    }
}
