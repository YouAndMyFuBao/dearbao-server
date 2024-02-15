package com.fubao.dearbao;

import com.fubao.dearbao.api.service.mission.MissionService;
import com.fubao.dearbao.domain.member.MemberRepository;
import com.fubao.dearbao.domain.mission.MemberMissionRepository;
import com.fubao.dearbao.domain.mission.MissionRepository;
import com.fubao.dearbao.global.util.DateUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class IntegrationTestSupport {
    @Autowired
    protected MissionService missionService;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected MissionRepository missionRepository;
    @Autowired
    protected MemberMissionRepository memberMissionRepository;
    @Autowired
    protected DateUtil dateUtil;
    @AfterEach
    void tearDown() {
        memberMissionRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        missionRepository.deleteAllInBatch();
    }
}