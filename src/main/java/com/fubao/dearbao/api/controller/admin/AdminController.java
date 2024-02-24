package com.fubao.dearbao.api.controller.admin;

import com.fubao.dearbao.api.controller.admin.dto.request.AdminLoginRequest;
import com.fubao.dearbao.api.controller.admin.dto.response.AdminLoginResponse;
import com.fubao.dearbao.api.controller.admin.dto.response.GetEnquiryResponse;
import com.fubao.dearbao.api.controller.auth.dto.reqeust.KakaoLoginRequest;
import com.fubao.dearbao.api.controller.auth.dto.response.KakaoLoginResponse;
import com.fubao.dearbao.api.service.admin.AdminService;
import com.fubao.dearbao.global.common.response.DataResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<DataResponse<AdminLoginResponse>> adminLogin(
        @Validated @RequestBody AdminLoginRequest request) {
        return ResponseEntity.ok(DataResponse.of(adminService.adminLogin(request.toService())));
    }

    @GetMapping("/enquiry")
    public ResponseEntity<DataResponse<List<GetEnquiryResponse>>> getEnquiryList() {
        return ResponseEntity.ok(DataResponse.of(adminService.getEnquiryList()));
    }
}
