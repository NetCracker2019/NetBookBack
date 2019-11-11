package com.example.netbooks.controllers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.netbooks.dao.UserRepository;
import com.example.netbooks.dao.VerificationTokenRepository;
import com.example.netbooks.exceptions.CustomException;
import com.example.netbooks.models.VerificationToken;
import com.example.netbooks.security.JwtProvider;
import com.example.netbooks.services.EmailSender;
import com.example.netbooks.services.UserManager;
import com.example.netbooks.services.VerificationTokenManager;
import com.example.netbooks.models.Role;
import com.example.netbooks.models.User;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class AuthenticationController {

	private final Logger logger = LogManager.getLogger(AuthenticationController.class);

	@Autowired
	private UserManager userManager ;

	@Autowired
	private VerificationTokenManager verificationTokenManager;

	
	@RequestMapping(value="/interrupt", method = RequestMethod.GET)
	public void interruptr(@RequestParam("login")String login){
		userManager.interrupt(login);
	}
	
	@RequestMapping(value="/register", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	public ResponseEntity<Map> register(@RequestBody User user){
		return userManager.signup(user, Role.ROLE_CLIENT);
	}

	@RequestMapping(value="/verification-account", method= {RequestMethod.GET})
	public ResponseEntity<Map> confirmUserAccount(@RequestParam("token")String verificationToken){
		return userManager.confirmUserAccount(verificationToken);        
	}

	@RequestMapping(value="/signin", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	public ResponseEntity<Map> signin(@RequestBody User user){
		return userManager.signin(user);
	}

	@RequestMapping(value = "/Users", method = {RequestMethod.GET, RequestMethod.POST})
	public Iterable<User> getAllUsers() {
		return userManager.getAllUsers();
	}
	@RequestMapping(value = "/rmuser", method = {RequestMethod.GET})
	public void removeUser(@RequestParam("id")long id) {
		userManager.removeUserById(id);
	}
	@RequestMapping(value="/recovery-pass-request", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	public ResponseEntity<Map> recoveryPassRequest(@RequestBody User user){
		return userManager.recoveryPassRequest(user);
	}
	@RequestMapping(value="/recovery-pass", method= {RequestMethod.GET})
	public ResponseEntity<Map> recoveryPass(@RequestParam("token")String token, @RequestParam("pass")String pass){
		return userManager.recoveryPass(token, pass);
	}
	

}