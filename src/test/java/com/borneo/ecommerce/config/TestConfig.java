package com.borneo.ecommerce.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Test configuration for Spring Boot tests. This configuration is used specifically for testing and
 * provides beans that override the production configurations.
 */
@TestConfiguration
public class TestConfig {

  /**
   * Provides a PasswordEncoder bean for testing. Uses BCrypt with a reduced strength for faster
   * test execution.
   *
   * @return PasswordEncoder bean
   */
  @Bean
  @Primary
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(4); // Lower strength for faster tests
  }
}
