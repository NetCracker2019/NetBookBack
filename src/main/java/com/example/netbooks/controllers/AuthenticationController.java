package com.example.netbooks.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.netbooks.dao.UserRepository;
import com.example.netbooks.dao.VerificationTokenRepository;
import com.example.netbooks.services.EmailSender;
import com.example.netbooks.services.User;
import com.example.netbooks.services.VerificationToken;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class AuthenticationController {
	
	private final Logger logger = LogManager.getLogger(AuthenticationController.class);
	
	@Autowired
	EmailSender emailSender;
	
	@Autowired
    private UserRepository userRepository ;

    @Autowired
    private VerificationTokenRepository verificationTokenRepo;

    

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public ResponseEntity registerUser(@RequestBody Map<String, String> body)
    {
    	logger.info("NEW registration" + body);
    	String messageResponse;
    	boolean isExistingUser = userRepository.findByEmail(body.get("username"));
    	/*"username" == email*/
    	
    	if(isExistingUser){
    		messageResponse = "exist";
    	}
    	else{
    		User user = new User(body);
            userRepository.save(user);//into db

            VerificationToken verificationToken = new VerificationToken(user);

            verificationTokenRepo.save(verificationToken);
            
            String message = "To verification your account, please click here : "
                    +"https://netbookstest.herokuapp.com/verification-account?token="+verificationToken.getVerificationToken();
            
            emailSender.sendMessage(user.getEmail(), "Complete Registration!", message);
             
            logger.info("Complete Registration!" + user.getLogin() + message);
            
            messageResponse = "ok";
        }
        return ResponseEntity.ok(messageResponse);
    }

    @RequestMapping(value="/verification-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity confirmUserAccount(@RequestParam("token")String verificationToken)
    {
    	logger.info("sdfsdfsfdfg");
    	VerificationToken token = verificationTokenRepo.findByVerificationToken(verificationToken);
    	String messageResponse;
        if(token != null)
        {
            //User user = userRepository.findByEmail(token.getUser().getEmail());
            //userRepository.save(user);
        	logger.info("Fail Register!" + verificationToken);
        	messageResponse = "invalidToken";
        }
        else
        {
        	
        	 logger.info("successRegister!" + verificationToken);
        	messageResponse = "successRegister";
        }
        
        return ResponseEntity.ok(messageResponse);
        
    }
    
}