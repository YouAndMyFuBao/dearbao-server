package com.fubao.dearbao.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberState {
    ACTIVE("활동"),
    INACTIVE("비활동");
    private final String name;
}
