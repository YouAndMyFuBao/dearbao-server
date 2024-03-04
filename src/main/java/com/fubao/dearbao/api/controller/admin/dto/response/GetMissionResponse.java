package com.fubao.dearbao.api.controller.admin.dto.response;

import com.fubao.dearbao.domain.mission.entity.MissionState;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetMissionResponse {
    Long id;
    String date;
    String content;
    String message;
    String constructor;
    String openStatus;
    @Builder
    public GetMissionResponse(Long id, String date, String content, String message, String constructor,
        String openStatus) {
        this.id = id;
        this.date = date;
        this.content = content;
        this.message = message;
        this.constructor = constructor;
        this.openStatus = openStatus;
    }
}
