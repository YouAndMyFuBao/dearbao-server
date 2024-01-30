package com.fubao.dearbao.api.controller.auth;

import com.fubao.dearbao.ControllerTestSupport;
import com.fubao.dearbao.api.controller.auth.dto.reqeust.KakaoLoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends ControllerTestSupport {

    @DisplayName("카카오 로그인")
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
}