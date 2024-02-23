package com.fubao.dearbao.api.controller.mission;

import com.fubao.dearbao.api.controller.mission.dto.response.DailyMissionBaseResponse;
import com.fubao.dearbao.api.controller.mission.dto.response.GetMyMissionResponse;
import com.fubao.dearbao.api.controller.mission.dto.response.GetTodayMissionResponse;
import com.fubao.dearbao.api.service.mission.MissionService;
import com.fubao.dearbao.global.common.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mission")
public class MissionController {

    private final MissionService missionService;

    @GetMapping
    public ResponseEntity<DataResponse<DailyMissionBaseResponse>> dailyMission() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(loggedInUser.getName());
        DailyMissionBaseResponse response = missionService.dailyMission(memberId,
            LocalDateTime.now());
        return ResponseEntity.ok(DataResponse.of(response));
    }

    @GetMapping("/my-mission")
    public ResponseEntity<DataResponse<List<GetMyMissionResponse>>> getMyMission() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(loggedInUser.getName());
        List<GetMyMissionResponse> response = missionService.getMyMission(memberId);
        return ResponseEntity.ok(DataResponse.of(response));
    }

    @GetMapping("daily")
    public ResponseEntity<DataResponse<GetTodayMissionResponse>> getTodayMission() {
        GetTodayMissionResponse response = missionService.getTodayMission();
        return ResponseEntity.ok(DataResponse.of(response));
    }
}
