package com.fubao.dearbao.api.service.member;

import com.fubao.dearbao.IntegrationTestSupport;
import com.fubao.dearbao.api.controller.member.dto.response.GetMemberNicknameResponse;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberGender;
import com.fubao.dearbao.domain.member.MemberRole;
import com.fubao.dearbao.domain.member.MemberState;
import com.fubao.dearbao.global.common.exception.CustomException;
import com.fubao.dearbao.global.common.exception.ResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest extends IntegrationTestSupport {

    @DisplayName("멤버의 닉네임을 조회한다.")
    @Test
    void getMemberNickname() {
        //given
        Member member = memberRepository.save(createMember("peter", MemberGender.MALE));

        //when
        GetMemberNicknameResponse response = memberService.getMemberNickname(member.getId());

        //then
        assertThat(response)
            .extracting("nickname").isEqualTo(member.getName());
    }

    @DisplayName("멤버의 닉네임을 조회할때 멤버가 없는 경우 예외가 발생한다.")
    @Test
    void getMemberNicknameWithNotFoundMember() {
        //given
        Long memberId = 1L;
        //when then
        assertThatThrownBy(() -> memberService.getMemberNickname(memberId))
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.NOT_FOUND_MEMBER);
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