package com.fubao.dearbao.api.service.enquiry;

import com.fubao.dearbao.api.service.enquiry.dto.EnquiryServiceDto;
import com.fubao.dearbao.domain.enquiry.Enquiry;
import com.fubao.dearbao.domain.enquiry.EnquiryRepository;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberRepository;
import com.fubao.dearbao.domain.member.MemberState;
import com.fubao.dearbao.global.common.exception.CustomException;
import com.fubao.dearbao.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnquiryServiceImpl implements EnquiryService {

    private final EnquiryRepository enquiryRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void enquiry(EnquiryServiceDto service) {
        Member member = findMemberById(service.getMemberId());
        Enquiry enquiry = Enquiry.create(member, service.getTitle(), service.getContent(),service.getEmail());
        enquiry.validate();
        enquiryRepository.save(enquiry);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findByIdAndState(memberId, MemberState.ACTIVE)
            .orElseThrow(
                () -> new CustomException(ResponseCode.NOT_FOUND_MEMBER)
            );
    }
}
