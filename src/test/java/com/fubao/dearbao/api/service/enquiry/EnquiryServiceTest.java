package com.fubao.dearbao.api.service.enquiry;

import com.fubao.dearbao.IntegrationTestSupport;
import com.fubao.dearbao.api.service.enquiry.dto.EnquiryServiceDto;
import com.fubao.dearbao.domain.enquiry.Enquiry;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberGender;
import com.fubao.dearbao.domain.member.MemberRole;
import com.fubao.dearbao.domain.member.MemberState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class EnquiryServiceTest extends IntegrationTestSupport {

    @DisplayName("문의하기")
    @Test
    void enquiry() {
        //given
        String title = "enquiry";
        String email = "dsk08208@gmail.com";
        String content = "content";
        Member member = memberRepository.save(createMember("peter", MemberGender.MALE));
        EnquiryServiceDto enquiryServiceDto = EnquiryServiceDto.builder()
            .email(email)
            .title(title)
            .content(content)
            .memberId(member.getId())
            .build();
        //when
        enquiryService.enquiry(enquiryServiceDto);
        //then
        List<Enquiry> enquiries = enquiryRepository.findAll();
        assertThat(enquiries).hasSize(1)
            .extracting("title", "content", "email")
            .contains(
                tuple(title, content, email)
            );
    }

    private Member createMember(String nickname, MemberGender memberGender) {
        return Member.builder()
            .name(nickname)
            .gender(memberGender)
            .state(MemberState.ACTIVE)
            .role(MemberRole.ROLE_MEMBER)
            .build();
    }
}