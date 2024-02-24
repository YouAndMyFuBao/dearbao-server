package com.fubao.dearbao.api.service.admin;

import com.fubao.dearbao.api.controller.admin.dto.response.AdminLoginResponse;
import com.fubao.dearbao.api.controller.admin.dto.response.GetEnquiryResponse;
import com.fubao.dearbao.api.controller.admin.dto.response.GetMissionResponse;
import com.fubao.dearbao.api.controller.auth.dto.response.KakaoLoginResponse;
import com.fubao.dearbao.api.controller.auth.dto.response.TokenRegenerateResponse;
import com.fubao.dearbao.api.service.admin.dto.AdminLoginDto;
import com.fubao.dearbao.api.service.admin.dto.PostMissionDto;
import java.util.List;

public interface AdminService {

    AdminLoginResponse adminLogin(AdminLoginDto service);

    List<GetEnquiryResponse> getEnquiryList();

    List<GetMissionResponse> getMissionList();

    void postMission(PostMissionDto service);
}
