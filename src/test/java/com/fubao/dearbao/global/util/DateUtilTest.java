package com.fubao.dearbao.global.util;

import com.fubao.dearbao.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class DateUtilTest extends IntegrationTestSupport {
    @DisplayName("시간 값을 HH:mm:ss 형태로 만든다.")
    @Test
    void toResponseTimeFormat() {
        //given
        LocalTime localTime = LocalTime.of(11,11,11);
        //when
        String result = dateUtil.toResponseTimeFormat(localTime);
        //then
        assertThat(result).isEqualTo("11:11:11");
    }
}