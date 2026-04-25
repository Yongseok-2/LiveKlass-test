package com.example.liveklass.repository;

import com.example.liveklass.domain.Lecture;
import com.example.liveklass.domain.LectureStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    Page<Lecture> findAllByCreator_UserNameAndLectureStatus(String userName, LectureStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"creator"})
    Page<Lecture> findAllByLectureStatus(LectureStatus lectureStatus, Pageable pageable);

    @EntityGraph(attributePaths = {"creator"})
    Page<Lecture> findAllByTitleContainingAndLectureStatus(String title, LectureStatus lectureStatus, Pageable pageable);
}
