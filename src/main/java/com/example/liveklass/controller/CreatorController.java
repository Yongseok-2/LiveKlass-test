package com.example.liveklass.controller;

import com.example.liveklass.dto.ApiResponse;
import com.example.liveklass.dto.creator.MyLectureDetailResponse;
import com.example.liveklass.dto.creator.MyLectureSearchRequest;
import com.example.liveklass.dto.creator.MyLectureSearchResponse;
import com.example.liveklass.dto.lecture.LectureCreateRequest;
import com.example.liveklass.dto.lecture.LectureUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Creator", description = "강사 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/creator/lectures")
public class CreatorController {

    @Operation(summary = "강의 등록", description = "새로운 강의를 생성하고 생성된 강의의 ID를 반환합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createLecture(@Valid @RequestBody LectureCreateRequest request) {

        // TODO: lectureService.createLecture(request);

        Long dummyId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(dummyId));
    }

    @Operation(summary = "강의 수정", description = "기존 강의를 수정하고 생성된 강의의 ID를 반환합니다.")
    @PutMapping("/{lectureId}")
    public ResponseEntity<ApiResponse<Long>> updateLecture(@Valid @RequestBody LectureUpdateRequest request, @PathVariable Long lectureId) {

        // TODO: lectureService.updateLecture(request, lectureId);

        Long dummyId = 1L;
        return ResponseEntity.ok(ApiResponse.ok(dummyId));
    }

    @Operation(summary = "강의 종료", description = "강의 상태를 CLOSED로 변경합니다.")
    @PatchMapping("/{lectureId}/close")
    public ResponseEntity<ApiResponse<Void>> closeLecture(@PathVariable Long lectureId) {

        // TODO: lectureService.closeLecture(lectureId)

        return ResponseEntity.ok(ApiResponse.ok());
    }

    @Operation(summary = "강의 삭제", description = "기존 강의를 삭제합니다.")
    @DeleteMapping("/{lectureId}")
    public ResponseEntity<ApiResponse<Void>> deleteLecture(@PathVariable Long lectureId) {

        // TODO: lectureService.deleteLecture(lectureId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @Operation(summary = "내가 생성한 강의 목록", description = "내가 생성한 강의를 페이징하여 반환합니다.")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<MyLectureSearchResponse>> getMyLectureList(@Valid @ModelAttribute MyLectureSearchRequest request) {

        // TODO: creatorService.getMyLectureList(request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "내가 생성한 강의 상세보기", description = "내가 생성한 강의의 정보를 반환합니다")
    @GetMapping("/{lectureId}/detail")
    public ResponseEntity<ApiResponse<MyLectureDetailResponse>> getMyLecture(@PathVariable Long lectureId) {

        // TODO: creatorService.getMyLecture(lectureId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
