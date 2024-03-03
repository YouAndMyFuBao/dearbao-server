package com.fubao.dearbao.docs.health;

import com.fubao.dearbao.api.controller.healthcheck.HealthCheckController;
import com.fubao.dearbao.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HealthControllerDocsTest extends RestDocsSupport {

    @Override
    protected Object initController() {
        return new HealthCheckController();
    }

    @DisplayName("서버 구동 확인 API")
    @Test
    void healthCheck() throws Exception {
        mockMvc.perform(
                get("")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("health-check",
                responseBody()
            ));
    }
}
