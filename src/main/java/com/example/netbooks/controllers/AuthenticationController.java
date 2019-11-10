package com.example.netbooks.controllers;

import com.example.netbooks.models.Role;
import com.example.netbooks.models.User;
import com.example.netbooks.services.UserManager;
import com.example.netbooks.services.VerificationTokenManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

}