package com.example.liveklass.dto.lecture;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

public record MyLectureSearchResponse(

        @Schema(description = "강의 목록 데이터")
        List<MyLectureListDto> contents,

        @Schema(description = "현재 페이지 번호", example = "0")
        int pageNumber,

        @Schema(description = "페이지 당 데이터 수", example = "10")
        int pageSize,

        @Schema(description = "전체 데이터 개수", example = "100")
        long totalElements,

        @Schema(description = "전체 페이지 수", example = "10")
        int totalPages,

        @Schema(description = "마지막 페이지 여부", example = "false")
        boolean isLast
) {
    public static MyLectureSearchResponse from(Page<MyLectureListDto> page) {
        return new MyLectureSearchResponse(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
