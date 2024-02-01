package com.fubao.dearbao.api.service.auth;

import com.fubao.dearbao.api.controller.auth.dto.response.KakaoLoginResponse;
import com.fubao.dearbao.api.service.auth.dto.InitMemberServiceDto;
import com.fubao.dearbao.api.service.auth.dto.KakaoInfoDto;
import com.fubao.dearbao.api.service.auth.dto.KakaoLoginServiceDto;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberRepository;
import com.fubao.dearbao.domain.member.MemberState;
import com.fubao.dearbao.domain.oauth.KakaoApiClient;
import com.fubao.dearbao.global.common.exception.CustomException;
import com.fubao.dearbao.global.common.exception.ResponseCode;
import com.fubao.dearbao.global.config.security.jwt.JwtTokenProvider;
import com.fubao.dearbao.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

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

    @Transactional
    @Override
    public void initMember(InitMemberServiceDto serviceDto) {
        Member member = findMemberById(serviceDto.getMemberId());
        if (memberRepository.existsByNameAndState(serviceDto.getNickName(), MemberState.ACTIVE)) {
            throw new CustomException(ResponseCode.EXIST_NICKNAME);
        }
        if (!member.isPossibleNickname(serviceDto.getNickName())) {
            throw new CustomException(ResponseCode.INVALID_NICKNAME);
        }
        member.initMember(serviceDto.getNickName(), serviceDto.getTitle());
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
