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
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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

    private Mission createMission(LocalDate localDate, MissionState missionState) {
        return Mission.builder()
            .answer("답변")
            .constructor("이름")
            .content("내용")
            .openAt(localDate)
            .state(missionState).build();
    }
}