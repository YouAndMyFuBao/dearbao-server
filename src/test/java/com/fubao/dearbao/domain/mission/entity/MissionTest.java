package com.fubao.dearbao.domain.mission.entity;

import com.fubao.dearbao.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class MissionTest extends IntegrationTestSupport {

    @DisplayName("미션을 활동상태로 변경한다.")
    @Test
    void setActive() {
        //given
        Mission mission = createMission(LocalDate.of(2023, 11, 11), MissionState.INACTIVE);
        LocalDate date = LocalDate.of(2023, 12, 12);
        //when
        mission.setActive(date);

        //then
        assertThat(mission)
            .extracting("openAt", "state")
            .contains(date, MissionState.ACTIVE);
    }

    @DisplayName("미션을 종료 상태로 변경한다.")
    @Test
    void setEnd() {
        //given
        Mission mission = createMission(LocalDate.of(2023, 11, 11), MissionState.ACTIVE);
        //when
        mission.setEnd();

        //then
        assertThat(mission)
            .extracting("state")
            .isEqualTo(MissionState.END);
    }

    private Mission createMission(LocalDate localDate, MissionState missionState) {
        return Mission.builder()
            .answer("답변")
            .constructor("이름")
            .content("내용")
            .openAt(localDate)
            .state(missionState).build();
    }
}