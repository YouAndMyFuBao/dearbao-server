package com.fubao.dearbao.api.service.mission;

import com.fubao.dearbao.IntegrationTestSupport;
import com.fubao.dearbao.api.controller.mission.dto.response.DailyMessageResponse;
import com.fubao.dearbao.api.controller.mission.dto.response.DailyMissionBaseResponse;
import com.fubao.dearbao.api.controller.mission.dto.response.DailyMissionResponse;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberGender;
import com.fubao.dearbao.domain.member.MemberRepository;
import com.fubao.dearbao.domain.member.MemberRole;
import com.fubao.dearbao.domain.member.MemberState;
import com.fubao.dearbao.domain.mission.MemberMissionRepository;
import com.fubao.dearbao.domain.mission.MissionRepository;
import com.fubao.dearbao.domain.mission.entity.MemberMission;
import com.fubao.dearbao.domain.mission.entity.MemberMissionState;
import com.fubao.dearbao.domain.mission.entity.Mission;
import com.fubao.dearbao.domain.mission.entity.MissionState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

class MissionServiceTest extends IntegrationTestSupport {

    @DisplayName("시간이 09:00:00 ~ 20:59:59 일때 아직 미션을 수행하지 않았다.")
    @Test
    void dailyMissionInTimeWithoutSuccess() {
        //given
        LocalDate todayDate = LocalDate.of(2023, 11, 11);
        LocalTime todayTime = LocalTime.of(9, 0, 0);
        LocalDateTime today = LocalDateTime.of(todayDate, todayTime);
        Member member = memberRepository.save(createMember("001", "peter", MemberGender.MALE));
        missionRepository.save(createMission(todayDate, MissionState.ACTIVE));
        //when
        DailyMissionResponse response = (DailyMissionResponse) missionService.dailyMission(
            member.getId(), today);
        //then
        assertThat(response)
            .extracting("isMissionSuccess", "isMessageOpenTime", "nickname", "remainingTime")
            .contains(false, false, member.getName(), "21:00:00");
    }

    @DisplayName("시간이 09:00:00 ~ 20:59:59 일때 미션을 수행했다.")
    @Test
    void dailyMissionInTimeWithSuccess() {
        //given
        LocalDate todayDate = LocalDate.of(2023, 11, 11);
        LocalTime todayTime = LocalTime.of(9, 0, 0);
        LocalDateTime today = LocalDateTime.of(todayDate, todayTime);
        Member member = memberRepository.save(createMember("001", "peter", MemberGender.MALE));
        Mission mission = missionRepository.save(createMission(todayDate, MissionState.ACTIVE));
        memberMissionRepository.save(createMemberMission(member, mission));
        //when
        DailyMissionResponse response = (DailyMissionResponse) missionService.dailyMission(
            member.getId(), today);
        //then
        assertThat(response)
            .extracting("isMissionSuccess", "isMessageOpenTime", "nickname", "remainingTime")
            .contains(true, false, member.getName(), "21:00:00");
    }

    @DisplayName("시간이 21:00:00 ~ 08:59:59 일때 미션을 수행하지 못했다.")
    @Test
    void dailyMissionOutTimeWithoutSuccess() {
        //given
        LocalDate todayDate = LocalDate.of(2023, 11, 11);
        LocalTime todayTime = LocalTime.of(21, 0, 0);
        LocalDateTime today = LocalDateTime.of(todayDate, todayTime);
        Member member = memberRepository.save(createMember("001", "peter", MemberGender.MALE));
        Mission mission = missionRepository.save(createMission(todayDate, MissionState.ACTIVE));
        //when
        DailyMessageResponse response = (DailyMessageResponse) missionService.dailyMission(
            member.getId(), today);
        //then
        assertThat(response)
            .extracting("isMissionSuccess", "isMessageOpenTime", "nickname", "message")
            .contains(false, true, member.getName(), "내일 편지 보내줘");
    }

    @DisplayName("시간이 21:00:00 ~ 08:59:59 일때 미션을 수행했다.")
    @Test
    void dailyMissionOutTimeWithSuccess() {
        //given
        LocalDate todayDate = LocalDate.of(2023, 11, 11);
        LocalTime todayTime = LocalTime.of(21, 0, 0);
        LocalDateTime today = LocalDateTime.of(todayDate, todayTime);
        Member member = memberRepository.save(createMember("001", "peter", MemberGender.MALE));
        Mission mission = missionRepository.save(createMission(todayDate, MissionState.ACTIVE));
        memberMissionRepository.save(createMemberMission(member, mission));
        //when
        DailyMessageResponse response = (DailyMessageResponse) missionService.dailyMission(
            member.getId(), today);
        //then
        assertThat(response)
            .extracting("isMissionSuccess", "isMessageOpenTime", "nickname", "message")
            .contains(true, true, member.getName(), mission.getAnswer());
    }

    private MemberMission createMemberMission(Member member, Mission mission) {
        return MemberMission.builder()
            .content("content")
            .member(member)
            .mission(mission)
            .state(MemberMissionState.ACTIVE)
            .build();
    }

    private Mission createMission(LocalDate localDate, MissionState missionState) {
        return Mission.builder()
            .answer("답변")
            .constructor("이름")
            .content("내용")
            .openAt(localDate)
            .state(missionState).build();
    }

    private Member createMember(String providerId, String nickname, MemberGender title) {
        return Member.builder()
            .name(nickname)
            .gender(title)
            .state(MemberState.ACTIVE)
            .role(MemberRole.ROLE_MEMBER)
            .providerId(providerId)
            .build();
    }
}