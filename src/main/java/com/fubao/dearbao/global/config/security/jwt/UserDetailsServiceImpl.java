package com.fubao.dearbao.global.config.security.jwt;

import java.util.Optional;
import java.util.UUID;
import com.fubao.dearbao.domain.member.Member;
import com.fubao.dearbao.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<Member> optionalMember = memberRepository.findById(Long.valueOf(username));
        if (optionalMember.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        UserDetails user = new UserDetailsImpl(optionalMember.get());
        return User.withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(user.getAuthorities())
            .build();
    }
}