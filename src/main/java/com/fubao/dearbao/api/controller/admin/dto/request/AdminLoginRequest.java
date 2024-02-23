package com.fubao.dearbao.api.controller.admin.dto.request;

import com.fubao.dearbao.api.service.admin.dto.AdminLoginDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminLoginRequest {
    String id;
    String password;

    public AdminLoginDto toService() {
        return AdminLoginDto.of(id,password);
    }
}