package com.example.liveklass.repository;

import com.example.liveklass.domain.EnrollmentStatus;
import com.example.liveklass.domain.Lecture;
import com.example.liveklass.domain.LectureStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface  LectureRepository extends JpaRepository<Lecture, Long> {

    @Override
    @EntityGraph(attributePaths = {"creator"})
    Optional<Lecture> findById(Long lectureId);

    @EntityGraph(attributePaths = {"creator"})
    Page<Lecture> findAllByCreator_UserNameAndLectureStatus(String userName, LectureStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"creator"})
    Page<Lecture> findAllByLectureStatus(LectureStatus lectureStatus, Pageable pageable);

    @EntityGraph(attributePaths = {"creator"})
    Page<Lecture> findAllByTitleContainingAndLectureStatus(String title, LectureStatus lectureStatus, Pageable pageable);

    @EntityGraph(attributePaths = {"creator"})
    Page<Lecture> findAllByCreator_UserName(String userName, Pageable pageable);

    @Modifying
    @Query("UPDATE Lecture l SET l.currentEnrollmentCount = l.currentEnrollmentCount + 1 " +
            "WHERE l.id = :id AND l.currentEnrollmentCount < l.maxCapacity")
    int increaseEnrollmentCountWithCondition(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Lecture l SET l.currentEnrollmentCount = l.currentEnrollmentCount - 1 " +
            "WHERE l.id = :id AND l.currentEnrollmentCount > 0")
    void decreaseEnrollmentCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Lecture l SET l.waitCount = l.waitCount + 1 WHERE l.id = :id")
    void increaseWaitCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Lecture l SET l.waitCount = l.waitCount - 1 " +
            "WHERE l.id = :id AND l.waitCount > 0")
    void decreaseWaitCount(@Param("id") Long id);
}
