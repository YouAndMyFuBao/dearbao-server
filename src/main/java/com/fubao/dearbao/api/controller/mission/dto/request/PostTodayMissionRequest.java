package com.fubao.dearbao.api.controller.mission.dto.request;

import com.fubao.dearbao.api.service.mission.dto.PostTodayMissionDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostTodayMissionRequest {
    String content;

    public PostTodayMissionRequest(String content) {
        this.content = content;
    }

    public PostTodayMissionDto toService(Long memberId) {
        return PostTodayMissionDto.of(content,memberId);
    }
}
