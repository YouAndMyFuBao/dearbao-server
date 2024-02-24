package com.fubao.dearbao.domain.mission.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionState {
    ACTIVE("오픈 중"),
    INACTIVE("오픈 대기"),
    END("오픈 완료");
    private final String name;
}
