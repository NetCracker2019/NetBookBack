package com.example.netbooks.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.netbooks.controllers.AuthenticationController;
import com.example.netbooks.exceptions.CustomException;
import org.springframework.web.cors.CorsUtils;

public class JwtFilter extends OncePerRequestFilter {

    private final Logger logger = LogManager.getLogger(JwtFilter.class);
    private JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        attemptAuthentication(httpServletRequest, httpServletResponse, filterChain);
        String token = jwtProvider.resolveToken(httpServletRequest);
        try {
            logger.debug("bearer {}", token);
            if (token != null && jwtProvider.validateToken(token)) {
                Authentication auth = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (CustomException ex) {
            logger.debug("next layer {}", ex.getMessage());
            throw ex;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    protected JwtProvider attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (CorsUtils.isPreFlightRequest(httpServletRequest)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return new JwtProvider();
        } else {
            return null;
        }
    }

}
