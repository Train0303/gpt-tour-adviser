package com.gptTour.backEnd.service;

import com.gptTour.backEnd.dto.LoginRequest;
import com.gptTour.backEnd.dto.SignupRequest;
import com.gptTour.backEnd.entity.Account;
import com.gptTour.backEnd.exception.CustomException;
import com.gptTour.backEnd.exception.ErrorCode;
import com.gptTour.backEnd.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 중복 검사 체크
    public void validateDuplicateAccount(String id) {
        if(accountRepository.findById(id).isPresent()){
            throw new CustomException(ErrorCode.SAME_EMAIL);
        }
    }

    // 유저 정보가져오기(selectOne) + 로그인
    public Account login(LoginRequest loginRequest) {
        Account account = accountRepository.findById(loginRequest.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_USER));
//        if(!bCryptPasswordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
//            throw new CustomException(ErrorCode.FAIL_PASSWORD);
//        }

        return account;
    }

    // 회원 가입
    @Transactional
    public Long save(SignupRequest requestDto){
        validateDuplicateAccount(requestDto.getEmail());
        Account account = requestDto.toEntiy();
        account.hashPassword(bCryptPasswordEncoder);

        return accountRepository.save(account);
    }
}
