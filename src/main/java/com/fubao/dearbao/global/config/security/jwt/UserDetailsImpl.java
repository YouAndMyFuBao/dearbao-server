package com.fubao.dearbao.global.config.security.jwt;

import com.fubao.dearbao.domain.member.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl extends Member implements UserDetails {

    public UserDetailsImpl(Member member) {
        super(member);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(super.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return super.getRole().getName();
    }

    @Override
    public String getUsername() {
        return String.valueOf(super.getId());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
