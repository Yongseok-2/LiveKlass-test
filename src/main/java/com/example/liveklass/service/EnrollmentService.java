package com.example.liveklass.service;

import com.example.liveklass.domain.*;
import com.example.liveklass.dto.enrollment.MyEnrollmentListDto;
import com.example.liveklass.dto.enrollment.MyEnrollmentRequest;
import com.example.liveklass.dto.enrollment.PaymentRequest;
import com.example.liveklass.dto.payment.PaymentHistoryRequest;
import com.example.liveklass.dto.payment.PaymentHistoryResponse;
import com.example.liveklass.global.error.CustomException;
import com.example.liveklass.global.error.ErrorCode;
import com.example.liveklass.repository.EnrollmentRepository;
import com.example.liveklass.repository.LectureRepository;
import com.example.liveklass.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    @Transactional
    public void confirmEnrollment(@Valid PaymentRequest request, Long enrollmentId, String userName) {

        Member user = memberValid(userName);
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new CustomException(ErrorCode.ENROLLMENT_NOT_FOUND));

        if(!enrollment.getMember().getUserName().equals(user.getUserName())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        if (!enrollment.getLecture().getBasePrice().equals(request.paidAmount())) {
            throw new CustomException(ErrorCode.INVALID_PAYMENT_AMOUNT);
        }

        enrollment.confirmEnrollment(LocalDateTime.now(), request.paidAmount());
    }

    @Transactional
    public void cancelEnrollment(Long enrollmentId, String userName) {

        Member user = memberValid(userName);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new CustomException(ErrorCode.ENROLLMENT_NOT_FOUND));

        if(!enrollment.getMember().getUserName().equals(user.getUserName())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        enrollment.cancel(LocalDateTime.now());
    }

    public Page<MyEnrollmentListDto> getEnrollmentList(MyEnrollmentRequest request, String userName) {

        Member member = memberValid(userName);

        Pageable pageable = request.toPageable();

        Page<Enrollment> enrollmentPage = enrollmentRepository.findAllByMemberId(member.getId(), pageable);

        return enrollmentPage.map(MyEnrollmentListDto::from);
    }

    public Page<PaymentHistoryResponse> getPaymentHistory(@Valid PaymentHistoryRequest request, String userName) {
        Member member = memberValid(userName);

        Pageable pageable = request.toPageable();

        Page<Enrollment> enrollmentPage = enrollmentRepository.findAllByMemberIdAndStatusNot(member.getId(), EnrollmentStatus.PENDING, pageable);

        return enrollmentPage.map(PaymentHistoryResponse::from);
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
