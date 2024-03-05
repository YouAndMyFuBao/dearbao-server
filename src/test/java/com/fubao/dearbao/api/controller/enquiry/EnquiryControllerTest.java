package com.fubao.dearbao.api.controller.enquiry;

import com.fubao.dearbao.ControllerTestSupport;
import com.fubao.dearbao.api.controller.enquiry.dto.request.EnquiryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EnquiryControllerTest extends ControllerTestSupport {

    @DisplayName("문의하기 API")
    @Test
    @WithMockUser(roles = "MEMBER", username = "1")
    void enquiry() throws Exception {
        EnquiryRequest request = EnquiryRequest.builder()
            .title("title")
            .email("dsk08208@gmail.com")
            .content("content").build();

        mockMvc.perform(
                post("/api/v1/enquiry")
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