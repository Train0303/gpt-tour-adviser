package com.gptTour.backEnd.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginRequest {
    private String id;
    private String password;
}
