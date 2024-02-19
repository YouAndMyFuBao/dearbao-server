package com.fubao.dearbao.api.controller.mission.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMyMissionResponse {

    private String date;
    private String content;

    @Builder
    public GetMyMissionResponse(String date, String content) {
        this.date = date;
        this.content = content;
    }

    public static GetMyMissionResponse of(String date, String content) {
        return GetMyMissionResponse.builder()
            .content(content)
            .date(date)
            .build();
    }
}
