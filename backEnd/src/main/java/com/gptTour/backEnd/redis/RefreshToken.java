//package com.gptTour.backEnd.redis;
//
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.redis.core.RedisHash;
//
//@RedisHash(value = "refreshToken", timeToLive = 60L)
////24*14*3600
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//public class RefreshToken {
//
//    @Id
//    private String refreshToken;
//    private String email;
//    private String role;
//
//    @Builder
//    public RefreshToken(final String token, final String email, final String role){
//        this.refreshToken = token;
//        this.email = email;
//        this.role = role;
//    }
//
//}
