package com.fubao.dearbao.api.controller.admin.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetEnquiryResponse {

    private String title;
    private String content;
    private String email;
    private String name;
    private String date;
    @Builder
    public GetEnquiryResponse(String title, String content, String email, String name,
        String date) {
        this.title = title;
        this.content = content;
        this.email = email;
        this.name = name;
        this.date = date;
    }
}
