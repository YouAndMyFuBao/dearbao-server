package com.fubao.dearbao.api.service.auth.dto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegenerateTokenServiceDto {

    private String accessToken;
    private String refreshToken;

    @Builder
    public RegenerateTokenServiceDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
    public static RegenerateTokenServiceDto of(String accessToken, String refreshToken){
        return RegenerateTokenServiceDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
