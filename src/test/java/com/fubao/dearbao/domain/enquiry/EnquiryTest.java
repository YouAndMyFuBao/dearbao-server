package com.fubao.dearbao.domain.enquiry;

import com.fubao.dearbao.IntegrationTestSupport;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberGender;
import com.fubao.dearbao.domain.member.MemberRole;
import com.fubao.dearbao.domain.member.MemberState;
import com.fubao.dearbao.global.common.exception.CustomException;
import com.fubao.dearbao.global.common.exception.ResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class EnquiryTest extends IntegrationTestSupport {

    @DisplayName("Enquiry의 content의 길이가 300을 초과할 경우 예외가 발생한다.")
    @Test
    void validate() {
        //given
        String content = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        Member member = createMember("peter", MemberGender.MALE);
        Enquiry enquiry = createEnquiry("title", content, "dsk08208@gmail.com", member);
        //when then
        assertThat(content.length()).isEqualTo(301);
        assertThatThrownBy(enquiry::validate)
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.ENQUIRY_OVER_CONTENT_LENGTH);
    }

    private Member createMember(String nickname, MemberGender memberGender) {
        return Member.builder()
            .name(nickname)
            .gender(memberGender)
            .state(MemberState.ACTIVE)
            .role(MemberRole.ROLE_MEMBER)
            .build();
    }

    private Enquiry createEnquiry(String title, String content, String email, Member member) {
        return Enquiry.builder()
            .title(title)
            .content(content)
            .email(email)
            .member(member)
            .build();
    }
}