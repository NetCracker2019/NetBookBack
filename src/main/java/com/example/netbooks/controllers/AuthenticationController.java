package com.example.netbooks.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
	
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
	public String firstPage() {
		return "qwer";
	}
	
	@Autowired
	EmailSender emailSender;
	
	@Autowired
    private UserRepository userRepository ;

    @Autowired
    private VerificationTokenRepository verificationTokenRepo;


    @RequestMapping(value="/register", method = RequestMethod.POST)
    public void registerUser( User user)
    {
    	User existingUser = new User();
    	/*
    	 * check if user already existing and create new user
    	 */
    	//else
        {
            user.save();//into db

            VerificationToken verificationToken = new VerificationToken(user);

            verificationTokenRepo.save(verificationToken);
            
            String message = "To verification your account, please click here : "
                    +"http://localhost:4200/verification-account?token="+verificationToken.getVerificationToken();
            
            emailSender.sendMessage(user.getEmail(), "Complete Registration!", message);
             
            logger.info("Complete Registration!" + user.getFirstName());
            
            //todo smth message for clien
        }

    }

    @RequestMapping(value="/verification-account", method= {RequestMethod.GET, RequestMethod.POST})
    public void confirmUserAccount(@RequestParam("token")String verificationToken)
    {
    	VerificationToken token = verificationTokenRepo.findByVerificationToken(verificationToken);

        if(token != null)
        {
            User user = userRepository.findByEmail(token.getUser().getEmail());
            user.save();
          //todo smth message for clien (account Verified)
        }
        else
        {
        	
            
            //todo smth message for clien (link is invalid)
        }

        
    }
    
}