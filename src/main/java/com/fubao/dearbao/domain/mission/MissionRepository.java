package com.fubao.dearbao.domain.mission;

import com.fubao.dearbao.domain.mission.entity.Mission;
import com.fubao.dearbao.domain.mission.entity.MissionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {

    Optional<Mission> findByOpenAtAndState(LocalDate now, MissionState missionState);

    List<Mission> findAllByState(MissionState missionState);

    Optional<Mission> findByState(MissionState missionState);
}
