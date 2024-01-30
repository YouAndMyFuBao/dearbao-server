package com.fubao.dearbao.api.service.auth;

import com.fubao.dearbao.IntegrationTestSupport;
import com.fubao.dearbao.api.controller.auth.dto.response.KakaoLoginResponse;
import com.fubao.dearbao.api.service.auth.dto.KakaoInfoDto;
import com.fubao.dearbao.api.service.auth.dto.KakaoLoginServiceDto;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberGender;
import com.fubao.dearbao.domain.member.MemberRepository;
import com.fubao.dearbao.domain.member.MemberRole;
import com.fubao.dearbao.domain.member.MemberState;
import com.fubao.dearbao.domain.oauth.KakaoApiClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class AuthServiceTest extends IntegrationTestSupport {

    @Autowired
    private AuthService authService;
    @MockBean
    private KakaoApiClient kakaoApiClient;
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원가입을 진행한다. 유저가 존재하여 유저를 새로 생성하지 않고 jwt를 발급한다.")
    @Test
    void kakaoLogin() {
        //given
        String code = "code";
        String accessToken = "token";
        String providerId = "id";
        Member member = createMember("peter", providerId, MemberGender.MALE, MemberState.ACTIVE,
            MemberRole.MEMBER);
        memberRepository.save(member);
        KakaoLoginServiceDto kakaoLoginServiceDto = KakaoLoginServiceDto.of(code);
        given(kakaoApiClient.requestAccessToken(any(KakaoLoginServiceDto.class)))
            .willReturn(accessToken);
        given(kakaoApiClient.requestOAuthInfo(accessToken))
            .willReturn(new KakaoInfoDto(providerId));

        //when
        KakaoLoginResponse response = authService.kakaoLogin(kakaoLoginServiceDto);

        //then
        assertThat(response).isNotNull();

        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(1)
            .extracting("providerId")
            .contains(providerId);
    }


    @DisplayName("회원가입을 진행한다. 유저가 존재하지 않아 유저를 새로 생성하고 jwt를 발급한다.")
    @Test
    void kakaoLoginWithCreateUser() {
        //given
        String code = "code";
        String accessToken = "token";
        String providerId = "id";
        KakaoLoginServiceDto kakaoLoginServiceDto = KakaoLoginServiceDto.of(code);
        given(kakaoApiClient.requestAccessToken(any(KakaoLoginServiceDto.class)))
            .willReturn(accessToken);
        given(kakaoApiClient.requestOAuthInfo(accessToken))
            .willReturn(new KakaoInfoDto(providerId));

        //when
        KakaoLoginResponse response = authService.kakaoLogin(kakaoLoginServiceDto);

        //then
        assertThat(response).isNotNull();

        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(1)
            .extracting("providerId")
            .contains(providerId);
    }

    private Member createMember(String name, String providerId, MemberGender gender,
        MemberState state, MemberRole role) {
        return Member.builder()
            .name(name)
            .gender(gender)
            .state(state)
            .role(role)
            .providerId(providerId)
            .build();
    }
}