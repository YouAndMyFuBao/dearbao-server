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

    @Builder
    public KakaoLoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static KakaoLoginResponse of(String accessToken, String refreshToken) {
        return KakaoLoginResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
