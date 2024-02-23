package com.fubao.dearbao.domain.member;

import com.fubao.dearbao.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private MemberRole role;
    @Enumerated(EnumType.STRING)
    private MemberGender gender;
    @Enumerated(EnumType.STRING)
    private MemberState state;

    @Builder
    public Member(String name, MemberRole role, MemberGender gender,
        MemberState state) {
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.state = state;
    }

    public Member(Member member) {
        this.state = member.getState();
        this.role = member.getRole();
        this.gender = member.getGender();
        this.id = member.getId();
        this.name = member.getName();
    }

    public static Member create() {
        return Member.builder()
            .role(MemberRole.ROLE_GUEST)
            .state(MemberState.ACTIVE)
            .build();
    }

    public void initMember(String nickName, MemberGender gender) {
        this.name = nickName;
        this.gender = gender;
        this.role = MemberRole.ROLE_MEMBER;
    }

    public boolean isPossibleNickname(String nickName) {
        return nickName.length() >= 2 && nickName.length() <= 8;
    }

    public boolean isInit() {
        return role.equals(MemberRole.ROLE_MEMBER);
    }

    public void deactivate() {
        this.state = MemberState.INACTIVE;
    }
}
