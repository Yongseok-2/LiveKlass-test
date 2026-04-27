package com.example.liveklass.service;

import com.example.liveklass.domain.Enrollment;
import com.example.liveklass.domain.EnrollmentStatus;
import com.example.liveklass.repository.EnrollmentRepository;
import com.example.liveklass.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class EnrollmentScheduler {

    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentService enrollmentService;

    @Scheduled(cron = "0 0 * * * *")
    public void cleanupExpiredEnrollments() {
        LocalDateTime treeDaysAgo = LocalDateTime.now().minusDays(3);

        List<Enrollment> expiredList = enrollmentRepository.findAllByStatusAndCreatedAtBefore(
                EnrollmentStatus.PENDING, treeDaysAgo);

        for (Enrollment enrollment : expiredList) {
            try {
                enrollmentService.schedulerCancel(enrollment.getId());
                System.out.println("자동 취소 및 승격 완료: Enrollment ID " + enrollment.getId());
            } catch (Exception e) {
                log.error("자동 취소 중 오류 발생: " + enrollment.getId(), e);
            }
        }
    }
}
