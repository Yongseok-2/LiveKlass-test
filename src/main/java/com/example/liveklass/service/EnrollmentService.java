package com.example.liveklass.service;

import com.example.liveklass.domain.Lecture;
import com.example.liveklass.domain.Member;
import com.example.liveklass.domain.MemberRole;
import com.example.liveklass.global.error.CustomException;
import com.example.liveklass.global.error.ErrorCode;
import com.example.liveklass.repository.EnrollmentRepository;
import com.example.liveklass.repository.LectureRepository;
import com.example.liveklass.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private EnrollmentRepository enrollmentRepository;

    public void subEnrollment(Long lectureId, String userName) {
        memberAndLectureValid(lectureId, userName);


    }

    public void memberAndLectureValid(Long lectureId, String userName) {
        Member creator = memberRepository.findByUserName(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if(creator.getRole() != (MemberRole.CREATOR)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new CustomException(ErrorCode.LECTURE_NOT_FOUND));
    }
}
