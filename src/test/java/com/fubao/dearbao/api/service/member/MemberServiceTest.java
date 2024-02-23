package com.fubao.dearbao.api.service.member;

import com.fubao.dearbao.IntegrationTestSupport;
import com.fubao.dearbao.api.controller.member.dto.response.GetMemberNicknameResponse;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberGender;
import com.fubao.dearbao.domain.member.MemberRole;
import com.fubao.dearbao.domain.member.MemberState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest extends IntegrationTestSupport {

    @DisplayName("멤버의 닉네임을 조회한다.")
    @Test
    void getMemberNickname() {
        //given
        Member member = memberRepository.save(createMember( "peter", MemberGender.MALE));

        //when
        GetMemberNicknameResponse response = memberService.getMemberNickname(member.getId());

        //then
        assertThat(response)
            .extracting("nickname").isEqualTo(member.getName());
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