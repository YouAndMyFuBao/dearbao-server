package com.fubao.dearbao.api.service.admin;

import com.fubao.dearbao.api.controller.admin.dto.response.AdminLoginResponse;
import com.fubao.dearbao.api.controller.admin.dto.response.GetEnquiryResponse;
import com.fubao.dearbao.api.controller.admin.dto.response.GetMissionResponse;
import com.fubao.dearbao.api.service.admin.dto.AdminLoginDto;
import com.fubao.dearbao.domain.enquiry.Enquiry;
import com.fubao.dearbao.domain.enquiry.EnquiryRepository;
import com.fubao.dearbao.domain.member.IdLogin;
import com.fubao.dearbao.domain.member.IdLoginRepository;
import com.fubao.dearbao.domain.mission.MissionRepository;
import com.fubao.dearbao.domain.mission.entity.Mission;
import com.fubao.dearbao.global.common.exception.CustomException;
import com.fubao.dearbao.global.common.exception.ResponseCode;
import com.fubao.dearbao.global.config.security.jwt.JwtTokenProvider;
import com.fubao.dearbao.global.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final PasswordEncoder passwordEncoder;
    private final MissionRepository missionRepository;
    private final IdLoginRepository idLoginRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EnquiryRepository enquiryRepository;
    private final DateUtil dateUtil;

    @Override
    public AdminLoginResponse adminLogin(AdminLoginDto service) {
        IdLogin idLogin = idLoginRepository.findByIdentification(service.getId())
            .orElseThrow(() -> new CustomException(ResponseCode.INVALID_LOGIN));
        if (!checkPassword(service.getPassword(), idLogin.getPassword())) {
            throw new CustomException(ResponseCode.INVALID_LOGIN);
        }
        String accessToken = jwtTokenProvider.createAdminToken(
            idLogin.getMember().getId().toString());
        return AdminLoginResponse.of(accessToken);
    }

    @Override
    public List<GetEnquiryResponse> getEnquiryList() {
        List<Enquiry> enquiries = enquiryRepository.findAll();
        return enquiries.stream().map(
            enquiry -> GetEnquiryResponse.builder()
                .title(enquiry.getTitle())
                .name(enquiry.getMember().getName())
                .date(dateUtil.toResponseDateTimeFormat(enquiry.getCreatedAt()))
                .email(enquiry.getEmail())
                .content(enquiry.getContent())
                .build()
        ).toList();
    }

    @Override
    public List<GetMissionResponse> getMissionList() {
        List<Mission> missions = missionRepository.findAll();
        return missions.stream().map(
            mission -> GetMissionResponse.builder()
                .constructor(mission.getConstructor())
                .message(mission.getAnswer())
                .openStatus(mission.getState().getName())
                .content(mission.getContent())
                .date(isMissionOpenAt(mission))
                .build()
        ).toList();
    }

    private String isMissionOpenAt(Mission mission) {
        if (mission.getOpenAt() == null) {
            return null;
        }
        return dateUtil.toResponseDateFormat(mission.getOpenAt());
    }

    public boolean checkPassword(String password1, String password2) {
        return passwordEncoder.matches(password1, password2);
    }
}
