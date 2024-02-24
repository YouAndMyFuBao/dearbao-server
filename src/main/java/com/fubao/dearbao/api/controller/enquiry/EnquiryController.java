package com.fubao.dearbao.api.controller.enquiry;

import com.fubao.dearbao.api.controller.admin.dto.response.AdminLoginResponse;
import com.fubao.dearbao.api.controller.enquiry.dto.request.EnquiryRequest;
import com.fubao.dearbao.api.service.enquiry.EnquiryService;
import com.fubao.dearbao.global.common.exception.ResponseCode;
import com.fubao.dearbao.global.common.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/enquiry")
public class EnquiryController {

    private final EnquiryService enquiryService;

    @PostMapping()
    public ResponseEntity<DataResponse<ResponseCode>> enquiry(@Validated @RequestBody EnquiryRequest request) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(loggedInUser.getName());
        enquiryService.enquiry(request.toService(memberId));
        return ResponseEntity.ok(DataResponse.of(ResponseCode.OK));
    }
}
