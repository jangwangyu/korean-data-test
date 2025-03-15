package org.example.koreandatatest.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.example.koreandatatest.DTO.security.GithubUser;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 스프링 설정을 위한 클래스임을 명시
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(auth ->
            auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations()) .permitAll()// 정적 리소스에 대한 요청은 모두 허용
                .requestMatchers(
                    HttpMethod.GET,
                    "/",
                    "table-schema",
                    "table-schema/export"
                ).permitAll()
                .anyRequest().authenticated())
        .oauth2Login(withDefaults())
        .logout(logout -> logout.logoutSuccessUrl("/")) // 로그아웃 성공 시 홈 페이지로 이동
        .build();
  }

  @Bean
  public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
    final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    return userRequest -> GithubUser.from(delegate.loadUser(userRequest).getAttributes());
  }
}
