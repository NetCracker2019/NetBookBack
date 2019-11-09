package com.example.netbooks.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.netbooks.controllers.AuthenticationController;
import com.example.netbooks.dao.UserRepository;
import com.example.netbooks.models.User;

@Service
public class JwtUserDetails implements UserDetailsService {
	private final Logger logger = LogManager.getLogger(AuthenticationController.class);
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		final User user = userRepository.findByLogin(login);

		if (user == null) {
			throw new UsernameNotFoundException("User '" + login + "' not found");
		}

		return org.springframework.security.core.userdetails.User//
				.withUsername(user.getLogin())
				.password(user.getPassword())
				.authorities(user.getRole())
				.accountLocked(!user.isActivity())
				.credentialsExpired(false)
				.disabled(false)
				.build();
	}

}