package com.fubao.dearbao.domain.member;

import com.fubao.dearbao.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class MemberTest extends IntegrationTestSupport {

    @DisplayName("멤버 생성시 멤버 권한은 Guest이다.")
    @Test
    void memberRoleWhenCreate() {
        //given
        String providerId = "123456789";
        //when
        Member member = Member.create(providerId);
        //then
        assertThat(member.getRole()).isEqualByComparingTo(MemberRole.GUEST);
    }

    @DisplayName("멤버 생성시 멤버 상태는 ACTIVE이다.")
    @Test
    void memberStateWhenCreate() {
        //given
        String providerId = "123456789";
        //when
        Member member = Member.create(providerId);
        //then
        assertThat(member.getState()).isEqualByComparingTo(MemberState.ACTIVE);
    }
}