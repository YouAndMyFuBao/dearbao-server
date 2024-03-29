package com.fubao.dearbao.api.service.mission;

import com.fubao.dearbao.api.controller.mission.dto.response.DailyMessageResponse;
import com.fubao.dearbao.api.controller.mission.dto.response.DailyMissionBaseResponse;
import com.fubao.dearbao.api.controller.mission.dto.response.DailyMissionResponse;
import com.fubao.dearbao.api.controller.mission.dto.response.GetMyMissionResponse;
import com.fubao.dearbao.api.controller.mission.dto.response.GetTodayMissionResponse;
import com.fubao.dearbao.api.service.mission.dto.PostTodayMissionDto;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberRepository;
import com.fubao.dearbao.domain.member.MemberState;
import com.fubao.dearbao.domain.mission.MemberMissionRepository;
import com.fubao.dearbao.domain.mission.MissionRepository;
import com.fubao.dearbao.domain.mission.entity.MemberMission;
import com.fubao.dearbao.domain.mission.entity.MemberMissionState;
import com.fubao.dearbao.domain.mission.entity.Mission;
import com.fubao.dearbao.domain.mission.entity.MissionState;
import com.fubao.dearbao.global.common.exception.CustomException;
import com.fubao.dearbao.global.common.exception.ResponseCode;
import com.fubao.dearbao.global.common.vo.MissionTime;
import com.fubao.dearbao.global.util.DateUtil;
import com.fubao.dearbao.global.util.SlackWebhookUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionServiceImpl implements MissionService {

    private final MemberRepository memberRepository;
    private final MemberMissionRepository memberMissionRepository;
    private final MissionRepository missionRepository;
    private final DateUtil dateUtil;
    private final SlackWebhookUtil slackWebhookUtil;

    @Override
    public DailyMissionBaseResponse dailyMission(Long memberId, LocalDateTime localDateTime) {
        Member member = findMemberById(memberId);
        Mission mission = findActiveMission();
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

    //추후 quartz로 변경
    @Transactional
    @Scheduled(cron = "00 00 09 * * *") // 매일 9시 00분 0초마다 실행
    public void setDailyMission() {
        Mission todayMission = findActiveMission();
        List<Mission> missionList = findInActiveMission();
        missionSetting(todayMission, missionList);
        deleteMemberMissionByTodayMission(todayMission);
    }

    @Override
    public List<GetMyMissionResponse> getMyMission(Long memberId) {
        List<MemberMission> memberMissions = findMemberMissionBy(memberId);
        return memberMissions.stream().map(
            memberMission -> GetMyMissionResponse.builder()
                .date(dateUtil.toResponseDateFormat(memberMission.getMission().getOpenAt()))
                .content(memberMission.getContent())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public GetTodayMissionResponse getTodayMission() {
        Mission mission = findActiveMission();
        return GetTodayMissionResponse.of(mission);
    }

    @Override
    @Transactional
    public void postTodayMission(PostTodayMissionDto service) {
        Member member = findMemberById(service.getMemberId());
        checkAlreadyMission(member.getId());
        Mission mission = findActiveMission();
        MemberMission memberMission = MemberMission.create(member, mission, service.getContent());
        memberMission.validate();
        memberMissionRepository.save(
            memberMission
        );
    }

    private void checkAlreadyMission(Long memberId) {
        if (findMemberMissionWithActive(memberId)) {
            throw new CustomException(ResponseCode.ALREADY_ACTIVE_DAILY_MISSION);
        }
    }

    private boolean findMemberMissionWithActive(Long memberId) {
        return memberMissionRepository.findByMemberIdAndState(memberId, MemberMissionState.ACTIVE)
            .isPresent();
    }

    private List<MemberMission> findMemberMissionBy(Long memberId) {
        return memberMissionRepository.findAllByMemberId(memberId);
    }

    private void deleteMemberMissionByTodayMission(Mission todayMission) {
        List<MemberMission> memberMissions = memberMissionRepository.findAllByMissionId(
            todayMission.getId());
        memberMissions.forEach(
            MemberMission::setEnd
        );
    }

    private void missionSetting(Mission todayMission, List<Mission> missionList) {
        Random random = new Random();
        int randomIndex = random.nextInt(missionList.size());
        Mission nextMission = missionList.get(randomIndex);
        nextMission.setActive(LocalDate.now());
        todayMission.setEnd();
    }

    private Mission findActiveMission() {
        return missionRepository.findByState(MissionState.ACTIVE)
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

    private List<Mission> findInActiveMission() {
        List<Mission> missions = missionRepository.findAllByState(MissionState.INACTIVE);
        if (missions.isEmpty()) {
            slackWebhookUtil.slackNotificationServerError(
                ResponseCode.NOT_FOUND_VALID_MISSION_FOR_SET_DAILY_MISSION);
            throw new CustomException(ResponseCode.NOT_FOUND_VALID_MISSION_FOR_SET_DAILY_MISSION);
        }
        return missions;
    }

}
