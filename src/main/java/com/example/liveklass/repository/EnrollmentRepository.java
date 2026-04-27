package com.example.liveklass.repository;

import com.example.liveklass.domain.Enrollment;
import com.example.liveklass.domain.EnrollmentStatus;
import com.example.liveklass.domain.Lecture;
import com.example.liveklass.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @EntityGraph(attributePaths = {"member"})
    Page<Enrollment> findAllByLectureIdAndStatusNotAndStatusNot(Long lectureId, Pageable pageable, EnrollmentStatus enrollmentStatus1, EnrollmentStatus enrollmentStatus2);

    boolean existsByMemberAndLectureAndStatusNot(Member member, Lecture lecture, EnrollmentStatus enrollmentStatus);

    Optional<Enrollment> findByMemberAndLecture(Member member, Lecture lecture);

    @EntityGraph(attributePaths = {"lecture.creator", "lecture"})
    Page<Enrollment> findAllByMemberId(Long id, Pageable pageable);

    @EntityGraph(attributePaths = {"lecture.creator", "lecture"})
    Page<Enrollment> findAllByMemberIdAndStatusNotAndPaidAmountIsNotNull(Long id, EnrollmentStatus enrollmentStatus, Pageable pageable);

    @EntityGraph(attributePaths = {"member", "lecture"})
    Optional<Enrollment> findWithMemberAndLectureById(Long id);

    @EntityGraph(attributePaths = {"member", "lecture"})
    Optional<Enrollment> findFirstByLectureIdAndStatusOrderByCreatedAtAsc(Long lectureId, EnrollmentStatus enrollmentStatus);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Enrollment e SET e.status = :newStatus " +
            "WHERE e.id = :id AND e.status = :oldStatus")
    int updateStatusWithCondition(@Param("id") Long id,
                                  @Param("oldStatus") EnrollmentStatus oldStatus,
                                  @Param("newStatus") EnrollmentStatus newStatus);

    @EntityGraph(attributePaths = {"member", "lecture"})
    Optional<Enrollment> findByMemberUserNameAndLectureId(String userName, Long lectureId);

    @EntityGraph(attributePaths = {"member", "lecture"})
    List<Enrollment> findAllByStatusAndCreatedAtBefore(EnrollmentStatus enrollmentStatus, LocalDateTime treeDaysAgo);

}
