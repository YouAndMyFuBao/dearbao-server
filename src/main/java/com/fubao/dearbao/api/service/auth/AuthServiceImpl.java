package com.fubao.dearbao.api.service.auth;

import com.fubao.dearbao.api.controller.auth.dto.response.KakaoLoginResponse;
import com.fubao.dearbao.api.service.auth.dto.KakaoInfoDto;
import com.fubao.dearbao.api.service.auth.dto.KakaoLoginServiceDto;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberRepository;
import com.fubao.dearbao.domain.member.MemberState;
import com.fubao.dearbao.domain.oauth.KakaoApiClient;
import com.fubao.dearbao.global.config.security.jwt.JwtTokenProvider;
import com.fubao.dearbao.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final KakaoApiClient kakaoApiClient;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public KakaoLoginResponse kakaoLogin(KakaoLoginServiceDto kakaoLoginServiceDto) {
        String code = kakaoApiClient.requestAccessToken(kakaoLoginServiceDto);
        KakaoInfoDto kakaoInfoDto = kakaoApiClient.requestOAuthInfo(code);
        Member member = findOrCreateMember(kakaoInfoDto);
        return jwtTokenProvider.createToken(member.getId().toString()).toKakaoLoginResponse();
    }

    private Member findOrCreateMember(KakaoInfoDto kakaoInfoDto) {
        return memberRepository.findByProviderIdAndState(kakaoInfoDto.getId(), MemberState.ACTIVE)
            .orElseGet(() -> signUp(kakaoInfoDto));
    }

    private Member signUp(KakaoInfoDto kakaoInfoDto) {
        return memberRepository.save(Member.create(kakaoInfoDto.getId()));
    }
}
