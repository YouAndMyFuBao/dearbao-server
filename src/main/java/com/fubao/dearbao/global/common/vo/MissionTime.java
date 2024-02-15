package com.fubao.dearbao.global.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public enum MissionTime {
    AM(LocalTime.of(9, 0,0)),
    PM(LocalTime.of(21, 0,0));
    private final LocalTime time;

    public static boolean isMessageOpenTime(LocalTime now) {
        return now.equals(PM.getTime())||now.isAfter(PM.getTime()) && now.isBefore(AM.getTime());
    }
}
