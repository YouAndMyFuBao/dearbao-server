package com.fubao.dearbao.global.common.vo;

import com.fubao.dearbao.api.controller.auth.dto.response.KakaoLoginResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthToken {
    private String accessToken;
    private String refreshToken;

    @Builder
    public AuthToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static AuthToken of(String accessToken, String refreshToken) {
        return AuthToken.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public KakaoLoginResponse toKakaoLoginResponse(boolean isInitProfile) {
        return KakaoLoginResponse.of(accessToken,refreshToken,isInitProfile);
    }
}
