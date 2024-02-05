package com.fubao.dearbao.api.service.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LogoutServiceDto {
    private String refreshToken;

    public LogoutServiceDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static LogoutServiceDto of(String refreshToken) {
        return new LogoutServiceDto(refreshToken);
    }
}
