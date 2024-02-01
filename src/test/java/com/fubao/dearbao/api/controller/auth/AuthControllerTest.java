package com.fubao.dearbao.api.controller.auth;

import com.fubao.dearbao.ControllerTestSupport;
import com.fubao.dearbao.api.controller.auth.dto.reqeust.InitMemberRequest;
import com.fubao.dearbao.api.controller.auth.dto.reqeust.KakaoLoginRequest;
import com.fubao.dearbao.domain.member.MemberGender;
import com.fubao.dearbao.domain.member.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
            .andDo(print())
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
            .andDo(print())
            .andExpect(status().isOk())
            .andExpectAll(
                jsonPath("$.isSuccess").value("true"),
                jsonPath("$.code").value("success"),
                jsonPath("$.message").value("요청에 성공하였습니다.")
            );
    }
}