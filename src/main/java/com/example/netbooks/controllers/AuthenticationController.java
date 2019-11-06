package com.example.netbooks.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.netbooks.models.User;
@CrossOrigin(origins = "https://netbooksnice.herokuapp.com")
@RestController
public class AuthenticationController {
	
	private final Logger logger = LogManager.getLogger(AuthenticationController.class);
	
	@Autowired
	EmailSender emailSender;
	
	@Autowired
    private UserManager userManager ;

    @Autowired
    private VerificationTokenRepository verificationTokenRepo;

    

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public ResponseEntity registerUser(@RequestBody Map<String, String> body){
    	logger.info("NEW registration" + body);
    	String messageResponse;
    	User existingUserByEmail = userManager.GetUserByMail(body.get("username"));
    	User existingUserByUsername = userManager.GetUserByUsername(body.get("username"));
    	/*"username" == email*/
    	
    	if(existingUserByEmail != null || existingUserByUsername != null){
    		messageResponse = "user already exist";
    	}
    	else{
    		User user = new User(body);
            userManager.SaveUser(user);//into db

            VerificationToken verificationToken = new VerificationToken(user);

            verificationTokenRepo.save(verificationToken);
            
            String message = "To verification your account, please click here : "
                    +"https://netbookstest.herokuapp.com/verification-account?token="+verificationToken.getVerificationToken();
            
            emailSender.sendMessage(user.getEmail(), "Complete Registration!", message);
             
            logger.info("Complete Registration!" + user.getLogin() + message);
            
            messageResponse = "oki";
        }
        return ResponseEntity.ok(messageResponse);
    }

    @ResponseBody
    @RequestMapping(value="/verification-account", method= {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(@RequestParam("token")String verificationToken){
    	VerificationToken token = verificationTokenRepo.findByVerificationToken(verificationToken);
    	String messageResponse;
        if(token != null) {
            //User user = userRepository.findByEmail(token.getUser().getEmail());
            //userRepository.save(user);
        	logger.info("Fail Register!" + verificationToken);
        	messageResponse = "invalidToken";
        }
        else {
        	
        	 logger.info("successRegister!" + verificationToken);
        	 messageResponse = "successRegister";
        }
                
        return messageResponse;

    }
    
}