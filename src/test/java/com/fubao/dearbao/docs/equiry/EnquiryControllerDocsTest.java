package com.fubao.dearbao.docs.equiry;

import com.fubao.dearbao.api.controller.auth.AuthController;
import com.fubao.dearbao.api.controller.enquiry.EnquiryController;
import com.fubao.dearbao.api.controller.enquiry.dto.request.EnquiryRequest;
import com.fubao.dearbao.api.service.auth.AuthService;
import com.fubao.dearbao.api.service.enquiry.EnquiryService;
import com.fubao.dearbao.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EnquiryControllerDocsTest extends RestDocsSupport {

    private final EnquiryService enquiryService = mock(EnquiryService.class);

    @Override
    protected Object initController() {
        return new EnquiryController(enquiryService);
    }

    @DisplayName("문의하기 API")
    @Test
    void enquiry() throws Exception {
        EnquiryRequest request = EnquiryRequest.builder()
            .title("title")
            .email("dsk08208@gmail.com")
            .content("content").build();

        mockMvc.perform(
                post("/api/v1/enquiry")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer dXNlcjpzZWNyZXQ=")
            )
            .andExpect(status().isOk())
            .andDo(document("enquiry",
                requestHeaders(
                    headerWithName("Authorization").description(
                        "JWT Token")),
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING)
                        .description("카카오 인가코드"),
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("dsk08208@gmail.com"),
                    fieldWithPath("content").type(JsonFieldType.STRING)
                        .description("content")
                ),
                responseFields(
                    fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN)
                        .description("성공 여부 boolean"),
                    fieldWithPath("code").type(JsonFieldType.STRING)
                        .description("success 또는 예외 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("요청에 성공하였습니다. 또는 예외 메시지"),
                    fieldWithPath("data").type(JsonFieldType.STRING)
                        .description("OK")
                )
            ));
    }
}
