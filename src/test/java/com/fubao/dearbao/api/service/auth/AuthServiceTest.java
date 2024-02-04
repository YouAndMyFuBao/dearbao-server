package com.fubao.dearbao.api.service.auth;

import com.fubao.dearbao.IntegrationTestSupport;
import com.fubao.dearbao.api.controller.auth.dto.response.KakaoLoginResponse;
import com.fubao.dearbao.api.controller.auth.dto.response.TokenRegenerateResponse;
import com.fubao.dearbao.api.service.auth.dto.InitMemberServiceDto;
import com.fubao.dearbao.api.service.auth.dto.KakaoInfoDto;
import com.fubao.dearbao.api.service.auth.dto.KakaoLoginServiceDto;
import com.fubao.dearbao.api.service.auth.dto.RegenerateTokenServiceDto;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberGender;
import com.fubao.dearbao.domain.member.MemberRepository;
import com.fubao.dearbao.domain.member.MemberRole;
import com.fubao.dearbao.domain.member.MemberState;
import com.fubao.dearbao.domain.oauth.KakaoApiClient;
import com.fubao.dearbao.global.common.exception.CustomException;
import com.fubao.dearbao.global.common.exception.ResponseCode;
import com.fubao.dearbao.global.common.vo.AuthToken;
import com.fubao.dearbao.global.config.security.jwt.JwtTokenProvider;
import com.fubao.dearbao.global.util.RedisUtil;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.nio.charset.MalformedInputException;
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
    @MockBean
    private RedisUtil redisUtil;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("회원가입을 진행한다. 유저가 존재하여 유저를 새로 생성하지 않고 jwt를 발급한다.")
    @Test
    void kakaoLogin() {
        //given
        String code = "code";
        String accessToken = "token";
        String providerId = "id";
        Member member = createMember(providerId);
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

    @DisplayName("멤버 정보를 입력한다.")
    @Test
    void initMember() {
        //given
        Member member = createMember("001");
        Member savedMember = memberRepository.save(member);
        Long memberId = savedMember.getId();
        String nickname = "peter";
        MemberGender title = MemberGender.FEMALE;
        InitMemberServiceDto initMemberServiceDto = InitMemberServiceDto.of(memberId, nickname,
            title);

        //when
        authService.initMember(initMemberServiceDto);

        //then
        List<Member> initMember = memberRepository.findAll();
        assertThat(initMember).hasSize(1)
            .extracting("name", "gender", "role")
            .contains(
                tuple(nickname, title, MemberRole.ROLE_MEMBER)
            );
    }

    @DisplayName("멤버 정보를 입력할때 중복된 닉네임이면 예외가 발생한다.")
    @Test
    void initMemberWhenExistNickname() {
        //given
        Member member = createMember("001");
        Member savedMember = memberRepository.save(member);

        Long memberId = savedMember.getId();
        String nickname = "peter";
        MemberGender title = MemberGender.FEMALE;
        InitMemberServiceDto initMemberServiceDto = InitMemberServiceDto.of(memberId, nickname,
            title);

        memberRepository.save(createMemberWithInit("001", nickname, title));

        //when then
        assertThatThrownBy(() -> authService.initMember(initMemberServiceDto))
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.EXIST_NICKNAME);
    }

    @DisplayName("멤버 정보를 입력할때 닉네임의 길이가 2미만일 경우 예외가 발생한다.")
    @Test
    void initMemberWithNicknameLessThan2() {
        //given
        Member member = createMember("001");
        Member savedMember = memberRepository.save(member);
        Long memberId = savedMember.getId();
        String nickname = "1";
        MemberGender title = MemberGender.FEMALE;
        InitMemberServiceDto initMemberServiceDto = InitMemberServiceDto.of(memberId, nickname,
            title);

        //when then
        assertThatThrownBy(() -> authService.initMember(initMemberServiceDto))
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.INVALID_NICKNAME);
    }

    @DisplayName("멤버 정보를 입력할때 닉네임의 길이가 8초과일 경우 예외가 발생한다.")
    @Test
    void initMemberWithNicknameMoreThan8() {
        //given
        Member member = createMember("001");
        Member savedMember = memberRepository.save(member);
        Long memberId = savedMember.getId();
        String nickname = "123456789";
        MemberGender title = MemberGender.FEMALE;
        InitMemberServiceDto initMemberServiceDto = InitMemberServiceDto.of(memberId, nickname,
            title);

        //when then
        assertThatThrownBy(() -> authService.initMember(initMemberServiceDto))
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.INVALID_NICKNAME);
    }

    @DisplayName("token을 재생성한다.")
    @Test
    void tokenRegenerate() {
        //given
        AuthToken authToken = createToken(1L);
        RegenerateTokenServiceDto dto = new RegenerateTokenServiceDto(
            authToken.getAccessToken(), authToken.getRefreshToken());
        given(redisUtil.hasKey(dto.getRefreshToken()))
            .willReturn(true);
        //when
       TokenRegenerateResponse response = authService.tokenRegenerate(dto);
        //then
        assertThat(response).isNotNull();
    }
    @DisplayName("token을 재생성할때 잘못된 refresh token일 경우 예외가 발생한다.")
    @Test
    void tokenRegenerateWithInvalidRefreshToken() {
        //given
        AuthToken authToken = createToken(1L);
        RegenerateTokenServiceDto dto = new RegenerateTokenServiceDto(
            authToken.getAccessToken(), "123");
        given(redisUtil.hasKey(dto.getRefreshToken()))
            .willReturn(true);
        //when
        assertThatThrownBy(()->authService.tokenRegenerate(dto))
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.INVALID_TOKEN);
    }
    @DisplayName("token을 재생성할때 cache에 refresh token이 없다면 예외가 발생한다.")
    @Test
    void tokenRegenerateWithoutRefreshTokenNotInCache() {
        //given
        AuthToken authToken = createToken(1L);
        RegenerateTokenServiceDto dto = new RegenerateTokenServiceDto(
            authToken.getAccessToken(), "123");
        given(redisUtil.hasKey(dto.getRefreshToken()))
            .willReturn(false);
        //when
        assertThatThrownBy(()->authService.tokenRegenerate(dto))
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.INVALID_TOKEN);
    }

    private Member createMember(String providerId) {
        return Member.builder()
            .state(MemberState.ACTIVE)
            .role(MemberRole.ROLE_GUEST)
            .providerId(providerId)
            .build();
    }

    private Member createMemberWithInit(String providerId, String nickname, MemberGender title) {
        return Member.builder()
            .name(nickname)
            .gender(title)
            .state(MemberState.ACTIVE)
            .role(MemberRole.ROLE_MEMBER)
            .providerId(providerId)
            .build();
    }
    private AuthToken createToken(long id){
        return jwtTokenProvider.createToken(String.valueOf(id));
    }
}