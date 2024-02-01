package com.fubao.dearbao.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByProviderIdAndState(String id,MemberState memberState);

    Optional<Member> findByIdAndState(Long memberId, MemberState memberState);

    boolean existsByNameAndState(String name, MemberState memberState);
}
