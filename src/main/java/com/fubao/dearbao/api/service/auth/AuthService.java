package com.fubao.dearbao.api.service.auth;

import com.fubao.dearbao.api.controller.auth.dto.response.KakaoLoginResponse;
import com.fubao.dearbao.api.service.auth.dto.InitMemberServiceDto;
import com.fubao.dearbao.api.service.auth.dto.KakaoLoginServiceDto;

public interface AuthService {

    KakaoLoginResponse kakaoLogin(KakaoLoginServiceDto kakaoLoginServiceDto);

    void initMember(InitMemberServiceDto serviceDto);
}
