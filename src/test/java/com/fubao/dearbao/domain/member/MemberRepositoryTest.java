package com.fubao.dearbao.domain.member;

import com.fubao.dearbao.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


class MemberRepositoryTest extends IntegrationTestSupport {

    @DisplayName("소셜 로그인의 provider의 id를 가진 member를 조회한다.")
    @Test
    void findByProviderIdAndState() {
        //given
        String providerId = "123456789";
        Member member = createMember(providerId, "peter");
        memberRepository.save(member);

        //when
        Optional<Member> savedMember = memberRepository.findByProviderIdAndState(providerId,
            MemberState.ACTIVE);

        //then
        assertThat(savedMember).isNotEmpty()
            .get().extracting("providerId").isEqualTo(providerId);
    }

    @DisplayName("소셜 로그인 provider의 id를 가진 member를 조회할 때, 없는 경우 null을 반환한다.")
    @Test
    void findByProviderIdAndStateWhenMemberIsEmpty() {
        //given
        String providerId = "123456789";

        //when
        Optional<Member> savedMember = memberRepository.findByProviderIdAndState(
            providerId, MemberState.ACTIVE
        );

        //then
        assertThat(savedMember).isEmpty();

    }

    @DisplayName("id와 state를 가지고 유저를 조회한다.")
    @Test
    void findByIdAndState() {
        //given
        Member member = createMember("001", "peter");
        Member savedMember = memberRepository.save(member);

        Long id = savedMember.getId();
        MemberState state = savedMember.getState();

        //when
        Optional<Member> optionalMember = memberRepository.findByIdAndState(
            id, state);

        //then
        assertThat(optionalMember).isNotEmpty()
            .get().extracting("id", "state").contains(id, state);
    }

    @DisplayName("id와 state를 가지고 유저를 조회할 때 없는 경우 null을 반환한다.")
    @Test
    void findByIdAndStateWhenMemberIsEmpty() {
        //given
        Long id = 1L;
        MemberState state = MemberState.ACTIVE;

        //when
        Optional<Member> optionalMember = memberRepository.findByIdAndState(
            id, state);

        //then
        assertThat(optionalMember).isEmpty();
    }

    @DisplayName("닉네임이 존재하지 않으면 false를 반환한다.")
    @Test
    void existsByNameAndStateWithNotExistNickname() {
        //given
        String nickname = "nickname";
        MemberState state = MemberState.ACTIVE;
        //when
        boolean result = memberRepository.existsByNameAndState(nickname, state);
        //then
        assertThat(result).isFalse();
    }

    @DisplayName("닉네임이 존재하면 true를 반환한다.")
    @Test
    void existsByNameAndStateWithExistNickname() {
        //given
        String nickname = "nickname";
        MemberState state = MemberState.ACTIVE;
        Member member = createMember("001", nickname);
        memberRepository.save(member);

        //when
        boolean result = memberRepository.existsByNameAndState(nickname,state);

        //then
        assertThat(result).isTrue();
    }

    private Member createMember(String providerId, String nickname) {
        return Member.builder()
            .name(nickname)
            .providerId(providerId)
            .state(MemberState.ACTIVE)
            .role(MemberRole.ROLE_GUEST)
            .build();
    }
}