package com.gptTour.backEnd.security;

import com.gptTour.backEnd.entity.AccountRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityAuthenticationFilter securityAuthenticationFilter() {
        return new SecurityAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable()
                .authorizeRequests()
                .antMatchers("/", "/user/**").permitAll()
                .antMatchers("/admin/**").hasAnyAuthority(AccountRole.ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .formLogin().disable()
                .logout()
                .logoutUrl("/user/logout")
//                .defaultSuccessUrl("/")
//                .failureUrl("/user/login")
//                .loginPage("/login")
////                .loginProcessingUrl()
//                .and()
//                .logout()
//                .logoutUrl("/user/logout")
                .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and()
                .httpBasic()
                .disable()
                /** 세션 사용하지 않음 **/
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .maximumSessions(1)
//                .expiredUrl("/user/login")
//                .and()
//                .invalidSessionUrl("/user/login");
//                .and()
//            .exceptionHandling()
//                .accessDeniedHandler(accessDeniedHandler())
//                .authenticationEntryPoint(authenticationEntryPoint())
//                .and()
                .and()
                .addFilterBefore(securityAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
//    }
    }
}
