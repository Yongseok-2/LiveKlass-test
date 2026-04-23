package com.example.liveklass.dto.lecture;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record LectureSearchRequest(

        @Schema(description = "강의 제목 검색어", example = "Spring")
        String title,

        @Min(0)
        @Schema(description = "페이지 번호 (기본 0부터 시작)", example = "0")
        Integer page,

        @Min(1)
        @Schema(description = "페이지 당 항목 수 (기본 10)", example = "10")
        Integer size,

        @Schema(description = "정렬 방향 (ASC: 오래된순, DESC: 최신순 / 기본 DESC)", example = "DESC")
        String direction
) {
    public Pageable toPageable() {
        int p = (page == null) ? 0 : page;
        int s = (size == null) ? 10 : size;

        Sort.Direction dir = Sort.Direction.DESC;
        if (direction != null && direction.equalsIgnoreCase("ASC")) {
            dir = Sort.Direction.ASC;
        }

        return PageRequest.of(p, s, Sort.by(dir, "createdAt"));
    }
}
