package com.example.netbooks.controllers;

import com.example.netbooks.models.User;
import com.example.netbooks.services.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping(value = "/profile")
public class ProfileController {
    private final Logger logger = LogManager.getLogger(AuthenticationController.class);
    @Autowired
    private UserManager userManager;

    @GetMapping("/{login}")
    public User getUser(@PathVariable("login")String login){
        return userManager.getUserByLogin(login);
    }
}
