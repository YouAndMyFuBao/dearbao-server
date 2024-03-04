package com.fubao.dearbao.docs.auth;

import com.fubao.dearbao.api.controller.auth.AuthController;
import com.fubao.dearbao.api.controller.auth.dto.reqeust.InitMemberRequest;
import com.fubao.dearbao.api.controller.auth.dto.reqeust.KakaoLoginRequest;
import com.fubao.dearbao.api.controller.auth.dto.reqeust.LogoutRequest;
import com.fubao.dearbao.api.controller.auth.dto.reqeust.TokenRegenerateRequest;
import com.fubao.dearbao.api.controller.auth.dto.response.KakaoLoginResponse;
import com.fubao.dearbao.api.controller.auth.dto.response.TokenRegenerateResponse;
import com.fubao.dearbao.api.service.auth.AuthService;
import com.fubao.dearbao.api.service.auth.dto.KakaoLoginServiceDto;
import com.fubao.dearbao.api.service.auth.dto.RegenerateTokenServiceDto;
import com.fubao.dearbao.docs.RestDocsSupport;
import com.fubao.dearbao.domain.member.MemberGender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerDocsTest extends RestDocsSupport {

    private final AuthService authService = mock(AuthService.class);

    @Override
    protected Object initController() {
        return new AuthController(authService);
    }

    @DisplayName("카카오 로그인 API")
    @Test
    void kakaoLogin() throws Exception {
        KakaoLoginRequest request = KakaoLoginRequest.of("code");
        given(authService.kakaoLogin(any(KakaoLoginServiceDto.class)))
            .willReturn(KakaoLoginResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .isInitProfile(true).build());

        mockMvc.perform(
                post("/api/v1/auth/kakao")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(document("kakao Login",
                requestFields(
                    fieldWithPath("code").type(JsonFieldType.STRING)
                        .description("카카오 인가코드")
                ),
                responseFields(
                    fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN)
                        .description("성공 여부 boolean"),
                    fieldWithPath("code").type(JsonFieldType.STRING)
                        .description("success 또는 예외 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("요청에 성공하였습니다. 또는 예외 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                        .description("access token"),
                    fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                        .description("refresh token"),
                    fieldWithPath("data.initProfile").type(JsonFieldType.BOOLEAN)
                        .description("회원가입 완료 여부")
                )
            ));
    }

    @DisplayName("회원가입 후 초기설정 API")
    @Test
    void initMember() throws Exception {
        InitMemberRequest initMemberRequest = InitMemberRequest.of("peter", MemberGender.MALE);

        mockMvc.perform(
                post("/api/v1/auth/init")
                    .content(objectMapper.writeValueAsString(initMemberRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer dXNlcjpzZWNyZXQ=")
            )
            .andExpect(status().isOk())
            .andDo(document("init member",
                requestHeaders(
                    headerWithName("Authorization").description(
                        "JWT Token")),
                requestFields(
                    fieldWithPath("nickName").type(JsonFieldType.STRING)
                        .description("닉네임"),
                    fieldWithPath("title").type(JsonFieldType.STRING)
                        .description("MALE 또는 FEMALE 만 허용")
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

    @DisplayName("token 재생성 API")
    @Test
    void tokenRegenerate() throws Exception {
        //given
        TokenRegenerateRequest request = TokenRegenerateRequest.of("123", "123");
        given(authService.tokenRegenerate(any(RegenerateTokenServiceDto.class)))
            .willReturn(TokenRegenerateResponse.builder()
                .accessToken("access token")
                .refreshToken("refresh token")
                .build());
        mockMvc.perform(
                post("/api/v1/auth/token/refresh")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(document("regenerate token",
                requestFields(
                    fieldWithPath("accessToken").type(JsonFieldType.STRING)
                        .description("access token"),
                    fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                        .description("refresh token")
                ),
                responseFields(
                    fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN)
                        .description("성공 여부 boolean"),
                    fieldWithPath("code").type(JsonFieldType.STRING)
                        .description("success 또는 예외 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("요청에 성공하였습니다. 또는 예외 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                        .description("access token"),
                    fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                        .description("refresh token")
                )
            ));
    }

    @DisplayName("로그아웃 API")
    @Test
    void logout() throws Exception {
        String refreshToken = "refreshToken";
        LogoutRequest request = LogoutRequest.of(refreshToken);
        mockMvc.perform(
                post("/api/v1/auth/logout")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(document("logout",
                requestFields(
                    fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                        .description("refresh token")
                ),
                responseFields(
                    fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN)
                        .description("성공 여부 boolean"),
                    fieldWithPath("code").type(JsonFieldType.STRING)
                        .description("success 또는 예외 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("요청에 성공하였습니다. 또는 예외 메시지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                        .description("응답 데이터"),
                    fieldWithPath("data.message").type(JsonFieldType.STRING)
                        .description("로그아웃하였습니다.")
                )
            ));
    }
}
