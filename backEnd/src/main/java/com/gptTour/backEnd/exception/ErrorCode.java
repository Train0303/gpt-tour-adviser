package com.gptTour.backEnd.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ADMIN_TOKEN(HttpStatus.BAD_REQUEST, "관리자 암호가 일치하지않습니다"),
    SAME_EMAIL(HttpStatus.BAD_REQUEST, "동일한 아이디가 존재합니다."),
    EXIST_ACCOUNT(HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다."),
    FAIL_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다."),
    NO_USER(HttpStatus.BAD_REQUEST, "없는 사용자입니다."),
    NO_LOGIN(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다"),
    NO_ADMIN(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다"),
    LOGIN_TOKEN_DETECTED(HttpStatus.BAD_REQUEST, "로그인 과정에 토큰이 검출되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    NO_REDIS_TOKEN(HttpStatus.BAD_REQUEST, "메모리에 없는 토큰입니다."),
    NO_EQUAL_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 일치하지 않습니다."),
    LOGOUT_INVALID_TOKEN(HttpStatus.UNAUTHORIZED , "로그아웃 : 유효하지 않은 토큰입니다.");


    private HttpStatus httpStatus;
    private String message;
}
