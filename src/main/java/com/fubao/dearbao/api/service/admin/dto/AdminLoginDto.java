package com.fubao.dearbao.api.service.admin.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdminLoginDto {

    private String id;
    private String password;

    @Builder
    public AdminLoginDto(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public static AdminLoginDto of(String id, String password) {
        return AdminLoginDto
            .builder()
            .id(id)
            .password(password)
            .build();
    }
}
