package com.fubao.dearbao.api.controller.auth;

import com.fubao.dearbao.api.controller.auth.dto.reqeust.KakaoLoginRequest;
import com.fubao.dearbao.api.controller.auth.dto.response.KakaoLoginResponse;
import com.fubao.dearbao.api.service.auth.AuthService;
import com.fubao.dearbao.global.common.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/kakao")
    public ResponseEntity<DataResponse<KakaoLoginResponse>> kakaoLogin(
        @Validated @RequestBody KakaoLoginRequest request) {
        return ResponseEntity.ok(DataResponse.of(authService.kakaoLogin(request.toServiceDto())));
    }

    @GetMapping("/kakao/code")
    public ResponseEntity<String> code(@RequestParam String code) {
        return ResponseEntity.ok(code);
    }
}
