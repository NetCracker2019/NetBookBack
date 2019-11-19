package com.example.netbooks.controllers;

import com.example.netbooks.models.Achievement;
import com.example.netbooks.models.Book;
import com.example.netbooks.models.ShortBookDescription;
import com.example.netbooks.models.User;
import com.example.netbooks.services.BookManager;
import com.example.netbooks.services.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping(value = "/profile")
public class ProfileController {
    private final Logger logger = LogManager.getLogger(ProfileController.class);
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserManager userManager;
    @Autowired
    private BookManager bookManager;

    @GetMapping("/{login}")
    public User getUser(@PathVariable("login")String login){
        return userManager.getUserByLogin(login);
    }

    @GetMapping("/{login}/get-achievement")
    public Achievement getAchievements(@PathVariable("login")String login){
        return userManager.getAchievementByLogin(login);
    }

    @PutMapping("/{login}/edit")
    public void editUser(@PathVariable("login")String login, @RequestBody User user){
        User originalUser = userManager.getUserByLogin(login);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        originalUser.compareAndReplace(user);
        userManager.updateUser(originalUser);
    }

    @GetMapping("/{login}/friends")
    public List<User> getFriends(@PathVariable("login")String login,
                                 @RequestParam("cnt")int cntFriends, @RequestParam("offset")int offset){
        return userManager.getFriendsByLogin(login, cntFriends, offset);
    }
    @GetMapping("/{login}/favourite-books")
    public List<ShortBookDescription> getFavouriteBooks(@PathVariable("login")String login,
                                                        @RequestParam("cnt")int cntBooks, @RequestParam("offset")int offset){
        return bookManager.getFavouriteBooksByUserId(
                userManager.getUserByLogin(login).getUserId(), cntBooks, offset);
    }
    @GetMapping("/{login}/reading-books")
    public List<ShortBookDescription> getReadingBooks(@PathVariable("login")String login,
                                                        @RequestParam("cnt")int cntBooks, @RequestParam("offset")int offset){
        return bookManager.getReadingBooksByUserId(
                userManager.getUserByLogin(login).getUserId(), cntBooks, offset);
    }
    @GetMapping("/{login}/read-books")
    public List<ShortBookDescription> getReadBooks(@PathVariable("login")String login,
                                                        @RequestParam("cnt")int cntBooks, @RequestParam("offset")int offset){
        return bookManager.getReadBooksByUserId(
                userManager.getUserByLogin(login).getUserId(), cntBooks, offset);
    }
}
