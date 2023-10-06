package com.gptTour.backEnd.controller;

import com.gptTour.backEnd.dto.LoginRequest;
import com.gptTour.backEnd.dto.ResponseDto;
import com.gptTour.backEnd.dto.SignupRequest;
import com.gptTour.backEnd.exception.CustomException;
import com.gptTour.backEnd.exception.ErrorCode;
import com.gptTour.backEnd.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("")
    public String home() {
        return "home";
    }

    @PostMapping("/login")
    public ResponseDto loginMembers(@RequestBody LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {
        if(request.getHeader("Authorization") != null || request.getHeader("Refresh") != null) {
            throw new CustomException(ErrorCode.LOGIN_TOKEN_DETECTED);
        }
        Map<String, String> tokenSet = accountService.selectAccount(loginRequest.getId(), loginRequest.getPassword());
        response.setHeader("Authorization", "Bearer " + tokenSet.get("accessToken"));

        return new ResponseDto(200, "success", "로그인 성공", tokenSet);

    }

    @PostMapping("/signup")
    public ResponseDto signUp(@RequestBody SignupRequest signupRequest, HttpServletRequest httpServletRequest) {
        System.out.println(signupRequest.getEmail());
        long id = accountService.save(signupRequest);

        return new ResponseDto(200, "success", "회원가입 성공", id);
    }

    @PostMapping("/refresh")
    public ResponseDto genAccessToken(HttpServletRequest request){
        String refreshToken = request.getHeader("Refresh");
        if(refreshToken == null || !refreshToken.startsWith("Bearer "))
            throw new CustomException(ErrorCode.INVALID_TOKEN);

        refreshToken = refreshToken.split(" ")[1];
        String accessToken = accountService.reissueAccessToken(refreshToken);
        return new ResponseDto(200, "success", "토큰 정보가 갱신되었습니다.", accessToken);
    }

    @GetMapping("/logout")
    public ResponseDto logoutMembers(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        accountService.logout(accessToken);

        return new ResponseDto(200, "success", "로그아웃 되었습니다.", "");
    }

    @GetMapping("/security_test")
    public String security_test() {
        return "인가 받은 사용자 출입 허용됐습니다!ㅎㅎ";
    }
}
