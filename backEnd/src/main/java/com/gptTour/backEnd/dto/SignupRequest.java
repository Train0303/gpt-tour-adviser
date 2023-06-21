package com.gptTour.backEnd.dto;

import com.gptTour.backEnd.entity.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@NoArgsConstructor //파라미터가 없는 기본 생성자를 생성
@Getter
public class SignupRequest {

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "닉네임은 특수문자를 포함하지 않은 2~10자리여야 합니다.")
    private String name;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$"
            , message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    private String password;

//    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "유효한 전화번호 형식이 아닙니다.")
//    private String phoneNumber;

    //    private RoleEnum role;

    /* DTO -> Entity */
    public Account toEntiy() {
        return Account.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();
    }
}
