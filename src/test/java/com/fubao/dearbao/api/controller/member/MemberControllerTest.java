package com.fubao.dearbao.api.controller.member;

import com.fubao.dearbao.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTestSupport {

    @DisplayName("닉네임을 조회 API")
    @Test
    @WithMockUser(value = "1", roles = "USER")
    void getMemberNickname() throws Exception {
        //given
        //when then
        mockMvc.perform(
                get("/api/v1/member/nickname")
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