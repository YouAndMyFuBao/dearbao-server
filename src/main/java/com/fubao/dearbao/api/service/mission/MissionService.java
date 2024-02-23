package com.fubao.dearbao.api.service.mission;

import com.fubao.dearbao.api.controller.mission.dto.response.DailyMissionBaseResponse;
import com.fubao.dearbao.api.controller.mission.dto.response.GetMyMissionResponse;
import com.fubao.dearbao.api.controller.mission.dto.response.GetTodayMissionResponse;
import com.fubao.dearbao.api.service.mission.dto.PostTodayMissionDto;

import java.time.LocalDateTime;
import java.util.List;

public interface MissionService {

    DailyMissionBaseResponse dailyMission(Long memberId, LocalDateTime localDateTime);

    void setDailyMission();

    List<GetMyMissionResponse> getMyMission(Long memberId);

    GetTodayMissionResponse getTodayMission();

    void postTodayMission(PostTodayMissionDto service);
}
