package com.fubao.dearbao.domain.mission;

import com.fubao.dearbao.domain.mission.entity.MemberMission;
import com.fubao.dearbao.domain.mission.entity.MemberMissionState;
import com.fubao.dearbao.domain.mission.entity.MissionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberMissionRepository extends JpaRepository<MemberMission, Long> {

    Optional<MemberMission> findByMemberIdAndState(Long memberId, MemberMissionState missionState);

    List<MemberMission> findAllByMissionId(Long id);
}
