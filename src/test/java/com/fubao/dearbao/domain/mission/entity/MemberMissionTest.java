package com.fubao.dearbao.domain.mission.entity;

import com.fubao.dearbao.IntegrationTestSupport;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberGender;
import com.fubao.dearbao.domain.member.MemberRole;
import com.fubao.dearbao.domain.member.MemberState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

class MemberMissionTest extends IntegrationTestSupport {

    @DisplayName("멤버 비션을 종료상태로 변경한다.")
    @Test
    void setEnd() {
        //given
        LocalDate todayDate = LocalDate.of(2023, 11, 11);
        Member member = memberRepository.save(createMember("peter", MemberGender.MALE));
        Mission mission = missionRepository.save(createMission(todayDate, MissionState.ACTIVE));
        MemberMission memberMission = memberMissionRepository.save(
            createMemberMission(member, mission));
        //when
        memberMission.setEnd();
        //then
        assertThat(memberMission.getState()).isEqualTo(MemberMissionState.END);
    }

    @DisplayName("멤버의 상태를 비활성으로 변경한다.")
    @Test
    void deactivate() {
        //given
        LocalDate todayDate = LocalDate.of(2023, 11, 11);
        Member member = memberRepository.save(createMember("peter", MemberGender.MALE));
        Mission mission = missionRepository.save(createMission(todayDate, MissionState.ACTIVE));
        memberMissionRepository.save(
            createMemberMission(member, mission));

        //when
        memberService.deactivate(member.getId());

        //then
        Optional<Member> savedMember = memberRepository.findById(member.getId());
        assertThat(savedMember).isNotEmpty()
            .get()
            .extracting("state")
            .isEqualTo(MemberState.INACTIVE);
        List<MemberMission> memberMissions = memberMissionRepository.findAll();

        assertThat(memberMissions).hasSize(1)
            .extracting("state")
            .contains(
                MemberMissionState.INACTIVE
            );
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

    private Member createMember(String nickname, MemberGender memberGender) {
        return Member.builder()
            .name(nickname)
            .gender(memberGender)
            .state(MemberState.ACTIVE)
            .role(MemberRole.ROLE_MEMBER)
            .build();
    }
}