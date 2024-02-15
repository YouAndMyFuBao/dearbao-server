package com.fubao.dearbao.api.service.mission;

import com.fubao.dearbao.api.controller.mission.dto.response.DailyMissionBaseResponse;
import java.time.LocalDateTime;

public interface MissionService {

    DailyMissionBaseResponse dailyMission(Long memberId, LocalDateTime localDateTime);
}
