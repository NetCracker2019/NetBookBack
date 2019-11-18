package com.example.netbooks.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.netbooks.controllers.AuthenticationController;
import com.example.netbooks.dao.implementations.UserRepository;
import com.example.netbooks.exceptions.CustomException;
import com.example.netbooks.models.Role;
import com.example.netbooks.models.User;
import com.example.netbooks.models.VerificationToken;
import com.example.netbooks.security.JwtProvider;
import java.util.UUID;

@Service
public class UserManager {
	@Autowired
	UserRepository userRepository;

	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void removeUserById(long id) {
		userRepository.removeUserById(id);
	}
	
	public void updateUser(User user) {
		userRepository.updateUser(user);
	}
        
        public void updateUserById(User user, Long id) {
		userRepository.updateUserById(user,id);
	}
	
	public void saveUser(User user) {
		userRepository.save(user);
	}

	public void activateUser(long id) {
		userRepository.activateUser(id);
	}

	public void deActivateUser(long id) {
		userRepository.deActivateUser(id);
	}

	public User getUserByMail(String mail) {
		return userRepository.findByEmail(mail);
	}

	public User getUserById(long id) {
		return userRepository.findByUserId(id);
	}

	public User getUserByLogin(String login) {
		return userRepository.findByLogin(login);
	}

	public Iterable<User> getAllUsers() {
		return userRepository.getAllUsers();
	}

	public void setMinRefreshDate(String login, Date date) {
		userRepository.setMinRefreshDate(login, date);

	}

}