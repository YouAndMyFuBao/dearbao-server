package com.fubao.dearbao.api.service.admin;

import com.fubao.dearbao.api.controller.admin.dto.response.AdminLoginResponse;
import com.fubao.dearbao.api.service.admin.dto.AdminLoginDto;
import com.fubao.dearbao.domain.member.IdLogin;
import com.fubao.dearbao.domain.member.IdLoginRepository;
import com.fubao.dearbao.global.common.exception.CustomException;
import com.fubao.dearbao.global.common.exception.ResponseCode;
import com.fubao.dearbao.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final PasswordEncoder passwordEncoder;
    private final IdLoginRepository idLoginRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AdminLoginResponse adminLogin(AdminLoginDto service) {
        IdLogin idLogin = idLoginRepository.findByIdentification(service.getId())
            .orElseThrow(()-> new CustomException(ResponseCode.INVALID_LOGIN));
        if (!checkPassword(service.getPassword(), idLogin.getPassword()))
            throw new CustomException(ResponseCode.INVALID_LOGIN);
        String accessToken = jwtTokenProvider.createAdminToken(idLogin.getMember().getId().toString());
        return AdminLoginResponse.of(accessToken);
    }
    public boolean checkPassword(String password1, String password2) {
        return passwordEncoder.matches(password1, password2);
    }
}
