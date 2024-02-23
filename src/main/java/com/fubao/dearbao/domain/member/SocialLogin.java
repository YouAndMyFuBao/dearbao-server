package com.fubao.dearbao.domain.member;

import com.fubao.dearbao.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "social_login")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SocialLogin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String providerId;
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public SocialLogin(String providerId, Member member) {
        this.providerId = providerId;
        this.member = member;
    }

    public static SocialLogin create(Member member,String providerId) {
        return SocialLogin.of(member,providerId);
    }

    private static SocialLogin of(Member member,String providerId) {
        return SocialLogin.builder()
            .member(member)
            .providerId(providerId)
            .build();
    }
}
