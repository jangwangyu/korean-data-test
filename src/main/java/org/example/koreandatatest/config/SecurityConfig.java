package org.example.koreandatatest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 스프링 설정을 위한 클래스임을 명시
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // 모든 요청에 대해 접근을 허용
            .logout(logout -> logout.logoutSuccessUrl("/")) // 로그아웃 성공 시 홈 페이지로 이동
            .build();
  }

}
