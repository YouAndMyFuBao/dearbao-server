package com.fubao.dearbao.api.controller.auth.dto.reqeust;

import com.fubao.dearbao.api.service.auth.dto.LogoutServiceDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LogoutRequest {
    @NotBlank(message = "Refresh Token Missing")
    private String refreshToken;

    public LogoutRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static LogoutRequest of(String refreshToken) {
        return new LogoutRequest(refreshToken);
    }

    public LogoutServiceDto toServiceDto() {
        return LogoutServiceDto.of(refreshToken);
    }
}
