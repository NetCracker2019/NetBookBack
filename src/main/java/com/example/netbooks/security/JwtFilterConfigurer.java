package com.example.netbooks.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  private JwtProvider jwtProvider;

  @Autowired
  public JwtFilterConfigurer(JwtProvider jwtProvider) {
    this.jwtProvider = jwtProvider;
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    JwtFilter customFilter = new JwtFilter(jwtProvider);
    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
  }

}