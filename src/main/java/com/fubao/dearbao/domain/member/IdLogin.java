package com.fubao.dearbao.domain.member;

import com.fubao.dearbao.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "id_login")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class IdLogin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String identification;
    private String password;
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public IdLogin(String identification, String password, Member member) {
        this.identification = identification;
        this.password = password;
        this.member = member;
    }
}
