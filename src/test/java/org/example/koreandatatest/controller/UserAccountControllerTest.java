package org.example.koreandatatest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.example.koreandatatest.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("[Controller] 회원 컨트롤러 테스트")
@Import(SecurityConfig.class)
@WebMvcTest
public record UserAccountControllerTest(@Autowired MockMvc mvc) {

  @WithMockUser
  @DisplayName("[Get] 내 정보 페이지 -> 내 정보 뷰 (정상)")
  @Test
  void givenAuthenticatedUser_whenRequesting_thenShowsMyAccountView() throws Exception {
    // Given

    // When&Then
    mvc.perform(get("/my-account"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
        .andExpect(model().attributeExists("nickname"))
        .andExpect(model().attributeExists("email"))
        .andExpect(view().name("my-account"));
  }
}
