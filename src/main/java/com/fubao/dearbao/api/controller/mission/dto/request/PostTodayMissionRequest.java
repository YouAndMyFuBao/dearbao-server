package com.fubao.dearbao.api.controller.mission.dto.request;

import com.fubao.dearbao.api.service.mission.dto.PostTodayMissionDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostTodayMissionRequest {

    @NotBlank(message = "내용은 비울 수 없습니다.")
    String content;

    public PostTodayMissionRequest(String content) {
        this.content = content;
    }

    public PostTodayMissionDto toService(Long memberId) {
        return PostTodayMissionDto.of(content, memberId);
    }
}
