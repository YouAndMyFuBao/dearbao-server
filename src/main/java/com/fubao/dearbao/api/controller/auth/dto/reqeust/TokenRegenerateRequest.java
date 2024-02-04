package com.fubao.dearbao.api.controller.auth.dto.reqeust;

import com.fubao.dearbao.api.service.auth.dto.RegenerateTokenServiceDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRegenerateRequest {

    @NotBlank(message = "Access Token Missing")
    private String accessToken;

    @NotBlank(message = "Refresh Token Missing")
    private String refreshToken;

    @Builder
    public TokenRegenerateRequest(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenRegenerateRequest of(String accessToken, String refreshToken) {
        return TokenRegenerateRequest.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public RegenerateTokenServiceDto toServiceDto() {
        return RegenerateTokenServiceDto.of(this.accessToken, this.refreshToken);
    }
}
