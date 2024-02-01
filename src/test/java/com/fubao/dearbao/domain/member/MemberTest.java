package com.fubao.dearbao.domain.member;

import com.fubao.dearbao.IntegrationTestSupport;
import com.fubao.dearbao.global.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class MemberTest extends IntegrationTestSupport {

    @DisplayName("멤버 생성시 멤버 권한은 Guest이다.")
    @Test
    void memberRoleWhenCreate() {
        //given
        String providerId = "123456789";
        //when
        Member member = Member.create(providerId);
        //then
        assertThat(member.getRole()).isEqualByComparingTo(MemberRole.ROLE_GUEST);
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

    @DisplayName("멤버 init 시 권한이 ROLE_MEMBER로 변경한다.")
    @Test
    void initMember() {
        //given
        String nickname = "peter";
        MemberGender gender = MemberGender.MALE;
        Member member = createMember("001");
        //when
        member.initMember(nickname, gender);
        //then
        assertThat(member)
            .extracting("name", "gender", "role").
            contains(nickname, gender, MemberRole.ROLE_MEMBER);
    }

    @DisplayName("닉네임의 길이가 2보다 작을 경우 예외가 발생한다.")
    @Test
    void isPossibleNicknameSizeLessThan2() {
        //given
        Member member = createMember("001");
        String nickname = "1";
        //when
        boolean result = member.isPossibleNickname(nickname);
        //then
        assertThat(result).isFalse();
    }

    @DisplayName("닉네임의 길이가 8보다 클 경우 예외가 발생한다.")
    @Test
    void isPossibleNicknameSizeMoreThan8() {
        //given
        Member member = createMember("001");
        String nickname = "123456789";
        //when
        boolean result = member.isPossibleNickname(nickname);
        //then
        assertThat(result).isFalse();
    }

    private Member createMember(String providerId) {
        return Member.builder()
            .providerId(providerId)
            .state(MemberState.ACTIVE)
            .role(MemberRole.ROLE_GUEST)
            .build();
    }
}