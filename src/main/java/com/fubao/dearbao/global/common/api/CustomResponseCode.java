package com.fubao.dearbao.global.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomResponseCode {
    MEMBER_SIGNUP("회원가입을 성공 하였습니다."),
    MEMBER_LOGOUT("로그아웃하였습니다."),
    MEMBER_DEACTIVATION("회원 탈퇴하였습니다.");
    private final String message;
}
