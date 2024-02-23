package com.fubao.dearbao.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SocialLoginRepository extends JpaRepository<SocialLogin,Long> {

    Optional<SocialLogin> findByProviderId(String id);
}
