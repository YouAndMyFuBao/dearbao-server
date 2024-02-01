package com.fubao.dearbao.api.service.auth.dto;

import com.fubao.dearbao.domain.member.MemberGender;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InitMemberServiceDto {

    private Long memberId;
    private String nickName;
    private MemberGender title;

    @Builder
    private InitMemberServiceDto(Long memberId, String nickName, MemberGender title) {
        this.memberId = memberId;
        this.nickName = nickName;
        this.title = title;
    }

    public static InitMemberServiceDto of(Long memberId, String nickName, MemberGender title) {
        return InitMemberServiceDto.builder()
            .memberId(memberId)
            .nickName(nickName)
            .title(title)
            .build();
    }
}
