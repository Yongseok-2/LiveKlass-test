package com.example.liveklass.dto.lecture;

import com.example.liveklass.domain.Vod;
import io.swagger.v3.oas.annotations.media.Schema;

public record VodListDto(

        @Schema(description = "Vod 아이디", example = "1")
        Long id,

        @Schema(description = "Vod 제목", example = "Spring 강의 1")
        String title,

        @Schema(description = "Vod 조회수", example = "100")
        long viewCount
) {
    public static VodListDto from(Vod vod) {
        return new VodListDto(
                vod.getId(),
                vod.getTitle(),
                vod.getViewCount());
    }
}
