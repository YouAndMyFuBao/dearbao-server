package com.fubao.dearbao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fubao.dearbao.api.controller.auth.AuthController;
import com.fubao.dearbao.api.controller.healthcheck.HealthCheckController;
import com.fubao.dearbao.api.controller.mission.MissionController;
import com.fubao.dearbao.api.service.auth.AuthService;
import com.fubao.dearbao.api.service.mission.MissionService;
import com.fubao.dearbao.global.util.SlackWebhookUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
    AuthController.class,
    HealthCheckController.class,
    MissionController.class
})
@WithMockUser
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected SlackWebhookUtil slackWebhookUtil;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected MissionService missionService;

    @MockBean
    protected SecurityContextHolder securityContextHolder;
}
