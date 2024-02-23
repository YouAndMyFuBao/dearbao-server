package com.fubao.dearbao.api.service.member;

import com.fubao.dearbao.api.controller.member.dto.response.GetMemberNicknameResponse;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberRepository;
import com.fubao.dearbao.domain.member.MemberState;
import com.fubao.dearbao.domain.mission.MemberMissionRepository;
import com.fubao.dearbao.domain.mission.entity.MemberMission;
import com.fubao.dearbao.global.common.exception.CustomException;
import com.fubao.dearbao.global.common.exception.ResponseCode;
import com.fubao.dearbao.global.common.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMissionRepository memberMissionRepository;

    @Override
    public GetMemberNicknameResponse getMemberNickname(
        Long memberId) {
        return GetMemberNicknameResponse.of(findMemberById(memberId).getName());
    }

    @Override
    @Transactional
    public void deactivate(Long memberId) {
        Member member = findMemberById(memberId);
        List<MemberMission> memberMissions = findMemberMissionBy(memberId);
        memberMissions.forEach(
            MemberMission::deactivate
        );
        member.deactivate();
    }

    private List<MemberMission> findMemberMissionBy(Long memberId) {
        return memberMissionRepository.findAllByMemberId(memberId);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findByIdAndState(memberId, MemberState.ACTIVE)
            .orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_MEMBER)
            );
    }
}
