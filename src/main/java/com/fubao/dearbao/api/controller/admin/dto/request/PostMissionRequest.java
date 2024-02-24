package com.fubao.dearbao.api.controller.admin.dto.request;

import com.fubao.dearbao.api.service.admin.dto.PostMissionDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostMissionRequest {

    private String content;
    private String answer;

    public PostMissionDto toService(Long memberId) {
        return PostMissionDto.of(this.content, this.answer, memberId);
    }
}
