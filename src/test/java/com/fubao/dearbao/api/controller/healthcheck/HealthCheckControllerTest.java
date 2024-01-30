package com.fubao.dearbao.api.controller.healthcheck;

import com.fubao.dearbao.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HealthCheckControllerTest extends ControllerTestSupport {

    @DisplayName("헬스체크")
    @Test
    void hello() throws Exception {
        //given

        //when //then
        mockMvc.perform(
                get("/")
            )
            .andExpect(status().isOk())
            .andExpect(content().string("The service is up and running..."));
    }
}