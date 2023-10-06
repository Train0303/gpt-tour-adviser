package com.gptTour.backEnd.dto;

import com.gptTour.backEnd.entity.AccountRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenUserDto {
    private String email;
    private AccountRole role;

    @Builder
    public TokenUserDto(String email, AccountRole role){
        this.email = email;
        this.role = role;
    }
}
