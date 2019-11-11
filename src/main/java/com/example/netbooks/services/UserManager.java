package com.example.netbooks.services;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.netbooks.controllers.AuthenticationController;
import com.example.netbooks.dao.UserRepository;
import com.example.netbooks.exceptions.CustomException;
import com.example.netbooks.models.Role;
import com.example.netbooks.models.User;
import com.example.netbooks.models.VerificationToken;
import com.example.netbooks.security.JwtProvider;

@Service
public class UserManager {
	private final Logger logger = LogManager.getLogger(AuthenticationController.class);
	@Autowired
	UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private VerificationTokenManager verificationTokenManager;
	
	public void interrupt(String login){
		User user = userRepository.findByLogin(login);
		user.setMinRefreshDate(null);
	}

	public ResponseEntity<Map> signin(User user) {
		try {
			//user.setMinRefreshDate(null);// if only one active session is allowed
			logger.info("Try to login " + user.getLogin() + " ---- " + user.getPassword());
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
			
			Map<Object, Object> response = new HashMap<>();
            response.put("token", jwtProvider.createToken(user.getLogin(), user.getRole()));
            response.put("msg", "Successful login");
			return ResponseEntity.ok(response);
		} catch (AuthenticationException e) {
			throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	public ResponseEntity<Map> signup(User user, Role role) {
		if (userRepository.findByLogin(user.getLogin()) == null &&
				userRepository.findByEmail(user.getEmail()) == null) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setRole(role);
			userRepository.save(user);
			
			VerificationToken verificationToken = new VerificationToken(
					userRepository.findByLogin(user.getLogin()).getUserId());
			verificationTokenManager.saveToken(verificationToken);
			
			String message = "To verification your account, please click here : "
					+"https://netbooksnice.herokuapp.com/verification-account?token="
					+verificationToken.getVerificationToken();        
			//emailSender.sendMessage(user.getEmail(), "Complete Registration!", message);
			logger.info("Complete Registration!" + user.getLogin() + message);
			
			Map<Object, Object> response = new HashMap<>();
            response.put("msg", "Successful registration");
            return ResponseEntity.ok(response);
		} else {
			throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	public ResponseEntity<Map> confirmUserAccount(String verificationToken) {
		VerificationToken token = verificationTokenManager.findVerificationToken(verificationToken);
		if(token != null) {
			userRepository.activateUser(token.getUserId());
			logger.info("successRegister!" + verificationToken);
			verificationTokenManager.removeVerificationToken(verificationToken);
			// TODO del addled tokens
			
			Map<Object, Object> response = new HashMap<>();
            response.put("msg", "Successful account verification");
            return ResponseEntity.ok(response);
		}
		else {
			logger.info("Fail Register!" + verificationToken);
			throw new CustomException("Invalid token", HttpStatus.NOT_FOUND);
		}   

	}
	
	public ResponseEntity<Map> recoveryPass(String verificationToken, String newPass){
		VerificationToken token = verificationTokenManager.findVerificationToken(verificationToken);
		if(token != null) {
			User user = userRepository.findByUserId(token.getUserId());
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setMinRefreshDate(null);
			userRepository.updateUser(user);
			verificationTokenManager.removeVerificationToken(verificationToken);
			// TODO del addled tokens
			Map<Object, Object> response = new HashMap<>();
            response.put("msg", "successful password recovery");
            return ResponseEntity.ok(response);
		}
		else {
			throw new CustomException("Invalid recovery password link", HttpStatus.NOT_FOUND);
		}  
	}
	
	public ResponseEntity<Map> recoveryPassRequest(User user){
		VerificationToken verificationToken = new VerificationToken(
				userRepository.findByEmail(user.getEmail()).getUserId());
		verificationTokenManager.saveToken(verificationToken);
		
		String message = "To recovery your password, please click here : "
				+"https://netbooksnice.herokuapp.com/recovery-password?token="
				+verificationToken.getVerificationToken();        
		//emailSender.sendMessage(user.getEmail(), "Recovery your password", message);
		logger.info("success recovery" + verificationToken.getVerificationToken());
		Map<Object, Object> response = new HashMap<>();
        response.put("msg", "Password recovery letter has been sent successfully");
        return ResponseEntity.ok(response);
	}

	public User getUserByMail(User user) {
		return userRepository.findByEmail(user.getEmail());
	}
	
	public void removeUserById(long id) {
		userRepository.removeUserById(id);
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

	public User getUserByUsername(String username) {
		return userRepository.findByLogin(username);
	}

	public Iterable<User> getAllUsers() {
		return userRepository.getAllUsers();
	}




}