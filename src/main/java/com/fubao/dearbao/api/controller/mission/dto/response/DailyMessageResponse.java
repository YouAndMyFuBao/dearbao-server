package com.fubao.dearbao.api.controller.mission.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DailyMessageResponse extends DailyMissionBaseResponse {

    private String message;

    public DailyMessageResponse(DailyMissionBaseResponse dailyMissionBaseResponse, String message) {
        super(dailyMissionBaseResponse);
        this.message = message;
    }

    public static DailyMessageResponse of(DailyMissionBaseResponse dailyMissionBaseResponse,
        String message) {
        return new DailyMessageResponse(dailyMissionBaseResponse, message);
    }
}
