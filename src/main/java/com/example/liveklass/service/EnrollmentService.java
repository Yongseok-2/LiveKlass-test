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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

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

        if (lecture.getLectureStatus() != LectureStatus.OPEN) {
            throw new CustomException(ErrorCode.SALE_PERIOD_EXPIRED);
        }

        lecture.canIncreaseCount(LocalDateTime.now());

        if (enrollmentRepository.existsByMemberAndLectureAndStatusNot(user, lecture, EnrollmentStatus.CANCELLED)) {
            throw new CustomException(ErrorCode.ALREADY_ENROLLED);
        }

        int updatedRows = lectureRepository.increaseEnrollmentCountWithCondition(lectureId);

        if (updatedRows > 0) {
            Enrollment enrollment = Enrollment.builder()
                    .member(user)
                    .lecture(lecture)
                    .status(EnrollmentStatus.PENDING)
                    .build();

            enrollmentRepository.save(enrollment);
        } else {
            Enrollment enrollment = Enrollment.builder()
                    .member(user)
                    .lecture(lecture)
                    .status(EnrollmentStatus.WAITLISTED)
                    .build();

            lectureRepository.increaseWaitCount(lectureId);

            enrollmentRepository.save(enrollment);
        }
    }

    @Transactional
    public void confirmEnrollment(@Valid PaymentRequest request, Long enrollmentId, String userName) {

        Member user = memberValid(userName);
        Enrollment enrollment = enrollmentRepository.findWithMemberAndLectureById(enrollmentId)
                .orElseThrow(() -> new CustomException(ErrorCode.ENROLLMENT_NOT_FOUND));

        if (!Objects.equals(enrollment.getMember().getUserName(), user.getUserName())) {
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

        Enrollment enrollment = enrollmentRepository.findWithMemberAndLectureById(enrollmentId)
                .orElseThrow(() -> new CustomException(ErrorCode.ENROLLMENT_NOT_FOUND));

        Long lectureId = enrollment.getLecture().getId();

        if (!Objects.equals(enrollment.getMember().getUserName(), user.getUserName())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        int isEnrollment =  enrollment.cancel(LocalDateTime.now());

        if(isEnrollment > 0) {
            lectureRepository.decreaseEnrollmentCount(lectureId);
            promoteWaitlistedUser(lectureId);
        }else {
            lectureRepository.decreaseWaitCount(lectureId);
        }
    }

    private void promoteWaitlistedUser(Long lectureId) {

        Optional<Enrollment> nextWaitlist = enrollmentRepository
                .findFirstByLectureIdAndStatusOrderByCreatedAtAsc(lectureId, EnrollmentStatus.WAITLISTED);

        // 1. 대기자가 한 명도 없으면 종료
        if (nextWaitlist.isEmpty()) {
            return;
        }

        Enrollment target = nextWaitlist.get();

        // 2. 내가 이 사람을 가로챌 수 있는지 확인 (WAITLISTED -> PENDING)
        int updatedRows = enrollmentRepository.updateStatusWithCondition(
                target.getId(),
                EnrollmentStatus.WAITLISTED,
                EnrollmentStatus.PENDING
        );

        if (updatedRows == 1) {
            // 성공하면 강의 인원을 1 올리고, 대기자 수를 1 줄입니다.
            lectureRepository.increaseEnrollmentCountWithCondition(lectureId);
            lectureRepository.decreaseWaitCount(lectureId);
            System.out.println("승격 성공: " + target.getMember().getUserName());
        } else {
            // 다른 취소 스레드가 이 사람을 채갔습니다.
            // 그다음 대기자를 찾기 위해 다시 처음부터 시작합니다.
            promoteWaitlistedUser(lectureId);
        }
    }

    public void promoteMultipleWaitlistedUsers(Long lectureId) {

        while (true) {
            Lecture lecture = lectureRepository.findById(lectureId)
                    .orElseThrow(() -> new CustomException(ErrorCode.LECTURE_NOT_FOUND));

            if (lecture.getCurrentEnrollmentCount() >= lecture.getMaxCapacity() || lecture.getWaitCount() <= 0) {
                break;
            }

            promoteWaitlistedUser(lectureId);
        }
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

        Page<Enrollment> enrollmentPage = enrollmentRepository.findAllByMemberIdAndStatusNotAndPaidAmountIsNotNull(member.getId(),
                EnrollmentStatus.PENDING, pageable);

        return enrollmentPage.map(PaymentHistoryResponse::from);
    }

    @Transactional
    public void schedulerCancel(Long enrollmentId) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new CustomException(ErrorCode.ENROLLMENT_NOT_FOUND));
        Long lectureId = enrollment.getLecture().getId();

        int isEnrollment = enrollment.cancel(LocalDateTime.now());

        if (isEnrollment > 0) {
            lectureRepository.decreaseEnrollmentCount(lectureId);
            promoteWaitlistedUser(lectureId);
        } else {
            lectureRepository.decreaseWaitCount(lectureId);
        }
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
