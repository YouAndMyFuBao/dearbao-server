package com.fubao.dearbao.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum MemberGender {
    MALE("삼툔"),
    FEMALE("임오");
    private final String name;
}
