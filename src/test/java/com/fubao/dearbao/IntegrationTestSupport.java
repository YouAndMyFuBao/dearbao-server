package com.fubao.dearbao;

import com.fubao.dearbao.api.service.enquiry.EnquiryService;
import com.fubao.dearbao.api.service.member.MemberService;
import com.fubao.dearbao.api.service.mission.MissionService;
import com.fubao.dearbao.domain.enquiry.EnquiryRepository;
import com.fubao.dearbao.domain.member.MemberRepository;
import com.fubao.dearbao.domain.member.SocialLoginRepository;
import com.fubao.dearbao.domain.mission.MemberMissionRepository;
import com.fubao.dearbao.domain.mission.MissionRepository;
import com.fubao.dearbao.global.util.DateUtil;
import com.fubao.dearbao.global.util.SlackWebhookUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public abstract class IntegrationTestSupport {

    @Autowired
    protected MissionService missionService;
    @Autowired
    protected MemberService memberService;
    @Autowired
    protected EnquiryService enquiryService;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected MissionRepository missionRepository;
    @Autowired
    protected EnquiryRepository enquiryRepository;
    @Autowired
    protected SocialLoginRepository socialLoginRepository;
    @Autowired
    protected MemberMissionRepository memberMissionRepository;
    @Autowired
    protected DateUtil dateUtil;
    @MockBean
    protected SlackWebhookUtil slackWebhookUtil;

    @AfterEach
    void tearDown() {
        socialLoginRepository.deleteAllInBatch();
        memberMissionRepository.deleteAllInBatch();
        enquiryRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
        missionRepository.deleteAllInBatch();
    }
}
