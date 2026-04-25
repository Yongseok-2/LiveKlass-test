package com.example.liveklass.service;

import com.example.liveklass.domain.Lecture;
import com.example.liveklass.domain.LectureStatus;
import com.example.liveklass.dto.lecture.LectureListDto;
import com.example.liveklass.dto.lecture.LectureSearchRequest;
import com.example.liveklass.repository.LectureRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureService {

    private final LectureRepository lectureRepository;


    public Page<LectureListDto> getLectureList(@Valid LectureSearchRequest request) {

        Pageable pageable = request.toPageable();

        Page<Lecture> lecturePage;
        String title = request.title();

        if (title == null || title.isBlank()) {
            lecturePage = lectureRepository.findAllByLectureStatus(LectureStatus.OPEN, pageable);
        } else {
            lecturePage = lectureRepository.findAllByTitleContainingAndLectureStatus(
                    title, LectureStatus.OPEN, pageable);
        }

        return lecturePage.map(LectureListDto::from);
    }
}
