package com.fubao.dearbao.api.controller.mission.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DailyMissionResponse extends DailyMissionBaseResponse {

    private String remainingTime;

    public DailyMissionResponse(DailyMissionBaseResponse dailyMissionBaseResponse,
        String remainingTime) {
        super(dailyMissionBaseResponse);
        this.remainingTime = remainingTime;
    }

    public static DailyMissionResponse of(DailyMissionBaseResponse dailyMissionBaseResponse,
        String remainingTime) {
        return new DailyMissionResponse(dailyMissionBaseResponse, remainingTime);
    }
}
