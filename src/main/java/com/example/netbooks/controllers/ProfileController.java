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

import static com.example.netbooks.services.NotificationEnum.ADD_FRIEND_NOTIF;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping(value = "/profile")
@Slf4j
public class ProfileController {
    private PasswordEncoder passwordEncoder;
    private UserManager userManager;
    private BookService bookService;
    private FileStorageService fileStorageService;
    @Autowired
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
        if(!checkAuth(login)) return;
        User originalUser = userManager.getUserByLogin(login);
        if(!Strings.isNullOrEmpty(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if(!Strings.isNullOrEmpty(originalUser.getAvatarFilePath()) && !
                originalUser.getAvatarFilePath().equals(user.getAvatarFilePath())){
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
                                            @RequestParam("sought")String sought,
                                            @RequestParam("cnt")int cntBooks,
                                            @RequestParam("offset")int offset){
        return bookService.getFavouriteBooksByUserId(
                userManager.getUserByLogin(login).getUserId(), sought, cntBooks, offset);
    }
    @GetMapping("/{login}/reading-books")
    public List<ViewBook> getReadingBooks(@PathVariable("login")String login,
                                          @RequestParam("sought")String sought,
                                          @RequestParam("cnt")int cntBooks,
                                          @RequestParam("offset")int offset){
        return bookService.getReadingBooksByUserId(
                userManager.getUserByLogin(login).getUserId(), sought, cntBooks, offset);
    }
    @GetMapping("/{login}/read-books")
    public List<ViewBook> getReadBooks(@PathVariable("login")String login,
                                       @RequestParam("sought")String sought,
                                       @RequestParam("cnt")int cntBooks,
                                       @RequestParam("offset")int offset){
        return bookService.getReadBooksByUserId(
                userManager.getUserByLogin(login).getUserId(), sought, cntBooks, offset);
    }
    @PostMapping("/add-friend/{ownLogin}/{friendLogin}")

    public void addFriend(@PathVariable("ownLogin")String ownLogin,
                         @PathVariable("friendLogin") String friendLogin) {
        if(!checkAuth(ownLogin)) return;
        userManager.addFriend(ownLogin, friendLogin);
        Notification notification = new Notification();
        notification.setNotifTypeId(1);
        notification.setUserId((int)(userManager.getUserByLogin(friendLogin).getUserId()));
        notification.setFromUserId((int)(userManager.getUserByLogin(ownLogin).getUserId()));
        notificationService.addNotification(notification);
//todo thread

    }
    /* 1 - is friend
    * 0 - is subscribe
    * -1 - not friend */
    @GetMapping("/is-friend/{ownLogin}/{friendLogin}")
    public int isFriend(@PathVariable("ownLogin")String ownLogin,
                            @PathVariable("friendLogin") String friendLogin){

        return userManager.isFriend(ownLogin, friendLogin);
    }
    @DeleteMapping("/delete-friend/{ownLogin}/{friendLogin}")
    public void deleteFriend(@PathVariable("ownLogin")String ownLogin,
                             @PathVariable("friendLogin") String friendLogin){
        if(!checkAuth(ownLogin)) return;
        userManager.deleteFriend(ownLogin, friendLogin);
    }
    @GetMapping("/{login}/book-list")
    public List<ViewBook> getBookList(@PathVariable("login")String login,
                                      @RequestParam("sought")String sought,
                                      @RequestParam("size")int size,
                                      @RequestParam("read")boolean read,
                                      @RequestParam("favourite")boolean favourite,
                                      @RequestParam("reading")boolean reading,
                                      @RequestParam("notset")boolean notSet,
                                      @RequestParam("sortby")String sortBy,
                                      @RequestParam("order")String order,
                                      @RequestParam("page")int page){
        return bookService.getBooksByUserId(
                userManager.getUserByLogin(login).getUserId(),
                sought, size, read, favourite, reading, notSet, sortBy, order, page);
    }
    @PutMapping("/{login}/{shelf}/add-books")
    public void addBookBatchTo(@PathVariable("login")String login,
                               @PathVariable("shelf")String shelf,
                               @RequestBody List<Long> booksId){
        if(!checkAuth(login)) return;
        bookService.addBookBatchTo(userManager.getUserByLogin(login).getUserId(), shelf, booksId);
    }
    @PutMapping("/{login}/{shelf}/remove-books")
    public void removeBookBatchFrom(@PathVariable("login")String login,
                                    @PathVariable("shelf")String shelf,
                                    @RequestBody List<Long> booksId){
        if(!checkAuth(login)) return;
        bookService.removeBookBatchFrom(userManager.getUserByLogin(login).getUserId(), shelf, booksId);
    }
    @DeleteMapping("/{login}/remove-books")
    public void removeBookBatch(@PathVariable("login")String login,
                                @RequestParam("booksid") List<Long> booksId){
        if(!checkAuth(login)) return;
        bookService.removeBookBatch(userManager.getUserByLogin(login).getUserId(), booksId);
    }
    private boolean checkAuth(String login){
        //return false if login != current user authentication
        return ((UserDetails) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal()).getUsername().equals(login);
    }
}