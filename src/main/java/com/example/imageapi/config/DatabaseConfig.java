package com.example.imageapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

  @Value("${spring.datasource.url}")
  private String url;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.driver-class-name}")
  private String driver;

  private final Environment env;

  public DatabaseConfig(@Autowired Environment env) {
    this.env = env;
  }

  @Bean
  public DataSource mySqlDataSource() {
    final String password = env.getProperty("DATABASE_PASSWORD");
    return DataSourceBuilder.create()
        .url(url)
        .username(username)
        .password(password)
        .driverClassName(driver)
        .build();
  }
}
