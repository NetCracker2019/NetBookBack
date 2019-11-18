package com.example.netbooks.controllers;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        
        @RequestMapping(value="/verification-admin", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	public ResponseEntity<Map> register(@RequestBody User user, @RequestParam("token")String verificationToken){
		return userManager.signupAdmin(user, Role.ROLE_ADMIN, verificationToken);
	}
        
        @RequestMapping(value="/send-admin-reg-mail", method = RequestMethod.POST)
	public ResponseEntity<Map> sendAdminRegMail(@RequestParam("mail")String mail){
		return userManager.sendAdminRegMail(mail);
	}
	@RequestMapping(value="/recovery-pass-request", method = RequestMethod.GET)
	public ResponseEntity<Map> recoveryPassRequest(@RequestParam("email")String email){
		return userManager.recoveryPassRequest(email);
	}
	@RequestMapping(value="/recovery-pass", method= {RequestMethod.GET})
	public ResponseEntity<Map> recoveryPass(@RequestParam("token")String token, @RequestParam("pass")String pass){
		return userManager.recoveryPass(token, pass);
	}
	

}
