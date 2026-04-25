package com.example.liveklass.controller;

import com.example.liveklass.document.LectureApiDocument;
import com.example.liveklass.dto.global.ApiResponse;
import com.example.liveklass.dto.global.PagedResponse;
import com.example.liveklass.dto.lecture.*;
import com.example.liveklass.service.LectureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Lecture", description = "강의 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lectures")
public class LectureController {

    private final LectureService lectureService;

    @LectureApiDocument.ListErrorResponse
    @Operation(summary = "강의 목록 조회", description = "등록된 강의의 목록을 페이징하여 반환합니다.")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<PagedResponse<LectureListDto>>> getLectureList(
            @ParameterObject  @Valid @ModelAttribute LectureSearchRequest request) {

        Page<LectureListDto> page = lectureService.getLectureList(request);

        return ResponseEntity.ok(ApiResponse.ok(PagedResponse.from(page)));
    }

    @LectureApiDocument.DetailErrorResponse
    @Operation(summary = "강의 상세 조회", description = "강의 ID를 이용해 상세 정보 및 VOD 목록을 조회합니다.")
    @GetMapping("/{lectureId}")
    public ResponseEntity<ApiResponse<LectureDetailResponse>> getLectureDetail(
            @PathVariable Long lectureId,
            @Parameter(hidden = true) @SessionAttribute(name = "userName", required = false) String userName) {

        LectureDetailResponse response = lectureService.getLectureDetail(lectureId, userName);

        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
