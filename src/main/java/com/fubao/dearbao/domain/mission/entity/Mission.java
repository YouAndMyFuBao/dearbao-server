package com.fubao.dearbao.domain.mission.entity;

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
import java.time.LocalDate;

@Entity(name = "mission")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Mission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String constructor;
    private String answer;
    @Enumerated(EnumType.STRING)
    private MissionState state;
    private LocalDate openAt;
    @Builder
    public Mission(String content, String constructor, String answer, MissionState state,
        LocalDate openAt) {
        this.content = content;
        this.constructor = constructor;
        this.answer = answer;
        this.state = state;
        this.openAt = openAt;
    }

    public void setActive(LocalDate date) {
        this.openAt = date;
        this.state = MissionState.ACTIVE;
    }

    public void setEnd() {
        this.state = MissionState.END;
    }
}
