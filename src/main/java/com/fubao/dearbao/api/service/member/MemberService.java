package com.fubao.dearbao.api.service.member;

import com.fubao.dearbao.api.controller.member.dto.response.GetMemberNicknameResponse;

public interface MemberService {

    GetMemberNicknameResponse getMemberNickname(Long memberId);
}
