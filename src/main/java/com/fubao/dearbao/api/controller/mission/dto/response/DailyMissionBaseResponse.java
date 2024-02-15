package com.fubao.dearbao.api.controller.mission.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
public class DailyMissionBaseResponse {

    private boolean isMissionSuccess;
    private boolean isMessageOpenTime;
    private String nickname;

    @Builder
    public DailyMissionBaseResponse(boolean isMissionSuccess, boolean isMessageOpenTime,
        String nickname) {
        this.isMissionSuccess = isMissionSuccess;
        this.isMessageOpenTime = isMessageOpenTime;
        this.nickname = nickname;
    }

    public DailyMissionBaseResponse(DailyMissionBaseResponse response) {
        this.isMissionSuccess = response.isMissionSuccess;
        this.isMessageOpenTime = response.isMessageOpenTime;
        this.nickname = response.nickname;
    }

    public static DailyMissionBaseResponse of(boolean isMissionSuccess, boolean isMessageOpenTime,
        String name) {
        return DailyMissionBaseResponse.builder()
            .isMessageOpenTime(isMessageOpenTime)
            .isMissionSuccess(isMissionSuccess)
            .nickname(name)
            .build();
    }
}
