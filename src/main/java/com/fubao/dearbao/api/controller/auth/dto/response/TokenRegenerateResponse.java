package com.fubao.dearbao.api.controller.auth.dto.response;

import com.fubao.dearbao.global.common.vo.AuthToken;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRegenerateResponse {

    private String accessToken;
    private String refreshToken;

    @Builder
    public TokenRegenerateResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenRegenerateResponse of(AuthToken authToken) {
        return TokenRegenerateResponse.builder()
            .accessToken(authToken.getAccessToken())
            .refreshToken(authToken.getRefreshToken())
            .build();
    }
}
