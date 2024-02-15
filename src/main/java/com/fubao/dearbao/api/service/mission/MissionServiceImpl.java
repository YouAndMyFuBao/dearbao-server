package com.fubao.dearbao.api.service.mission;

import com.fubao.dearbao.api.controller.mission.dto.response.DailyMessageResponse;
import com.fubao.dearbao.api.controller.mission.dto.response.DailyMissionBaseResponse;
import com.fubao.dearbao.api.controller.mission.dto.response.DailyMissionResponse;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberRepository;
import com.fubao.dearbao.domain.member.MemberState;
import com.fubao.dearbao.domain.mission.MemberMissionRepository;
import com.fubao.dearbao.domain.mission.MissionRepository;
import com.fubao.dearbao.domain.mission.entity.MemberMissionState;
import com.fubao.dearbao.domain.mission.entity.Mission;
import com.fubao.dearbao.domain.mission.entity.MissionState;
import com.fubao.dearbao.global.common.exception.CustomException;
import com.fubao.dearbao.global.common.exception.ResponseCode;
import com.fubao.dearbao.global.common.vo.MissionTime;
import com.fubao.dearbao.global.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {

    private final MemberRepository memberRepository;
    private final MemberMissionRepository memberMissionRepository;
    private final MissionRepository missionRepository;
    private final DateUtil dateUtil;

    @Override
    public DailyMissionBaseResponse dailyMission(Long memberId, LocalDateTime localDateTime) {
        Member member = findMemberById(memberId);
        Mission mission = findTodayMission(localDateTime.toLocalDate());
        boolean isMessageOpenTime = isMessageOpenTime(localDateTime.toLocalTime());
        boolean isMissionSuccess = isSuccess(memberId);
        DailyMissionBaseResponse baseResponse = DailyMissionBaseResponse.of(isMissionSuccess,
            isMessageOpenTime, member.getName());

        // 매세지가 오픈된 시간
        if (isMessageOpenTime) {
            if (isMissionSuccess) {
                return DailyMessageResponse.of(baseResponse,
                    mission.getAnswer());
            }
            // 미션이 되기 전 시간이다.
            return DailyMessageResponse.of(baseResponse,
                "내일 편지 보내줘");
        }
        // 미션을 성공했다.
        if (isMissionSuccess) {
            return DailyMissionResponse.of(baseResponse,
                dateUtil.toResponseTimeFormat(MissionTime.PM.getTime()));
        }
        return DailyMissionResponse.of(baseResponse,
            dateUtil.toResponseTimeFormat(MissionTime.PM.getTime()));
    }

    private Mission findTodayMission(LocalDate now) {
        return missionRepository.findByOpenAtAndState(now, MissionState.ACTIVE)
            .orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_MISSION)
            );
    }

    private boolean isSuccess(Long memberId) {
        return memberMissionRepository.findByMemberIdAndState(memberId, MemberMissionState.ACTIVE)
            .isPresent();
    }

    private boolean isMessageOpenTime(LocalTime now) {
        return MissionTime.isMessageOpenTime(now);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findByIdAndState(memberId, MemberState.ACTIVE)
            .orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_MEMBER)
            );
    }

}
