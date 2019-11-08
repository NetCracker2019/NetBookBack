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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.netbooks.controllers.AuthenticationController;
import com.example.netbooks.exceptions.CustomException;



public class JwtFilter extends OncePerRequestFilter {
	private final Logger logger = LogManager.getLogger(AuthenticationController.class);
	private JwtProvider jwtProvider;

	public JwtFilter(JwtProvider jwtProvider) {
		this.jwtProvider = jwtProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		String token = jwtProvider.resolveToken(httpServletRequest);
		try {
			logger.info("bearer22 " + token);
			if (token != null && jwtProvider.validateToken(token)) {
				Authentication auth = jwtProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (CustomException ex) {
			SecurityContextHolder.clearContext();
			httpServletResponse.sendError(ex.getHttpStatus().value(), ex.getMessage());
			return;
		}


		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}
}
