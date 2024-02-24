package com.fubao.dearbao.api.service.admin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostMissionDto {
    private String content;
    private String answer;
    private Long memberId;
    @Builder
    public PostMissionDto(String content, String answer, Long memberId) {
        this.content = content;
        this.answer = answer;
        this.memberId = memberId;
    }

    public static PostMissionDto of(String content, String answer, Long memberId) {
        return PostMissionDto.builder()
            .content(content)
            .answer(answer)
            .memberId(memberId)
            .build();
    }
}
