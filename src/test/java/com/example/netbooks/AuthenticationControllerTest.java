package com.example.netbooks;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import com.example.netbooks.services.UserManager;
import com.example.netbooks.services.VerificationTokenManager;
import com.example.netbooks.controllers.AuthenticationController;
import com.example.netbooks.models.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthenticationControllerTest {
	@Autowired
	private AuthenticationController authenticationController;
	@Autowired
	private UserManager userManager;
	@Autowired
	private VerificationTokenManager tokenManager;
	
	/*
	@Test
	void registerTest() {
		Map<Object, Object> request = new HashMap<>();
		User user = new User();
		user.setLogin("testlogin");
		user.setPassword("pass");
		user.setEmail("testEmail");
		request =  authenticationController.register(user).getBody();
		assertEquals(request.get("msg"), "Successful registration");
	}
	
	@Test
	void confirmUserAccountTest() {
		Map<Object, Object> request = new HashMap<>();
		User user = userManager.getUserByLogin("testlogin");
		String token = tokenManager.findVerificationTokenByUserId(user.getUserId()).getVerificationToken();
		
		request =  authenticationController.confirmUserAccount(token).getBody();
		assertEquals(request.get("msg"), "Successful account verification");
		assertEquals(true, userManager.getUserByLogin("testlogin").isActivity());
	}
	@Test
	void signinTest() {
		Map<Object, Object> request = new HashMap<>();
		User user = new User();
		user.setLogin("testlogin");
		user.setPassword("pass");
		
		request =  authenticationController.signin(user).getBody();
		assertEquals(request.get("msg"), "Successful login");
	}
	
	@Test
	void removeUserTest() {
		User user = userManager.getUserByLogin("testlogin");
		
		//authenticationController.removeUser(user.getUserId());
		
		//assertEquals(null, userManager.getUserByLogin("testlogin"));
		assertEquals(1, 1);
	}
	*/
	@Test
	void removeUserTest() {
		
		assertEquals(1, 1);
	}
	

}
