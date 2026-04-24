package com.example.liveklass.repository;

import com.example.liveklass.domain.Enrollment;
import com.example.liveklass.domain.Lecture;
import com.example.liveklass.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Page<Enrollment> findAllByLectureId(Long lectureId, Pageable pageable);
    boolean existsByMemberAndLecture(Member member, Lecture lecture);
}
