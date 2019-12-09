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
    private NotificationService notificationService;
    @Autowired
    public ProfileController(PasswordEncoder passwordEncoder,
                             UserManager userManager, BookService bookService,
                             FileStorageService fileStorageService,
                             NotificationService notificationService) {
        this.passwordEncoder = passwordEncoder;
        this.userManager = userManager;
        this.bookService = bookService;
        this.fileStorageService = fileStorageService;
        this.notificationService = notificationService;
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
    public List<Achievement> getAchievements(@PathVariable("login")String login){
        log.info("fd {}", userManager.getAchievementByLogin(login));
        return userManager.getAchievementByLogin(login);
    }

    @PutMapping("/edit")
    public void editUser(@RequestBody User user){
        User originalUser = userManager.getUserByLogin(getCurrentUserLogin());
        log.info("ff {}", user.getAvatarFilePath());
        log.info("ff {}", originalUser.getAvatarFilePath());
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
    @PostMapping("/add-friend/{friendLogin}")
    public void addFriend(@PathVariable("friendLogin") String friendLogin) {
        String ownLogin = getCurrentUserLogin();
        userManager.addFriend(ownLogin, friendLogin);
        Thread notifThread = new Thread(() -> {
            Notification notification = new Notification();
            notification.setNotifTypeId(1);
            notification.setUserId((int) (userManager.getUserByLogin(friendLogin).getUserId()));
            notification.setFromUserId((int) (userManager.getUserByLogin(ownLogin).getUserId()));
            notificationService.addNotification(notification);
        });
        notifThread.start();

    }
    /* 1 - is friend
     * 0 - is subscribe
     * -1 - not friend */
    @GetMapping("/is-friend/{ownLogin}/{friendLogin}")
    public int isFriend(@PathVariable("ownLogin")String ownLogin,
                        @PathVariable("friendLogin") String friendLogin){

        return userManager.isFriend(ownLogin, friendLogin);
    }
    @DeleteMapping("/delete-friend/{friendLogin}")
    public void deleteFriend(@PathVariable("friendLogin") String friendLogin){
        userManager.deleteFriend(getCurrentUserLogin(), friendLogin);
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
    @PutMapping("/{shelf}/add-books")
    public void addBookBatchTo(@PathVariable("shelf")String shelf,
                               @RequestBody List<Long> booksId){
        bookService.addBookBatchTo(userManager.getUserByLogin(getCurrentUserLogin()).getUserId(), shelf, booksId);
    }
    @PutMapping("/{shelf}/remove-books")
    public void removeBookBatchFrom(@PathVariable("shelf")String shelf,
                                    @RequestBody List<Long> booksId){
        bookService.removeBookBatchFrom(userManager.getUserByLogin(getCurrentUserLogin()).
                getUserId(), shelf, booksId);
    }
    @DeleteMapping("/remove-books")
    public void removeBookBatch(@RequestParam("booksid") List<Long> booksId){
        bookService.removeBookBatch(userManager.getUserByLogin(getCurrentUserLogin()).getUserId(), booksId);
    }

    private String getCurrentUserLogin(){
        return ((UserDetails) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal()).getUsername();
    }
}