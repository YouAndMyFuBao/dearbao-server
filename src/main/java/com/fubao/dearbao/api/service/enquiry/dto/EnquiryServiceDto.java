package com.fubao.dearbao.api.service.enquiry.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EnquiryServiceDto {

    private String title;
    private String content;
    private String email;
    private Long memberId;

    @Builder
    public EnquiryServiceDto(String title, String content, String email, Long memberId) {
        this.title = title;
        this.content = content;
        this.email = email;
        this.memberId = memberId;
    }

    public static EnquiryServiceDto of(String title, String content, String email, Long memberId) {
        return EnquiryServiceDto.builder()
            .content(content)
            .title(title)
            .email(email)
            .memberId(memberId)
            .build();
    }
}
