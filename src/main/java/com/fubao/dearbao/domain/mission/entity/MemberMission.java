package com.fubao.dearbao.domain.mission.entity;

import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity(name = "member_mission")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberMission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;
    @Enumerated(EnumType.STRING)
    private MemberMissionState state;

    @Builder
    public MemberMission(String content, Member member, Mission mission, MemberMissionState state) {
        this.content = content;
        this.member = member;
        this.mission = mission;
        this.state = state;
    }

    public void setEnd() {
        this.state = MemberMissionState.END;
    }

    public void deactivate() {
        this.state = MemberMissionState.INACTIVE;
    }
}
