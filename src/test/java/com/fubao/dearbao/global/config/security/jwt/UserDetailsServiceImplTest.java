package com.fubao.dearbao.global.config.security.jwt;

import com.fubao.dearbao.IntegrationTestSupport;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberRepository;
import com.fubao.dearbao.domain.member.MemberRole;
import com.fubao.dearbao.domain.member.MemberState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
class UserDetailsServiceImplTest extends IntegrationTestSupport {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("유저 조회를 조회하고 userDetail을 반환한다.")
    @Test
    void loadUserByUsernameExistUser() {
        //given
        Member member = createMember("001");
        memberRepository.save(member);
        //when
        UserDetails userDetails = userDetailsService.loadUserByUsername(
            String.valueOf(member.getId()));
        //then

        assertThat(userDetails)
            .extracting("username", "authorities")
            .contains(String.valueOf(member.getId()),
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().getName())));
    }

    @DisplayName("유저를 조회하지만 존재하지 않으면 null을 반환한다.")
    @Test
    void loadUserByUsernameNotExistUser() {
        //given
        Long memberId = 1L;
        //when
        Exception e = assertThrows(UsernameNotFoundException.class,()->
            userDetailsService.loadUserByUsername(String.valueOf(memberId)));
        //then
        assertThat(e.getMessage()).isEqualTo(String.valueOf(memberId));
    }

    private Member createMember(String providerId) {
        return Member.builder()
            .providerId(providerId)
            .state(MemberState.ACTIVE)
            .role(MemberRole.ROLE_GUEST)
            .build();
    }
}