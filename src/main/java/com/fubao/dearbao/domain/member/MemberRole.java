package com.fubao.dearbao.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    GUEST("guest")
    ,MEMBER("user");
    private final String name;
}