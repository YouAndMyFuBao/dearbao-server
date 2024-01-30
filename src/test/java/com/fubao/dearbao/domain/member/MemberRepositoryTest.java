package com.fubao.dearbao.domain.member;

import com.fubao.dearbao.IntegrationTestSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


class MemberRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("소셜 로그인의 provider의 id를 가진 member를 조회한다.")
    @Test
    void findByProviderIdAndState() {
        //given
        String providerId = "123456789";
        Member member = createMember(providerId);
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
        Optional<Member> savedMember = memberRepository.findByProviderIdAndState(providerId,
            MemberState.ACTIVE);

        //then
        assertThat(savedMember).isEmpty();

    }

    private Member createMember(String providerId) {
        return Member.builder()
            .providerId(providerId)
            .state(MemberState.ACTIVE)
            .role(MemberRole.GUEST)
            .build();
    }
}