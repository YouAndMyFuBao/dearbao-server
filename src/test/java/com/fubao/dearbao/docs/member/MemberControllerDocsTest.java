package com.fubao.dearbao.docs.member;

import com.fubao.dearbao.api.controller.member.MemberController;
import com.fubao.dearbao.api.controller.member.dto.response.GetMemberNicknameResponse;
import com.fubao.dearbao.api.service.member.MemberService;
import com.fubao.dearbao.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerDocsTest extends RestDocsSupport {
    private final MemberService memberService = mock(MemberService.class);

    @Override
    protected Object initController() {
        return new MemberController(memberService);
    }

    @DisplayName("닉네임을 조회 API")
    @Test
    void getMemberNickname() throws Exception {
        //given
        String nickname = "peter";
        given(memberService.getMemberNickname(anyLong()))
            .willReturn(GetMemberNicknameResponse.builder()
                .nickname(nickname)
                .build());
        //when then
        mockMvc.perform(
                get("/api/v1/member/nickname")
                    .header("Authorization", "Bearer dXNlcjpzZWNyZXQ=")
            )
            .andExpect(status().isOk())
            .andDo(document("get nickname",
                requestHeaders(
                    headerWithName("Authorization").description(
                        "JWT Token")),
                responseFields(
                    fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN)
                        .description("성공 여부 boolean"),
                    fieldWithPath("code").type(JsonFieldType.STRING)
                        .description("success 또는 예외 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("요청에 성공하였습니다. 또는 예외 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답"),
                    fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                        .description("닉네임")
                )
            ));
    }
}
