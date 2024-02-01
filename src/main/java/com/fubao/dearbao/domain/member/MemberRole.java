package com.fubao.dearbao.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    ROLE_GUEST("GUEST")
    ,ROLE_MEMBER("MEMBER");
    private final String name;
}