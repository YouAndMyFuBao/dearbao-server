package com.fubao.dearbao.domain.mission.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberMissionState {
    ACTIVE("활동"),
    INACTIVE("비활동"),
    END("끝");
    private final String name;
}
