package com.fubao.dearbao.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    //success
    OK("success", HttpStatus.OK, "요청에 성공하였습니다."),
    //member
    NOT_GET_KAKAO_INFO("MEM-ERR-001", HttpStatus.BAD_REQUEST, "카카오 정보를 가져오는데 실패했습니다."),
    NOT_FOUND_MEMBER("MEM-ERR-002", HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    INVALID_NICKNAME("MEM-ERR-003", HttpStatus.BAD_REQUEST, "유효하지 않은 닉네임입니다."),
    EXIST_NICKNAME("MEM-ERR-004", HttpStatus.CONFLICT, "이미 사용중인 닉네임입니다."),
    //mission
    NOT_FOUND_MISSION("MISSION-ERR-001", HttpStatus.NOT_FOUND, "미션을 찾을 수 없습니다."),
    ALREADY_ACTIVE_DAILY_MISSION("MISSION-ERR-002", HttpStatus.CONFLICT, "이미 미션을 수행하였습니다."),
    INVALID_MISSION_CONTENT("MISSION-ERR-003", HttpStatus.BAD_REQUEST, "미션 내용은 400자를 넘길 수 없습니다."),

    NOT_FOUND_VALID_MISSION_FOR_SET_DAILY_MISSION("MISSION-ERR-999", HttpStatus.NOT_FOUND,
        "데일리 미션을 생성할 수 있는 미션이 없습니다."),

    //GLOBAL
    BAD_REQUEST("GLB-ERR-001", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    METHOD_NOT_ALLOWED("GLB-ERR-002", HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR("GLB-ERR-003", HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
    //auth
    UNAUTHORIZED("AUTH-ERR-001", HttpStatus.UNAUTHORIZED, "접근 권한이 없는 유저입니다."),
    INVALID_TOKEN("AUTH-ERR-002", HttpStatus.UNAUTHORIZED, "유효한 토큰이 아닙니다."),
    EXPIRED_TOKEN("AUTH-ERR-003", HttpStatus.UNAUTHORIZED, "만료된 토근입니다."),
    UNSUPPORTED_TOKEN("AUTH-ERR-004", HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰입니다."),
    //test
    TEST("TEST-ERR-001", HttpStatus.BAD_REQUEST, "테스트입니다"),
    //admin
    INVALID_LOGIN("ADMIN-001", HttpStatus.BAD_REQUEST, "유효하지 않은 아이디 비밀번호입니다."),
    NOT_DELETE_MISSION("ADMIN-002", HttpStatus.BAD_REQUEST, "지울 수 없는 미션입니다."),
    //enquiry
    ENQUIRY_OVER_CONTENT_LENGTH("ENQUIRY-001", HttpStatus.BAD_REQUEST, "글자수는 최대 300글자 입니다.");
    private final String code;
    private final HttpStatus status;
    private final String message;


    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
            .filter(Predicate.not(String::isBlank))
            .orElse(this.getMessage());
    }
}
