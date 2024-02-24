package com.fubao.dearbao.api.controller.enquiry.dto.request;

import com.fubao.dearbao.api.service.enquiry.dto.EnquiryServiceDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class EnquiryRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @Builder
    public EnquiryRequest(String title, String content, String email) {
        this.title = title;
        this.content = content;
        this.email = email;
    }

    public EnquiryServiceDto toService(Long memberId) {
        return EnquiryServiceDto.of(title, content, email, memberId);
    }
}
