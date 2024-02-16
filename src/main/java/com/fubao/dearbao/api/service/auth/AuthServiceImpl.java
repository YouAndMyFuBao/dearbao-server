package com.fubao.dearbao.api.service.auth;

import com.fubao.dearbao.api.controller.auth.dto.response.KakaoLoginResponse;
import com.fubao.dearbao.api.controller.auth.dto.response.TokenRegenerateResponse;
import com.fubao.dearbao.api.service.auth.dto.InitMemberServiceDto;
import com.fubao.dearbao.api.service.auth.dto.KakaoInfoDto;
import com.fubao.dearbao.api.service.auth.dto.KakaoLoginServiceDto;
import com.fubao.dearbao.api.service.auth.dto.LogoutServiceDto;
import com.fubao.dearbao.api.service.auth.dto.RegenerateTokenServiceDto;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberRepository;
import com.fubao.dearbao.domain.member.MemberState;
import com.fubao.dearbao.domain.oauth.KakaoApiClient;
import com.fubao.dearbao.global.common.exception.CustomException;
import com.fubao.dearbao.global.common.exception.ResponseCode;
import com.fubao.dearbao.global.common.vo.AuthToken;
import com.fubao.dearbao.global.config.security.jwt.JwtTokenProvider;
import com.fubao.dearbao.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final KakaoApiClient kakaoApiClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @Transactional
    @Override
    public KakaoLoginResponse kakaoLogin(KakaoLoginServiceDto dto) {
        String code = kakaoApiClient.requestAccessToken(dto);
        KakaoInfoDto kakaoInfoDto = kakaoApiClient.requestOAuthInfo(code);
        Member member = findOrCreateMember(kakaoInfoDto);
        AuthToken authToken = jwtTokenProvider.createToken(member.getId().toString());
        return authToken.toKakaoLoginResponse(member.isInit());
    }

    @Transactional
    @Override
    public void initMember(InitMemberServiceDto dto) {
        Member member = findMemberById(dto.getMemberId());
        if (memberRepository.existsByNameAndState(dto.getNickName(), MemberState.ACTIVE)) {
            throw new CustomException(ResponseCode.EXIST_NICKNAME);
        }
        if (!member.isPossibleNickname(dto.getNickName())) {
            throw new CustomException(ResponseCode.INVALID_NICKNAME);
        }
        member.initMember(dto.getNickName(), dto.getTitle());
    }

    @Override
    public TokenRegenerateResponse tokenRegenerate(RegenerateTokenServiceDto dto) {
        if (!jwtTokenProvider.validateToken(dto.getRefreshToken(), null)) {
            throw new CustomException(ResponseCode.INVALID_TOKEN);
        }
        if (!redisUtil.hasKey(dto.getRefreshToken())) {
            throw new CustomException(ResponseCode.INVALID_TOKEN);
        }
        redisUtil.deleteData(dto.getRefreshToken());
        String userName = jwtTokenProvider.getUsernameFromRefreshToken(
            dto.getRefreshToken());
        AuthToken authToken = jwtTokenProvider.createToken(userName);
        return TokenRegenerateResponse.of(authToken);
    }

    @Override
    public void logout(LogoutServiceDto dto) {
        redisUtil.deleteData(dto.getRefreshToken());
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findByIdAndState(memberId, MemberState.ACTIVE)
            .orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_MEMBER)
            );
    }

    private Member findOrCreateMember(KakaoInfoDto kakaoInfoDto) {
        return memberRepository.findByProviderIdAndState(kakaoInfoDto.getId(), MemberState.ACTIVE)
            .orElseGet(() -> signUp(kakaoInfoDto));
    }

    private Member signUp(KakaoInfoDto kakaoInfoDto) {
        return memberRepository.save(Member.create(kakaoInfoDto.getId()));
    }
}
