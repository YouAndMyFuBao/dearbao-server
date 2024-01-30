package com.fubao.dearbao.api.controller.auth.dto.reqeust;

import com.fubao.dearbao.api.service.auth.dto.KakaoLoginServiceDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoLoginRequest {

    @NotBlank(message = "인가 코드는 필수 입니다.")
    private String code;

    public KakaoLoginRequest(String code) {
        this.code = code;
    }

    public static KakaoLoginRequest of(String code) {
        return new KakaoLoginRequest(code);
    }

    public KakaoLoginServiceDto toServiceDto() {
        return KakaoLoginServiceDto.of(code);
    }
}
