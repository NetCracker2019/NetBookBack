package com.example.netbooks.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.netbooks.controllers.AuthenticationController;
import com.example.netbooks.dao.UserRepository;
import com.example.netbooks.exceptions.CustomException;
import com.example.netbooks.models.Role;
import com.example.netbooks.models.User;
import com.example.netbooks.services.UserManager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {
	private final Logger logger = LogManager.getLogger(JwtProvider.class);
	@Value("${jwt.token.secret}")
	private String secretKey;

	@Value("${jwt.token.expired}")
	private long validityTime;
	
	@Value("${jwt.token.secondPause}")
	private long secondPause;

	@Autowired
	private JwtUserDetails JwtUserDetails;
	
	@Autowired
	private UserManager userManager;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public String createToken(String login, Role role) {
		User user = userManager.getUserByLogin(login);
		Claims claims = Jwts.claims().setSubject(login);
		claims.put("role", role);

		Date now = new Date();
		Date validity = new Date(now.getTime() + validityTime);
		if(user.getMinRefreshDate() == null) {
			userManager.setMinRefreshDate(login, new Date(now.getTime() - secondPause));
		}
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(validity)
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}

	public Authentication getAuthentication(String token) {
		UserDetails userDetails = JwtUserDetails.loadUserByUsername(getUsername(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer_")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public boolean validateToken(String token) {
		try {
			Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
			final User user = userManager.getUserByLogin(claims.getSubject());
			if(claims.getIssuedAt().compareTo(user.getMinRefreshDate()) == -1) {
				logger.debug("fal {}", user.getLogin());
				throw new Exception();
				//return false;
			}
			return true;
		} catch (Exception e) {
			logger.debug("valid error ");
			throw new CustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}