package com.example.liveklass.controller;

import com.example.liveklass.document.CreatorApiDocument;
import com.example.liveklass.domain.LectureStatus;
import com.example.liveklass.dto.global.ApiResponse;
import com.example.liveklass.dto.global.PagedResponse;
import com.example.liveklass.dto.creator.MyLectureDetailResponse;
import com.example.liveklass.dto.creator.MyLectureListDto;
import com.example.liveklass.dto.creator.MyLectureSearchRequest;
import com.example.liveklass.dto.lecture.LectureCreateRequest;
import com.example.liveklass.dto.lecture.LectureUpdateRequest;
import com.example.liveklass.service.CreatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    @Operation(summary = "강의 오픈", description = "기존 강의에 임시로 등록 되어있던 강의를 오픈합니다.")
    @PatchMapping("/{lectureId}/open")
    public ResponseEntity<ApiResponse<Long>> openLecture(
            @PathVariable Long lectureId,
            @SessionAttribute(name = "userName") String userName) {

        Long openId = creatorService.openLecture(lectureId, userName);

        return ResponseEntity.ok(ApiResponse.ok(openId));
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
    public ResponseEntity<ApiResponse<Void>> closeLecture(
            @PathVariable Long lectureId,
            @SessionAttribute(name = "userName") String userName) {

        creatorService.closeLecture(lectureId, userName);

        return ResponseEntity.ok(ApiResponse.ok());
    }

    @CreatorApiDocument.DeleteLectureErrorResponse
    @Operation(summary = "강의 삭제", description = "기존 강의를 삭제합니다.")
    @DeleteMapping("/{lectureId}")
    public ResponseEntity<ApiResponse<Void>> deleteLecture(
            @PathVariable Long lectureId,
            @SessionAttribute(name = "userName") String userName) {

        creatorService.deleteLecture(lectureId, userName);

        return ResponseEntity.ok(ApiResponse.ok());
    }

    @CreatorApiDocument.GetLectureListErrorResponse
    @Operation(summary = "내가 생성한 강의 목록", description = "내가 생성한 강의를 페이징하여 반환합니다.")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<PagedResponse<MyLectureListDto>>> getMyLectureList(
            @Valid @ModelAttribute MyLectureSearchRequest request,
            @SessionAttribute(name = "userName") String userName,
            LectureStatus lectureStatus) {

        Page<MyLectureListDto> page = creatorService.getMyLectureList(request, userName, lectureStatus);

        return ResponseEntity.ok(ApiResponse.ok(PagedResponse.from(page)));
    }

    @CreatorApiDocument.GetLectureDetailErrorResponse
    @Operation(summary = "내가 생성한 강의 상세보기", description = "내가 생성한 강의의 정보를 반환합니다")
    @GetMapping("/{lectureId}/detail")
    public ResponseEntity<ApiResponse<MyLectureDetailResponse>> getMyLecture(
            @PathVariable Long lectureId,
            @SessionAttribute(name = "userName") String userName,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        MyLectureDetailResponse response = creatorService.getMyLecture(lectureId, userName, pageable);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

}
