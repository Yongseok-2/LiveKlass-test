package com.example.liveklass.service;

import com.example.liveklass.domain.*;
import com.example.liveklass.dto.creator.CurrentEnrollmentListDto;
import com.example.liveklass.dto.creator.MyLectureDetailResponse;
import com.example.liveklass.dto.creator.MyLectureListDto;
import com.example.liveklass.dto.creator.MyLectureSearchRequest;
import com.example.liveklass.dto.lecture.LectureCreateRequest;
import com.example.liveklass.dto.lecture.LectureUpdateRequest;
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
public class CreatorService {

    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final EnrollmentRepository enrollmentRepository;

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
    public Long openLecture(Long lectureId, String userName) {
        Lecture lecture = memberAndLectureValid(lectureId, userName);

        lecture.openLecture();

        return lecture.getId();
    }

    @Transactional
    public Long updateLecture(LectureUpdateRequest request, Long lectureId, String userName) {

        Lecture lecture = memberAndLectureValid(lectureId, userName);

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

        Lecture lecture = memberAndLectureValid(lectureId, userName);

        lecture.closeLecture();
    }

    @Transactional
    public void deleteLecture(Long lectureId, String userName) {

        Lecture lecture = memberAndLectureValid(lectureId, userName);

        lectureRepository.delete(lecture);
    }

    @Transactional
    public Page<MyLectureListDto> getMyLectureList(MyLectureSearchRequest request, String userName, LectureStatus lectureStatus) {

        Member creator = memberRepository.findByUserName(userName)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if(creator.getRole() != (MemberRole.CREATOR)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Pageable pageable = request.toPageable();

        Page<Lecture> lecturePage = lectureRepository.findAllByCreator_UserNameAndLectureStatus(userName, lectureStatus, pageable);

        return lecturePage.map(MyLectureListDto::from);
    }

    @Transactional
    public MyLectureDetailResponse getMyLecture(Long lectureId, String userName, Pageable pageable) {

        Lecture lecture = memberAndLectureValid(lectureId, userName);

        Page<Enrollment> enrollmentPage = enrollmentRepository.findAllByLectureId(lectureId, pageable);

        Page<CurrentEnrollmentListDto> enrollmentDtoPage = enrollmentPage.map(CurrentEnrollmentListDto::from);

        return MyLectureDetailResponse.from(lecture, enrollmentDtoPage);
    }

    @Transactional
    public Lecture memberAndLectureValid(Long lectureId, String userName) {
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

        return lecture;
    }

}
