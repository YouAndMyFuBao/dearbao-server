package com.fubao.dearbao.domain.mission;

import com.fubao.dearbao.IntegrationTestSupport;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberGender;
import com.fubao.dearbao.domain.member.MemberRole;
import com.fubao.dearbao.domain.member.MemberState;
import com.fubao.dearbao.domain.mission.entity.MemberMission;
import com.fubao.dearbao.domain.mission.entity.MemberMissionState;
import com.fubao.dearbao.domain.mission.entity.Mission;
import com.fubao.dearbao.domain.mission.entity.MissionState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MemberMissionRepositoryTest extends IntegrationTestSupport {

    @DisplayName("멤버 Id와 State를 기준으로 멤버미션을 찾는다.")
    @Test
    void findByMemberIdAndState() {
        //given
        LocalDate date = LocalDate.of(2023, 11, 11);
        Member member = memberRepository.save(createMember("1", "동석", MemberGender.MALE));
        Mission mission = missionRepository.save(createMission(date, MissionState.ACTIVE));
        memberMissionRepository.save(createMemberMission(member, mission));
        //when
        Optional<MemberMission> memberMissionOptional = memberMissionRepository.findByMemberIdAndState(
            member.getId(), MemberMissionState.ACTIVE);
        //then
        assertThat(memberMissionOptional).isNotEmpty();
        assertThat(memberMissionOptional.get().getMember().getId()).isEqualTo(member.getId());
        assertThat(memberMissionOptional.get().getMission().getId()).isEqualTo(mission.getId());
        assertThat(memberMissionOptional.get().getState()).isEqualTo(MemberMissionState.ACTIVE);
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
}