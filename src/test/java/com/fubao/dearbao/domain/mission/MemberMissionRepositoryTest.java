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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class MemberMissionRepositoryTest extends IntegrationTestSupport {

    @DisplayName("멤버 Id와 State를 기준으로 멤버미션을 찾는다.")
    @Test
    void findByMemberIdAndState() {
        //given
        LocalDate date = LocalDate.of(2023, 11, 11);
        Member member = memberRepository.save(createMember("동석", MemberGender.MALE));
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

    @DisplayName("특정 미션 아이디를 가진 멤버미션을 모두 조회한다.")
    @Test
    void findAllByMissionId() {
        //given
        LocalDate date = LocalDate.of(2023, 11, 11);
        Member member1 = createMember("동석", MemberGender.MALE);
        Member member2 = createMember("동석2", MemberGender.MALE);
        Mission mission = missionRepository.save(createMission(date, MissionState.ACTIVE));
        List<Member> members = memberRepository.saveAll(List.of(member1, member2));
        List<MemberMission> memberMissions = memberMissionRepository.saveAll(List.of(
            createMemberMission(members.get(0), mission),
            createMemberMission(members.get(1), mission))
        );
        //when
        List<MemberMission> savedMemberMissions = memberMissionRepository.findAllByMissionId(
            mission.getId());
        //then
        assertThat(savedMemberMissions).hasSize(2)
            .extracting("id")
            .contains(memberMissions.get(0).getId(), memberMissions.get(1).getId());
    }

    @DisplayName("멤버 id로 멤버가 수행한 미션 내용을 찾는다.")
    @Test
    void findAllByMemberId() {
        //given
        LocalDate date = LocalDate.of(2023, 11, 11);
        Member member = memberRepository.save(createMember("동석", MemberGender.MALE));
        Member member2 = memberRepository.save(createMember("동석2", MemberGender.MALE));
        Mission mission = missionRepository.save(createMission(date, MissionState.ACTIVE));
        Mission mission2 = missionRepository.save(createMission(date, MissionState.ACTIVE));
        List<MemberMission> savedMemberMissions = memberMissionRepository.saveAll(List.of(
            createMemberMission(member, mission),
            createMemberMission(member, mission2))
        );
        memberMissionRepository.save(createMemberMission(member2, mission));

        //when
        List<MemberMission> memberMissions = memberMissionRepository.findAllByMemberId(
            member.getId());

        //then
        assertThat(memberMissions).hasSize(2)
            .extracting("id")
            .contains(
                savedMemberMissions.get(0).getId(), savedMemberMissions.get(1).getId()
            );
    }

    private Member createMember(String nickname, MemberGender title) {
        return Member.builder()
            .name(nickname)
            .gender(title)
            .state(MemberState.ACTIVE)
            .role(MemberRole.ROLE_MEMBER)
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