package com.fubao.dearbao.api.controller.auth.dto.reqeust;

import com.fubao.dearbao.api.service.auth.dto.InitMemberServiceDto;
import com.fubao.dearbao.domain.member.MemberGender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InitMemberRequest {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickName;
    @NotNull(message = "호칭은 필수입니다.")
    private MemberGender title;

    @Builder
    private InitMemberRequest(String nickName, MemberGender title) {
        this.nickName = nickName;
        this.title = title;
    }

    public static InitMemberRequest of(String nickName, MemberGender title) {
        return InitMemberRequest.builder()
            .nickName(nickName)
            .title(title)
            .build();
    }

    public InitMemberServiceDto toServiceDto(Long memberId) {
        return InitMemberServiceDto.of(memberId,nickName, title);
    }
}
