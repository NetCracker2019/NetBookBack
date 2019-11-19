package com.example.netbooks.controllers;


import com.example.netbooks.models.User;
import com.example.netbooks.services.BookManager;
import com.example.netbooks.services.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping(value = "/find-persons")
public class FindPersonController {
    private final Logger logger = LogManager.getLogger(ProfileController.class);
    @Autowired
    private UserManager userManager;

    @GetMapping("/{login}")
    public List<User> getUser(@PathVariable("login")String login,
                              @RequestParam("sought")String sought, @RequestParam("where") String where,
                              @RequestParam("cnt")int cntPersons, @RequestParam("offset")int offset) {
        if(sought == null) {sought = new String("");}
        logger.info("login {}", login);
        logger.info("sought {}", sought);
        logger.info("cnt {}", where);
        if(where.equals("all")){
            return userManager.getPersonsBySought(sought, cntPersons, offset);
        }else{
            return userManager.getFriendsBySought(login, sought, cntPersons, offset);
        }

    }

}
