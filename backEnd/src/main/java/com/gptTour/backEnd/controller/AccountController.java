package com.gptTour.backEnd.controller;

import com.gptTour.backEnd.dto.LoginRequest;
import com.gptTour.backEnd.dto.ResponseDto;
import com.gptTour.backEnd.dto.SignupRequest;
import com.gptTour.backEnd.entity.Account;
import com.gptTour.backEnd.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@RequiredArgsConstructor
@RestController("/api")
public class AccountController {

    private final AccountService accountService;
    final static String USER_SESSION_ID = "userId";

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public ResponseDto getUser(@AuthenticationPrincipal Account user) {

        return new ResponseDto("success", "로그인페이지입니다.", user);
//        if(userId == null) {
//            System.out.println("로그인 하지 않음");
//            return new ResponseDto("success", "로그인 되어 있지 않습니다.", null);
//        } else {
//            System.out.println("로그인 유저 ID : " + userId);
//            return new ResponseDto("success", "로그인 되어 있습니다.", userId);
//        }
    }

    @PostMapping("/login")
    public ResponseDto login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {

//        Account account = accountService.login(loginRequest);

//        httpServletRequest.getSession().invalidate(); // 세션파기(있다면)
//        HttpSession session = httpServletRequest.getSession(true);  // Session이 없으면 생성
//        session.setAttribute(USER_SESSION_ID, account.getEmail());
//        session.setMaxInactiveInterval(1800); // session이 30분동안 유지
//        System.out.println(session.getAttribute(USER_SESSION_ID));
        var auth = (Authentication) httpServletRequest.getUserPrincipal();
        var user = (User) auth.getPrincipal();
//        System.out.println("User {} logged in." + user.getUsername());


        return new ResponseDto("success", "로그인 성공", user.getUsername());
    }

    @GetMapping("/signup")
    public ResponseDto signUpPage() {
        return new ResponseDto("success", "요청이 처리되었습니다.", null);
    }

    @PostMapping("/signup")
    public ResponseDto signUp(@RequestBody SignupRequest signupRequest, HttpServletRequest httpServletRequest) {
        System.out.println(signupRequest.getEmail());
        long id = accountService.save(signupRequest);

        return new ResponseDto("success", "회원가입 성공", id);
    }

    @GetMapping("/security_test")
    public ResponseDto security_test() {
        return new ResponseDto("success", "시큐리티 인증 성공", "");
    }
}
