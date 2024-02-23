package com.fubao.dearbao.api.controller.admin.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminLoginResponse {

    private String accessToken;

    @Builder
    public AdminLoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static AdminLoginResponse of(String accessToken) {
        return AdminLoginResponse.builder()
            .accessToken(accessToken)
            .build();
    }
}
