package com.fubao.dearbao.api.controller.mission.dto.response;

import com.fubao.dearbao.domain.mission.entity.Mission;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetTodayMissionResponse {
    String content;
    Long id;
    @Builder
    public GetTodayMissionResponse(String content, Long id) {
        this.content = content;
        this.id = id;
    }

    public static GetTodayMissionResponse of(Mission mission) {
        return GetTodayMissionResponse
            .builder()
            .content(mission.getContent())
            .id(mission.getId())
            .build();
    }
}
