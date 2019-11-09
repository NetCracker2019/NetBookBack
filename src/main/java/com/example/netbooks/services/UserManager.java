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
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
			Map<Object, Object> response = new HashMap<>();
            response.put("token", jwtProvider.createToken(user.getLogin(), user.getRole()));
            response.put("msg", "Successful login");
			return ResponseEntity.ok(response);
		} catch (AuthenticationException e) {
			throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	public ResponseEntity<Map> signup(User user) {
		if (userRepository.findByLogin(user.getLogin()) == null &&
				userRepository.findByEmail(user.getEmail()) == null) {
			logger.info("Complete Registration!" + user.getLogin() + " " + user.getPassword());
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setUserId(userRepository.getAllUsers().size() + 1);//rewrite after add db
			userRepository.save(user);
			VerificationToken verificationToken = new VerificationToken(user.getUserId());
			verificationTokenManager.saveToken(verificationToken);
			String message = "To verification your account, please click here : "
					+"https://netbooksnice.herokuapp.com/verification-account?token="+verificationToken.getVerificationToken();        
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
			User user = userRepository.findByUserId(token.getUserId());
			user.setActivity(true);
			logger.info("successRegister!" + verificationToken);
			logger.info("user" + user.getLogin());
			verificationTokenManager.removeVerificationToken(verificationToken);
			// TODO del addled tokens
			Map<Object, Object> response = new HashMap<>();
            response.put("msg", "Successful account verification");
            return ResponseEntity.ok(response);
			//return new ResponseEntity<>(HttpStatus.OK);
		}
		else {
			logger.info("Fail Register!" + verificationToken);
			throw new CustomException("Page not found", HttpStatus.NOT_FOUND);
		}   

	}

	public User getUserByMail(User user) {
		return userRepository.findByEmail(user.getEmail());
	}

	public User getUserByMailAndRole(String mail, String role) {
		return userRepository.findByEmailAndRole(mail, role);
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

	public LinkedList<User> getAllUsers() {
		return userRepository.getAllUsers();
	}

	public boolean saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}



}