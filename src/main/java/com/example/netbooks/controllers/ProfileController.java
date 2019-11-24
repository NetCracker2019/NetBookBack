package com.example.netbooks.controllers;

import com.example.netbooks.exceptions.CustomException;
import com.example.netbooks.models.*;
import com.example.netbooks.services.BookService;
import com.example.netbooks.services.FileStorageService;
import com.example.netbooks.services.NotificationService;
import com.example.netbooks.services.UserManager;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping(value = "/profile")
@Slf4j
public class ProfileController {
    private PasswordEncoder passwordEncoder;
    private UserManager userManager;
    private BookService bookService;
    private FileStorageService fileStorageService;
    private NotificationService notificationService;
    @Autowired
    public ProfileController(PasswordEncoder passwordEncoder,
                             UserManager userManager,
                             BookService bookService,
                             FileStorageService fileStorageService) {
        this.passwordEncoder = passwordEncoder;
        this.userManager = userManager;
        this.bookService = bookService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/{login}")
    public User getUser(@PathVariable("login")String login){
        try{
            return userManager.getUserByLogin(login);
        }catch (CustomException ex){
            throw ex;
        }
    }

    @GetMapping("/{login}/get-achievement")
    public Achievement getAchievements(@PathVariable("login")String login){
        return userManager.getAchievementByLogin(login);
    }

    @PutMapping("/{login}/edit")
    public void editUser(@PathVariable("login")String login, @RequestBody User user){
        //return if login != current user authentication
        if(!((UserDetails) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal()).getUsername().equals(login)){
            return;
        }
        User originalUser = userManager.getUserByLogin(login);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(!Strings.isNullOrEmpty(originalUser.getAvatarFilePath())){
            fileStorageService.deleteFile(originalUser.getAvatarFilePath());
        }
        originalUser.compareAndReplace(user);
        userManager.updateUser(originalUser);
    }

    @GetMapping("/{login}/friends")
    public List<User> getFriends(@PathVariable("login")String login,
                                 @RequestParam("cnt")int cntFriends, @RequestParam("offset")int offset){
        return userManager.getFriendsByLogin(login, cntFriends, offset);
    }
    @GetMapping("/{login}/favourite-books")
    public List<ViewBook> getFavouriteBooks(@PathVariable("login")String login,
                                                        @RequestParam("cnt")int cntBooks, @RequestParam("offset")int offset){
        return bookService.getFavouriteBooksByUserId(
                userManager.getUserByLogin(login).getUserId(), cntBooks, offset);
    }
    @GetMapping("/{login}/reading-books")
    public List<ViewBook> getReadingBooks(@PathVariable("login")String login,
                                                        @RequestParam("cnt")int cntBooks, @RequestParam("offset")int offset){
        return bookService.getReadingBooksByUserId(
                userManager.getUserByLogin(login).getUserId(), cntBooks, offset);
    }
    @GetMapping("/{login}/read-books")
    public List<ViewBook> getReadBooks(@PathVariable("login")String login,
                                       @RequestParam("cnt")int cntBooks, @RequestParam("offset")int offset){
        return bookService.getReadBooksByUserId(
                userManager.getUserByLogin(login).getUserId(), cntBooks, offset);
    }
    @PostMapping("/add-friend/{ownLogin}/{friendLogin}")
    public void register(@PathVariable("ownLogin")String ownLogin,
                         @PathVariable("friendLogin") String friendLogin) {
        log.info("ffggg {} ", friendLogin);
        if(!ownLogin.equals(((UserDetails) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal()).getUsername())){
            return;
        }
        userManager.addFriend(ownLogin, friendLogin);
        notificationService.addNotification(userManager.getUserByLogin(friendLogin).getUserId(),"addFriend");
        //TODO send notification to friend

    }
    @GetMapping("/is-friend/{ownLogin}/{friendLogin}")
    public boolean isFriend(@PathVariable("ownLogin")String ownLogin,
                            @PathVariable("friendLogin") String friendLogin){
        log.info("fffgggg {} {} ", friendLogin, userManager.isFriend(ownLogin, friendLogin));
        return userManager.isFriend(ownLogin, friendLogin);
    }
    @DeleteMapping("/delete-friend/{ownLogin}/{friendLogin}")
    public void deleteFriend(@PathVariable("ownLogin")String ownLogin,
                             @PathVariable("friendLogin") String friendLogin){
        if(!ownLogin.equals(((UserDetails) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal()).getUsername())){
            return;
        }
        userManager.deleteFriend(ownLogin, friendLogin);
    }
}
