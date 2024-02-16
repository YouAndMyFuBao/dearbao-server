package com.fubao.dearbao.api.controller.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMemberNicknameResponse {

    private String nickname;

    @Builder
    public GetMemberNicknameResponse(String nickname) {
        this.nickname = nickname;
    }

    public static GetMemberNicknameResponse of(String name) {
        return new GetMemberNicknameResponse(name);
    }
}
