package com.fubao.dearbao.api.controller.auth;

import com.fubao.dearbao.ControllerTestSupport;
import com.fubao.dearbao.api.controller.auth.dto.reqeust.InitMemberRequest;
import com.fubao.dearbao.api.controller.auth.dto.reqeust.KakaoLoginRequest;
import com.fubao.dearbao.api.controller.auth.dto.reqeust.LogoutRequest;
import com.fubao.dearbao.api.controller.auth.dto.reqeust.TokenRegenerateRequest;
import com.fubao.dearbao.domain.member.MemberGender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends ControllerTestSupport {

    @DisplayName("카카오 로그인 API")
    @Test
    void kakaoLogin() throws Exception {
        //given
        KakaoLoginRequest kakaoLoginRequest = KakaoLoginRequest.of("1234567890");
        //when //then
        mockMvc.perform(
                post("/api/v1/auth/kakao")
                    .content(objectMapper.writeValueAsString(kakaoLoginRequest))
                    .contentType(MediaType.APPLICATION_JSON).with(csrf())
            )
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.isSuccess").value("true"),
                jsonPath("$.code").value("success"),
                jsonPath("$.message").value("요청에 성공하였습니다.")
            );
    }

    @DisplayName("회원가입 후 초기설정 API")
    @WithMockUser(roles = "GUEST", username = "1")
    @Test
    void initMember() throws Exception {
        //given
        InitMemberRequest initMemberRequest = InitMemberRequest.of("peter", MemberGender.MALE);
        //when //then
        mockMvc.perform(
                post("/api/v1/auth/init")
                    .content(objectMapper.writeValueAsString(initMemberRequest))
                    .contentType(MediaType.APPLICATION_JSON).with(csrf())
            )
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.isSuccess").value("true"),
                jsonPath("$.code").value("success"),
                jsonPath("$.message").value("요청에 성공하였습니다.")
            );
    }

    @DisplayName("token 재생성 API")
    @Test
    void tokenRegenerate() throws Exception {
        //given
        TokenRegenerateRequest request = TokenRegenerateRequest.of("123", "123");
        //when //then
        mockMvc.perform(
                post("/api/v1/auth/token/refresh")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON).with(csrf())
            )
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.isSuccess").value("true"),
                jsonPath("$.code").value("success"),
                jsonPath("$.message").value("요청에 성공하였습니다.")
            );
    }

    @DisplayName("로그아웃 API")
    @Test
    void logout() throws Exception {
        //given
        String refreshToken = "refreshToken";
        LogoutRequest request = LogoutRequest.of(refreshToken);
        //when then
        mockMvc.perform(
                post("/api/v1/auth/logout")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON).with(csrf())
            )
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.isSuccess").value("true"),
                jsonPath("$.code").value("success"),
                jsonPath("$.message").value("요청에 성공하였습니다.")
            );
    }
}