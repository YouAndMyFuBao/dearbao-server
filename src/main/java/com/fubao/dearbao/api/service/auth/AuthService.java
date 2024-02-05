package com.fubao.dearbao.api.service.auth;

import com.fubao.dearbao.api.controller.auth.dto.reqeust.TokenRegenerateRequest;
import com.fubao.dearbao.api.controller.auth.dto.response.KakaoLoginResponse;
import com.fubao.dearbao.api.controller.auth.dto.response.TokenRegenerateResponse;
import com.fubao.dearbao.api.service.auth.dto.InitMemberServiceDto;
import com.fubao.dearbao.api.service.auth.dto.KakaoLoginServiceDto;
import com.fubao.dearbao.api.service.auth.dto.LogoutServiceDto;
import com.fubao.dearbao.api.service.auth.dto.RegenerateTokenServiceDto;

public interface AuthService {

    KakaoLoginResponse kakaoLogin(KakaoLoginServiceDto dto);

    void initMember(InitMemberServiceDto dto);

    TokenRegenerateResponse tokenRegenerate(RegenerateTokenServiceDto dto);

    void logout(LogoutServiceDto dto);
}
