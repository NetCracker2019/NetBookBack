package com.example.netbooks.controllers;

import com.example.netbooks.models.Role;
import com.example.netbooks.models.User;
import com.example.netbooks.services.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping(value = "/find-persons")
@Slf4j
public class FindPersonController {

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
            sought = "";
        }
        if (userManager.getUserByLogin(login).getRole() == Role.ROLE_CLIENT) {
            if ("all".equals(where)) {
                return userManager.getClientPersonsBySought(sought, cntPersons, offset);
            } else {
                return userManager.getFriendsBySought(login, sought, cntPersons, offset);
            }
        } else {
            if ("all".equals(where)) {
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
        if ("all".equals(where)) {
            return userManager.getCountPersonsBySought(sought);
        } else {
            return userManager.getCountFriendsBySought(login, sought);
        }

    }

}
