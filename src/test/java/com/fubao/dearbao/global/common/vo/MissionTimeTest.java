package com.fubao.dearbao.global.common.vo;

import com.fubao.dearbao.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class MissionTimeTest extends IntegrationTestSupport {
    @DisplayName("09:00:00 ~ 20:59:59 사이일 경우 true를 반환한다.")
    @Test
    void isOpenTimeInTime() {
        //given
        LocalTime todayTime = LocalTime.of(9,0,0);
        //when then
        assertThat(MissionTime.isMessageOpenTime(todayTime)).isFalse();
    }
    @DisplayName("21:00:00 ~ 08:59:59 사이일 경우 false를 반환한다.")
    @Test
    void isOpenTimeOutTime() {
        //given
        LocalTime todayTime = LocalTime.of(21,0,0);
        //when then
        assertThat(MissionTime.isMessageOpenTime(todayTime)).isTrue();
    }
}