package com.fubao.dearbao.api.controller.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoLoginResponse {

    private String accessToken;
    private String refreshToken;
    private boolean isInitProfile;

    @Builder
    public KakaoLoginResponse(String accessToken, String refreshToken,boolean isInitProfile) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isInitProfile = isInitProfile;
    }

    public static KakaoLoginResponse of(String accessToken, String refreshToken,boolean isInitProfile) {
        return KakaoLoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .isInitProfile(isInitProfile)
            .build();
    }
}
