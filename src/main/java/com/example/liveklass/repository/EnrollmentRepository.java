package com.example.liveklass.repository;

import com.example.liveklass.domain.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Page<Enrollment> findAllByLectureId(Long lectureId, Pageable pageable);
}
