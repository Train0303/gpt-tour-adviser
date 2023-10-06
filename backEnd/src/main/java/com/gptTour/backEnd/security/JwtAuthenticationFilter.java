package com.gptTour.backEnd.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        logger.info(request.getRequestURL().toString());
        // 헤더에서 JWT 를 받아옵니다.
        String token = jwtTokenProvider.resolveToken(request);

        // 유효한 토큰인지 확인합니다. 유효성검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // (추가) Redis에 해당 accessToken logout 여부 확인
            String isLogout = redisTemplate.opsForValue().get(token);
            // accessToken이 블랙리스트로 등록되어 있지않다면
            if(ObjectUtils.isEmpty(isLogout)) {
                // 토큰 인증과정을 거친 결과를 authentication이라는 이름으로 저장해줌.
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                // SecurityContext 에 Authentication 객체를 저장합니다.
                // token이 인증된 상태를 유지하도록 context(맥락)을 유지해줌
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        //UsernamePasswordAuthenticationFilter로 이동
        chain.doFilter(request, response);
    }
}
