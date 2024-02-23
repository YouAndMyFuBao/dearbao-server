package com.fubao.dearbao.api.service.mission;

import com.fubao.dearbao.IntegrationTestSupport;
import com.fubao.dearbao.api.controller.mission.dto.response.DailyMessageResponse;
import com.fubao.dearbao.api.controller.mission.dto.response.DailyMissionResponse;
import com.fubao.dearbao.api.controller.mission.dto.response.GetMyMissionResponse;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberGender;
import com.fubao.dearbao.domain.member.MemberRole;
import com.fubao.dearbao.domain.member.MemberState;
import com.fubao.dearbao.domain.mission.entity.MemberMission;
import com.fubao.dearbao.domain.mission.entity.MemberMissionState;
import com.fubao.dearbao.domain.mission.entity.Mission;
import com.fubao.dearbao.domain.mission.entity.MissionState;
import com.fubao.dearbao.global.common.exception.CustomException;
import com.fubao.dearbao.global.common.exception.ResponseCode;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MissionServiceTest extends IntegrationTestSupport {

    @DisplayName("시간이 09:00:00 ~ 20:59:59 일때 아직 미션을 수행하지 않았다.")
    @Test
    void dailyMissionInTimeWithoutSuccess() {
        //given
        LocalDate todayDate = LocalDate.of(2023, 11, 11);
        LocalTime todayTime = LocalTime.of(9, 0, 0);
        LocalDateTime today = LocalDateTime.of(todayDate, todayTime);
        Member member = memberRepository.save(createMember("peter", MemberGender.MALE));
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
        Member member = memberRepository.save(createMember("peter", MemberGender.MALE));
        Mission mission = missionRepository.save(createMission(todayDate, MissionState.ACTIVE));
        memberMissionRepository.save(createMemberMission("content",member, mission));

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
        Member member = memberRepository.save(createMember("peter", MemberGender.MALE));
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
        Member member = memberRepository.save(createMember("peter", MemberGender.MALE));
        Mission mission = missionRepository.save(createMission(todayDate, MissionState.ACTIVE));
        memberMissionRepository.save(createMemberMission("content",member, mission));

        //when
        DailyMessageResponse response = (DailyMessageResponse) missionService.dailyMission(
            member.getId(), today);

        //then
        assertThat(response)
            .extracting("isMissionSuccess", "isMessageOpenTime", "nickname", "message")
            .contains(true, true, member.getName(), mission.getAnswer());
    }

    @DisplayName("데일리 미션을 세팅한다.")
    @Test
    void setDailyMission() {
        //given
        Mission nowMission = createMission(LocalDate.of(2023, 11, 11), MissionState.ACTIVE);
        Mission nextMission = createMission(LocalDate.of(2023, 11, 11), MissionState.INACTIVE);
        Member member = memberRepository.save(createMember("peter", MemberGender.MALE));
        List<Mission> missions = missionRepository.saveAll(List.of(nowMission, nextMission));
        memberMissionRepository.save(createMemberMission("content",member, missions.get(0)));

        //when
        missionService.setDailyMission();

        //then
        List<Mission> savedMissions = missionRepository.findAll();
        List<MemberMission> memberMissions = memberMissionRepository.findAll();
        assertThat(memberMissions).hasSize(1)
            .extracting("state").contains(MemberMissionState.END);
        assertThat(savedMissions).hasSize(2)
            .extracting("id", "state")
            .containsExactlyInAnyOrder(
                tuple(missions.get(0).getId(), MissionState.END),
                tuple(missions.get(1).getId(), MissionState.ACTIVE)
            );
    }

    @DisplayName("데일리 미션을 세팅할때 남은 미션이 없을 경우 예외가 발생하고 슬랙 메세지를 보낸다.")
    @Test
    void setDailyMissionWithoutValidMission() {
        //given
        Mission nowMission = createMission(LocalDate.of(2023, 11, 11), MissionState.END);
        Mission nextMission = createMission(LocalDate.of(2023, 11, 11), MissionState.ACTIVE);
        Member member = memberRepository.save(createMember( "peter", MemberGender.MALE));
        List<Mission> missions = missionRepository.saveAll(List.of(nowMission, nextMission));
        memberMissionRepository.save(createMemberMission("content",member, missions.get(0)));

        //when then
        assertThatThrownBy(() -> missionService.setDailyMission())
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.NOT_FOUND_VALID_MISSION_FOR_SET_DAILY_MISSION);
        verify(slackWebhookUtil).slackNotificationServerError(
            ResponseCode.NOT_FOUND_VALID_MISSION_FOR_SET_DAILY_MISSION);
    }

    @DisplayName("")
    @Test
    void getMyMission() {
        //given
        LocalDate date = LocalDate.of(2023, 11, 11);
        LocalDate date2 = LocalDate.of(2023, 12, 12);
        Member member = memberRepository.save(createMember("동석", MemberGender.MALE));
        Member member2 = memberRepository.save(createMember("동석2", MemberGender.MALE));
        Mission mission = missionRepository.save(createMission(date, MissionState.ACTIVE));
        Mission mission2 = missionRepository.save(createMission(date2, MissionState.ACTIVE));
        List<MemberMission> savedMemberMissions = memberMissionRepository.saveAll(List.of(
            createMemberMission("내용1",member, mission),
            createMemberMission("내용2",member, mission2))
        );
        memberMissionRepository.save(createMemberMission("내용3",member2,mission));

        //when
        List<GetMyMissionResponse> responses = missionService.getMyMission(member.getId());

        //then
        assertThat(responses).hasSize(2)
            .extracting("date", "content")
            .contains(
                tuple("11:11","내용1"),
                tuple("12:12","내용2")
            );
    }

    private MemberMission createMemberMission(String content, Member member, Mission mission) {
        return MemberMission.builder()
            .content(content)
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

    private Member createMember(String nickname, MemberGender title) {
        return Member.builder()
            .name(nickname)
            .gender(title)
            .state(MemberState.ACTIVE)
            .role(MemberRole.ROLE_MEMBER)
            .build();
    }
}