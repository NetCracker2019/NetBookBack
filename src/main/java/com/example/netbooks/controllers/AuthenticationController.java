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
import com.example.netbooks.models.VerificationToken;
import com.example.netbooks.services.EmailSender;
import com.example.netbooks.services.UserManager;
import com.example.netbooks.services.VerificationTokenManager;
import com.example.netbooks.models.User;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class AuthenticationController {
	
	private final Logger logger = LogManager.getLogger(AuthenticationController.class);
	
	@Autowired
	EmailSender emailSender;
	
	@Autowired
    private UserManager userManager ;

    @Autowired
    private VerificationTokenManager verificationTokenManager;

    
    @RequestMapping(value="/register", method = RequestMethod.POST, headers = {"Content-type=application/json"})
    public ResponseEntity<String> registerUser(@RequestBody User user){
    	logger.info("NEW registration" + user.getLogin() + user.getEmail());
    	if( userManager.GetUserByMail(user.getEmail()) != null ||
    			userManager.GetUserByUsername(user.getLogin()) != null){
    		return new ResponseEntity<>(HttpStatus.CONFLICT);
    	}
    	else{
            userManager.SaveUser(user);
            VerificationToken verificationToken = new VerificationToken(user);
            verificationTokenManager.SaveToken(verificationToken);
            String message = "To verification your account, please click here : "
                    +"https://netbooksnice.herokuapp.com/verification-account?token="+verificationToken.getVerificationToken();        
            emailSender.sendMessage(user.getEmail(), "Complete Registration!", message);
            logger.info("Complete Registration!" + user.getLogin() + message);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @ResponseBody
    @RequestMapping(value="/verification-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> confirmUserAccount(@RequestParam("token")String verificationToken){
    	VerificationToken token = verificationTokenManager.FindVerificationToken(verificationToken);
        if(token != null) {
            User user = userManager.GetUserByMail(token.getUser().getEmail());
            userManager.SaveUser(user);
        	logger.info("Fail Register!" + verificationToken);
        	verificationTokenManager.RemoveVerificationToken(verificationToken);
        	return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        else {
        	 logger.info("successRegister!" + verificationToken);
        	 return new ResponseEntity<>(HttpStatus.OK);
        }               
    }
    
    @RequestMapping(value = "/Users", method = {RequestMethod.GET, RequestMethod.POST})
    public LinkedList<User> getAllUsers() {
        return userManager.GetAllUsers();
    }
    
    @RequestMapping(value = "/Tokens", method = {RequestMethod.GET, RequestMethod.POST})
    public LinkedList<VerificationToken> getAllVerificationTokens() {
        return verificationTokenManager.GetAllVerificationTokens();
    }
}