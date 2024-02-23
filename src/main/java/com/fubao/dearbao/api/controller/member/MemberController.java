package com.fubao.dearbao.api.controller.member;

import com.fubao.dearbao.api.controller.auth.dto.reqeust.LogoutRequest;
import com.fubao.dearbao.api.controller.member.dto.response.GetMemberNicknameResponse;
import com.fubao.dearbao.api.service.member.MemberService;
import com.fubao.dearbao.global.common.api.CustomResponseCode;
import com.fubao.dearbao.global.common.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/nickname")
    public ResponseEntity<DataResponse<GetMemberNicknameResponse>> getMemberNickname() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(loggedInUser.getName());
        return ResponseEntity.ok(DataResponse.of(memberService.getMemberNickname(memberId)));
    }

    @DeleteMapping()
    public ResponseEntity<DataResponse<CustomResponseCode>> deactivate() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(loggedInUser.getName());
        memberService.deactivate(memberId);
        return ResponseEntity.ok(DataResponse.of(CustomResponseCode.MEMBER_DEACTIVATION));
    }
}
