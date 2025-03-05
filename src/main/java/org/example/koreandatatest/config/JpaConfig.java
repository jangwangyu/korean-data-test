package org.example.koreandatatest.config;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // JPA Auditing 활성화
@Configuration // JPA 설정을 위한 클래스임을 명시
public class JpaConfig {

  @Bean
  public AuditorAware<String> auditorAware() {
    return () -> Optional.of("jang");
  }
}
