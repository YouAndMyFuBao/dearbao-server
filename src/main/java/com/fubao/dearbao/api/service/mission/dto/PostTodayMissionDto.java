package com.fubao.dearbao.api.service.mission.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostTodayMissionDto {

    String content;
    Long memberId;

    @Builder
    public PostTodayMissionDto(String content, Long memberId) {
        this.content = content;
        this.memberId = memberId;
    }

    public static PostTodayMissionDto of(String content, Long memberId) {
        return PostTodayMissionDto
            .builder()
            .content(content)
            .memberId(memberId)
            .build();
    }
}
