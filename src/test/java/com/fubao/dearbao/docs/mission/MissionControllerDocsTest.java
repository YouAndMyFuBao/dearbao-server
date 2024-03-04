package com.fubao.dearbao.docs.mission;

import com.fubao.dearbao.api.controller.member.MemberController;
import com.fubao.dearbao.api.controller.mission.MissionController;
import com.fubao.dearbao.api.controller.mission.dto.response.DailyMessageResponse;
import com.fubao.dearbao.api.controller.mission.dto.response.DailyMissionBaseResponse;
import com.fubao.dearbao.api.controller.mission.dto.response.DailyMissionResponse;
import com.fubao.dearbao.api.controller.mission.dto.response.GetMyMissionResponse;
import com.fubao.dearbao.api.service.member.MemberService;
import com.fubao.dearbao.api.service.mission.MissionService;
import com.fubao.dearbao.docs.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MissionControllerDocsTest extends RestDocsSupport {

    private final MissionService missionService = mock(MissionService.class);

    @Override
    protected Object initController() {
        return new MissionController(missionService);
    }

    @DisplayName("데일리 미션 API")
    @Test
    void dailyMissionWithDailyMissionNotOpenTime() throws Exception {
        DailyMissionBaseResponse dailyMissionBaseResponse = DailyMissionBaseResponse.builder()
            .nickname("peter")
            .isMissionSuccess(true)
            .isMessageOpenTime(false)
            .build();
        given(missionService.dailyMission(anyLong(),any(LocalDateTime.class)))
            .willReturn(DailyMissionResponse.of(dailyMissionBaseResponse,"21:00:00"));
        mockMvc.perform(
                get("/api/v1/mission")
                    .header("Authorization", "Bearer dXNlcjpzZWNyZXQ=")
            )
            .andExpect(status().isOk())
            .andDo(document("daily mission with message not open time",
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
                        .description("닉네임"),
                    fieldWithPath("data.remainingTime").type(JsonFieldType.STRING)
                        .description("남은 시간 (09:00:00 - 20:59:59일 때, 활성)"),
                    fieldWithPath("data.messageOpenTime").type(JsonFieldType.BOOLEAN)
                        .description("메세지 오픈 시간 여부"),
                    fieldWithPath("data.missionSuccess").type(JsonFieldType.BOOLEAN)
                        .description("미션 성공 여부")
                )
            ));
    }
    @DisplayName("데일리 미션 API")
    @Test
    void dailyMissionWithMessageOpenTime() throws Exception {
        DailyMissionBaseResponse dailyMissionBaseResponse = DailyMissionBaseResponse.builder()
            .nickname("peter")
            .isMissionSuccess(true)
            .isMessageOpenTime(false)
            .build();
        given(missionService.dailyMission(anyLong(),any(LocalDateTime.class)))
            .willReturn(DailyMessageResponse.of(dailyMissionBaseResponse,"안녕"));
        mockMvc.perform(
                get("/api/v1/mission")
                    .header("Authorization", "Bearer dXNlcjpzZWNyZXQ=")
            )
            .andExpect(status().isOk())
            .andDo(document("daily mission with message open time",
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
                        .description("닉네임"),
                    fieldWithPath("data.message").type(JsonFieldType.STRING)
                        .description("푸바오 메시지 (21:00:00 - 08:59:59일 때, 활성)"),
                    fieldWithPath("data.messageOpenTime").type(JsonFieldType.BOOLEAN)
                        .description("메세지 오픈 시간 여부"),
                    fieldWithPath("data.missionSuccess").type(JsonFieldType.BOOLEAN)
                        .description("미션 성공 여부")
                )
            ));
    }
    @DisplayName("나의 미션을 조회한다.")
    @Test
    @WithMockUser(value = "1", roles = "USER")
    void getMyMission() throws Exception {
        given(missionService.getMyMission(anyLong()))
            .willReturn(Collections.singletonList(GetMyMissionResponse.builder()
                    .date("02:18")
                    .content("안녕")
                .build()));
        mockMvc.perform(
                get("/api/v1/mission/my-mission")
                    .header("Authorization", "Bearer dXNlcjpzZWNyZXQ=")
            )
            .andExpect(status().isOk())
            .andDo(document("get my mission",
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
                    fieldWithPath("data").type(JsonFieldType.ARRAY)
                        .description("응답"),
                    fieldWithPath("data[].date").type(JsonFieldType.STRING)
                        .description("날짜 (MM:dd 형식)"),
                    fieldWithPath("data[].content").type(JsonFieldType.STRING)
                        .description("수행 내용")
                )
            ));
    }
}
