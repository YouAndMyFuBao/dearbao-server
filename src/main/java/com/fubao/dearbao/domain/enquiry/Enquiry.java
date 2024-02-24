package com.fubao.dearbao.domain.enquiry;

import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.global.common.entity.BaseEntity;
import com.fubao.dearbao.global.common.exception.CustomException;
import com.fubao.dearbao.global.common.exception.ResponseCode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "enquiry")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Enquiry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    private String email;

    @Builder
    public Enquiry(String title, String content, Member member, String email) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.email = email;
    }

    public static Enquiry create(Member member, String title, String content, String email) {
        return Enquiry.builder()
            .title(title)
            .content(content)
            .email(email)
            .member(member)
            .build();
    }

    public void validate() {
        if(this.content.length()>300)
            throw new CustomException(ResponseCode.ENQUIRY_OVER_CONTENT_LENGTH);
    }
}
