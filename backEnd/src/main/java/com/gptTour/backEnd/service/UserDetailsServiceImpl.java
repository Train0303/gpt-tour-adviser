package com.gptTour.backEnd.service;

import com.gptTour.backEnd.entity.Account;
import com.gptTour.backEnd.exception.CustomException;
import com.gptTour.backEnd.exception.ErrorCode;
import com.gptTour.backEnd.repository.AccountRepository;
import com.gptTour.backEnd.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("userID: " + email);
        Optional<Account> account = accountRepository.findById(email);
        if(account.isPresent()) {
            return new UserDetailsImpl(account.get());
        } else {
            throw new CustomException(ErrorCode.NO_USER);
        }
    }
}
