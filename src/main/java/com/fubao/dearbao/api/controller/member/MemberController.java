package com.fubao.dearbao.api.controller.member;

import com.fubao.dearbao.api.controller.member.dto.response.GetMemberNicknameResponse;
import com.fubao.dearbao.api.service.member.MemberService;
import com.fubao.dearbao.global.common.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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
}
