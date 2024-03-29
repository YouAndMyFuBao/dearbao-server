package com.fubao.dearbao.global.common.exception;

import com.fubao.dearbao.global.common.response.ResponseDto;

public class ErrorResponse extends ResponseDto {

    private ErrorResponse(ResponseCode errorCode) {
        super(false, errorCode.getCode(), errorCode.getMessage());
    }

    private ErrorResponse(ResponseCode errorCode, Exception e) {
        super(false, errorCode.getCode(), errorCode.getMessage(e));
    }

    private ErrorResponse(ResponseCode errorCode, String message) {
        super(false, errorCode.getCode(), errorCode.getMessage(message));
    }

    public static ErrorResponse of(ResponseCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse of(ResponseCode errorCode, Exception e) {
        return new ErrorResponse(errorCode, e);
    }

    public static ErrorResponse of(ResponseCode errorCode, String message) {
        return new ErrorResponse(errorCode, message);
    }
}