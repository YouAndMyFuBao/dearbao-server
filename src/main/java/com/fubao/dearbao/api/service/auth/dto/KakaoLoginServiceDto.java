package com.fubao.dearbao.api.service.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@RequiredArgsConstructor
public class KakaoLoginServiceDto {

    private String code;

    @Builder
    private KakaoLoginServiceDto(String code) {
        this.code = code;
    }

    public static KakaoLoginServiceDto of(String code) {
        return KakaoLoginServiceDto.builder()
            .code(code)
            .build();
    }

    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        return body;
    }
}
