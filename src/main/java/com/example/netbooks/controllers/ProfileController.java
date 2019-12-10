package com.example.netbooks.controllers;

import com.example.netbooks.exceptions.CustomException;
import com.example.netbooks.models.*;
import com.example.netbooks.services.BookService;
import com.example.netbooks.services.NotificationService;
import com.example.netbooks.services.UserManager;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private UserManager userManager;
    private BookService bookService;
    private NotificationService notificationService;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public ProfileController(UserManager userManager, BookService bookService,
                             NotificationService notificationService,
                             PasswordEncoder passwordEncoder) {
        this.userManager = userManager;
        this.bookService = bookService;
        this.notificationService = notificationService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/{login}")
    public User getUser(@PathVariable("login")String login){
        User user = userManager.getUserByLogin(login);
        //block if its admin but not you
        if(!user.getRole().equals(Role.ROLE_CLIENT) &&
                getCurrentUserRole().equals(Role.ROLE_CLIENT)){
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    @GetMapping("/{login}/get-achievement")
    public List<Achievement> getAchievements(@PathVariable("login")String login){
        log.info("fd {}", userManager.getAchievementByLogin(login));
        return userManager.getAchievementByLogin(login);
    }

    @PutMapping("/{login}/edit")
    public void editUser(@PathVariable("login")String login,
                         @RequestBody User user){
        log.info("gfg {}", user.getSex());
        if(!login.equals(getCurrentUserLogin()) && Integer.parseInt(userManager.getUserRole(login)) - 1
                <= getCurrentUserRole().ordinal()) return;
        userManager.updateUser(compareAndReplace(user));
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

    @GetMapping("/{login}/is-editable")
    public boolean isEditable(@PathVariable("login")String login){
        if(getCurrentUserLogin().equals(login)) return true;
        return Integer.parseInt(userManager.getUserRole(login)) - 1 >
                getCurrentUserRole().ordinal();
    }
    @PostMapping("/add-friend/{friendLogin}")
    public void addFriend(@PathVariable("friendLogin") String friendLogin) {
        String ownLogin = getCurrentUserLogin();
        if(!userManager.getUserRole(friendLogin).equals("4")) return;
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
    private User compareAndReplace(User user){
        User originalUser = userManager.getUserByLogin(user.getLogin());
        if(!Strings.isNullOrEmpty(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if(!Strings.isNullOrEmpty(originalUser.getAvatarFilePath()) && !
                originalUser.getAvatarFilePath().equals(user.getAvatarFilePath())){
            userManager.deleteFile(originalUser.getAvatarFilePath());
        }
        originalUser.compareAndReplace(user);
        return originalUser;
    }
    private String getCurrentUserLogin(){
        return ((UserDetails) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal()).getUsername();
    }
    private Role getCurrentUserRole(){
        UserDetails currentUserDetails
                = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return (Role) (currentUserDetails.getAuthorities().iterator().next());
    }
}