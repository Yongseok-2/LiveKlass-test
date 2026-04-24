package com.example.liveklass.service;

import com.example.liveklass.domain.*;
import com.example.liveklass.global.error.CustomException;
import com.example.liveklass.global.error.ErrorCode;
import com.example.liveklass.repository.EnrollmentRepository;
import com.example.liveklass.repository.LectureRepository;
import com.example.liveklass.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentService {

    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public void subEnrollment(Long lectureId, String userName) {

        Member user = memberValid(userName);
        Lecture lecture = lectureValid(lectureId);

        if (lecture.getCreator().equals(user)) {
            throw new CustomException(ErrorCode.CANNOT_ENROLL_OWN_LECTURE);
        }

        if (enrollmentRepository.existsByMemberAndLecture(user, lecture)) {
            throw new CustomException(ErrorCode.ALREADY_ENROLLED);
        }

        if (lecture.getLectureStatus() != LectureStatus.OPEN) {
            throw new CustomException(ErrorCode.SALE_PERIOD_EXPIRED);
        }

        lecture.increaseCurrentEnrollmentCount(LocalDateTime.now());

        Enrollment enrollment = Enrollment.builder()
                .member(user)
                .lecture(lecture)
                .build();

        enrollmentRepository.save(enrollment);
    }

    public Member memberValid(String userName) {
        return memberRepository.findByUserName(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Lecture lectureValid(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new CustomException(ErrorCode.LECTURE_NOT_FOUND));
    }
}
