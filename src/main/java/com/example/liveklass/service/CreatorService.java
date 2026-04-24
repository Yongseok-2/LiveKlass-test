package com.example.liveklass.service;

import com.example.liveklass.domain.Lecture;
import com.example.liveklass.domain.Member;
import com.example.liveklass.domain.MemberRole;
import com.example.liveklass.dto.lecture.LectureCreateRequest;
import com.example.liveklass.dto.lecture.LectureUpdateRequest;
import com.example.liveklass.global.error.CustomException;
import com.example.liveklass.global.error.ErrorCode;
import com.example.liveklass.repository.LectureRepository;
import com.example.liveklass.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatorService {

    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;

    @Transactional
    public Long createLecture(LectureCreateRequest request, String userName) {

        Member creator = memberRepository.findByUserName(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if(creator.getRole() != (MemberRole.CREATOR)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Lecture lecture = request.toEntity(creator);
        Lecture savedLecture = lectureRepository.save(lecture);

        return savedLecture.getId();
    }

    @Transactional
    public Long updateLecture(LectureUpdateRequest request, Long lectureId, String userName) {

        Member creator = memberRepository.findByUserName(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if(creator.getRole() != (MemberRole.CREATOR)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new CustomException(ErrorCode.LECTURE_NOT_FOUND));

        if (!lecture.getCreator().getUserName().equals(userName)) {
            throw new CustomException(ErrorCode.NOT_LECTURE_CREATOR);
        }

        lecture.updateBasicInfo(
                request.title(),
                request.description(),
                request.maxCapacity(),
                request.basePrice(),
                request.lectureType()
        );

        lecture.updateDates(
                request.salesStartAt(),
                request.salesEndAt(),
                request.lectureStartAt(),
                request.lectureEndAt()
        );

        return lecture.getId();
    }

    @Transactional
    public void closeLecture(Long lectureId, String userName) {

        Member creator = memberRepository.findByUserName(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if(creator.getRole() != (MemberRole.CREATOR)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new CustomException(ErrorCode.LECTURE_NOT_FOUND));

        if (!lecture.getCreator().getUserName().equals(userName)) {
            throw new CustomException(ErrorCode.NOT_LECTURE_CREATOR);
        }

        lecture.closeLecture();
    }

    @Transactional
    public void deleteLecture(Long lectureId, String userName) {

        Member creator = memberRepository.findByUserName(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if(creator.getRole() != (MemberRole.CREATOR)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new CustomException(ErrorCode.LECTURE_NOT_FOUND));

        if (!lecture.getCreator().getUserName().equals(userName)) {
            throw new CustomException(ErrorCode.NOT_LECTURE_CREATOR);
        }

        lectureRepository.delete(lecture);
    }
}
