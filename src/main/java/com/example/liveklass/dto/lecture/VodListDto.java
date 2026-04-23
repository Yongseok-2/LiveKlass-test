package com.example.liveklass.dto.lecture;

import com.example.liveklass.domain.Vod;

public record VodListDto(
        Long id,
        String title,
        long viewCount
) {
    public static VodListDto from(Vod vod) {
        return new VodListDto(
                vod.getId(),
                vod.getTitle(),
                vod.getViewCount());
    }
}
