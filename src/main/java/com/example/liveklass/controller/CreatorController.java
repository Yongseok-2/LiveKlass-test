package com.example.liveklass.controller;

import com.example.liveklass.document.CreatorApiDocument;
import com.example.liveklass.dto.global.ApiResponse;
import com.example.liveklass.dto.global.PagedResponse;
import com.example.liveklass.dto.creator.MyLectureDetailResponse;
import com.example.liveklass.dto.creator.MyLectureListDto;
import com.example.liveklass.dto.creator.MyLectureSearchRequest;
import com.example.liveklass.dto.lecture.LectureCreateRequest;
import com.example.liveklass.dto.lecture.LectureUpdateRequest;
import com.example.liveklass.dto.lecture.VodCreateRequest;
import com.example.liveklass.service.CreatorService;
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

    private final CreatorService creatorService;

    @CreatorApiDocument.CreateLectureErrorResponse
    @Operation(summary = "강의 등록", description = "새로운 강의를 생성하고 생성된 강의의 ID를 반환합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createLecture(
            @Valid @RequestBody LectureCreateRequest request,
            @SessionAttribute(name = "userName") String userName) {

        Long lectureId = creatorService.createLecture(request, userName);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(lectureId));
    }

    @CreatorApiDocument.UpdateLectureErrorResponse
    @Operation(summary = "강의 수정", description = "기존 강의를 수정하고 생성된 강의의 ID를 반환합니다.")
    @PutMapping("/{lectureId}")
    public ResponseEntity<ApiResponse<Long>> updateLecture(
            @Valid @RequestBody LectureUpdateRequest request,
            @PathVariable Long lectureId,
            @SessionAttribute(name = "userName") String userName) {

        Long updateId = creatorService.updateLecture(request, lectureId, userName);

        return ResponseEntity.ok(ApiResponse.ok(updateId));
    }

    @CreatorApiDocument.CloseLectureErrorResponse
    @Operation(summary = "강의 종료", description = "강의 상태를 CLOSED로 변경합니다.")
    @PatchMapping("/{lectureId}/close")
    public ResponseEntity<ApiResponse<Void>> closeLecture(@PathVariable Long lectureId) {

        // TODO: lectureService.closeLecture(lectureId)

        return ResponseEntity.ok(ApiResponse.ok());
    }

    @CreatorApiDocument.DeleteLectureErrorResponse
    @Operation(summary = "강의 삭제", description = "기존 강의를 삭제합니다.")
    @DeleteMapping("/{lectureId}")
    public ResponseEntity<ApiResponse<Void>> deleteLecture(@PathVariable Long lectureId) {

        // TODO: lectureService.deleteLecture(lectureId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @CreatorApiDocument.GetLectureListErrorResponse
    @Operation(summary = "내가 생성한 강의 목록", description = "내가 생성한 강의를 페이징하여 반환합니다.")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<PagedResponse<MyLectureListDto>>> getMyLectureList(@Valid @ModelAttribute MyLectureSearchRequest request) {

        // TODO: Page<MyLectureListDto> page = creatorService.getMyLectureList(request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @CreatorApiDocument.GetLectureDetailErrorResponse
    @Operation(summary = "내가 생성한 강의 상세보기", description = "내가 생성한 강의의 정보를 반환합니다")
    @GetMapping("/{lectureId}/detail")
    public ResponseEntity<ApiResponse<MyLectureDetailResponse>> getMyLecture(@PathVariable Long lectureId) {

        // TODO: creatorService.getMyLecture(lectureId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @CreatorApiDocument.DeleteLectureErrorResponse
    @Operation(summary = "영상 등록", description = "강의에 영상을 등록합니다.")
    @PostMapping("/{lectureId}/vod")
    public ResponseEntity<ApiResponse<Void>> uploadVod(@PathVariable Long lectureId, @Valid @RequestBody VodCreateRequest request) {

        // TODO: lectureService.uploadVod(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok());
    }
}
