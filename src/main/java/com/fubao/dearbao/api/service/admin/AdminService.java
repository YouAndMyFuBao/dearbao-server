package com.fubao.dearbao.api.service.admin;

import com.fubao.dearbao.api.controller.admin.dto.response.AdminLoginResponse;
import com.fubao.dearbao.api.controller.auth.dto.response.KakaoLoginResponse;
import com.fubao.dearbao.api.controller.auth.dto.response.TokenRegenerateResponse;
import com.fubao.dearbao.api.service.admin.dto.AdminLoginDto;

public interface AdminService {

    AdminLoginResponse adminLogin(AdminLoginDto service);
}