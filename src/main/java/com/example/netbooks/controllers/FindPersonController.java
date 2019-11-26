package com.example.netbooks.controllers;

import com.example.netbooks.models.Role;
import com.example.netbooks.models.User;
import com.example.netbooks.services.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping(value = "/find-persons")
public class FindPersonController {

    private final Logger logger = LogManager.getLogger(ProfileController.class);
    private UserManager userManager;

    @Autowired
    public FindPersonController(UserManager userManager) {
        this.userManager = userManager;
    }

    @GetMapping("/{login}")
    public List<User> getUser(@PathVariable("login") String login,
            @RequestParam("sought") String sought, @RequestParam("where") String where,
            @RequestParam("cnt") int cntPersons, @RequestParam("offset") int offset) {
        if (sought == null) {
            sought = new String("");
        }
        if (userManager.getUserByLogin(login).getRole() == Role.ROLE_CLIENT) {
            if (where.equals("all")) {
                return userManager.getClientPersonsBySought(sought, cntPersons, offset);
            } else {
                return userManager.getFriendsBySought(login, sought, cntPersons, offset);
            }
        } else {
            if (where.equals("all")) {
                return userManager.getPersonsBySought(sought, cntPersons, offset);
            } else {
                return userManager.getFriendsBySought(login, sought, cntPersons, offset);
            }
        }
    }

    //get size of result set(without pagination)
    @GetMapping("/{login}/collection-size")
    public int getCollectionSize(@PathVariable("login") String login,
            @RequestParam("sought") String sought, @RequestParam("where") String where) {
        if (sought == null) {
            sought = new String("");
        }
        if (where.equals("all")) {
            return userManager.getCountPersonsBySought(sought);
        } else {
            return userManager.getCountFriendsBySought(login, sought);
        }

    }

}