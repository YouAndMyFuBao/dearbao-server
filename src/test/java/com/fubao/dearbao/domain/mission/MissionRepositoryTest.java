package com.fubao.dearbao.domain.mission;

import com.fubao.dearbao.IntegrationTestSupport;
import com.fubao.dearbao.domain.mission.entity.Mission;
import com.fubao.dearbao.domain.mission.entity.MissionState;
import com.fubao.dearbao.global.common.exception.CustomException;
import com.fubao.dearbao.global.common.exception.ResponseCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class MissionRepositoryTest extends IntegrationTestSupport {

    @DisplayName("날짜에 맞는 미션을 조회한다.")
    @Test
    void findByOpenAt() {
        //given
        LocalDate today = LocalDate.of(2023, 11, 11);
        missionRepository.save(createMission(today, MissionState.ACTIVE));

        //when
        Optional<Mission> savedMission = missionRepository.findByOpenAtAndState(today,
            MissionState.ACTIVE);

        //then
        assertThat(savedMission).isNotEmpty();
    }

    @DisplayName("특정 상태의 미션을 조회한다.")
    @Test
    void findByState() {
        //given
        Mission activeMission = createMission(LocalDate.of(2023, 11, 10), MissionState.ACTIVE);
        Mission inActiveMission = createMission(LocalDate.of(2023, 11, 11), MissionState.INACTIVE);
        missionRepository.saveAll(List.of(activeMission, inActiveMission));
        //when
        Optional<Mission> optionalMission = missionRepository.findByState(MissionState.ACTIVE);
        //then
        assertThat(optionalMission).isNotEmpty()
            .get().extracting("state").isEqualTo(MissionState.ACTIVE);
    }

    @DisplayName("특정 상태의 미션리스트를 조회한다.")
    @Test
    void findAllByState() {
        //given
        Mission activeMission = createMission(LocalDate.of(2023, 11, 10), MissionState.ACTIVE);
        Mission inActiveMission1 = createMission(LocalDate.of(2023, 11, 11), MissionState.INACTIVE);
        Mission inActiveMission2 = createMission(LocalDate.of(2023, 11, 12), MissionState.INACTIVE);
        missionRepository.saveAll(List.of(activeMission, inActiveMission1, inActiveMission2));

        //when
        List<Mission> missions = missionRepository.findAllByState(MissionState.INACTIVE);

        //then
        assertThat(missions).hasSize(2)
            .extracting("state")
            .contains(
                MissionState.INACTIVE,MissionState.INACTIVE
            );
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