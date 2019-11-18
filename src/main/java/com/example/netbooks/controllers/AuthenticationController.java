package com.example.netbooks.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.websocket.server.PathParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.netbooks.services.EmailSender;
import com.example.netbooks.services.UserManager;
import com.example.netbooks.services.VerificationTokenManager;
import com.example.netbooks.dao.implementations.UserRepository;
import com.example.netbooks.exceptions.CustomException;
import com.example.netbooks.models.Role;
import com.example.netbooks.models.User;
import com.example.netbooks.models.VerificationToken;
import com.example.netbooks.security.JwtProvider;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping(value = "/user-service")
public class AuthenticationController {

    private final Logger logger = LogManager.getLogger(AuthenticationController.class);
    @Autowired
    private UserManager userManager;
    @Autowired
    EmailSender emailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private VerificationTokenManager verificationTokenManager;

    @PutMapping("/interrupt-sessions/{login}")
    public void interruptr(@PathVariable("login") String login) {
        userManager.setMinRefreshDate(login, null);
    }

    @PostMapping("/register/user")
    public ResponseEntity<Map> register(@RequestBody User user) {
        if (userManager.getUserByLogin(user.getLogin()) == null
                && userManager.getUserByEmail(user.getEmail()) == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(Role.ROLE_CLIENT);
            userManager.saveUser(user);

            VerificationToken verificationToken = new VerificationToken(
                    userManager.getUserByLogin(user.getLogin()).getUserId());
            verificationTokenManager.saveToken(verificationToken);

            String message = "To verification your account, please click here : "
                    + "https://netbooksfront.herokuapp.com/verification-account?token="
                    + verificationToken.getVerificationToken();
            emailSender.sendMessage(user.getEmail(), "Complete Registration!", message);
            logger.info("Complete Registration for {}", user.getLogin());

            Map<Object, Object> response = new HashMap<>();
            response.put("msg", "Successful registration");
            return ResponseEntity.ok(response);
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping("/verification/user")
    public ResponseEntity<Map> confirmUserAccount(@RequestParam("token") String verificationToken) {
        VerificationToken token = verificationTokenManager.findVerificationToken(verificationToken);
        if (token != null) {
            userManager.activateUser(token.getUserId());
            logger.info("successRegister for {}", verificationToken);
            verificationTokenManager.removeVerificationToken(verificationToken);
            // TODO del addled tokens

            Map<Object, Object> response = new HashMap<>();
            response.put("msg", "Successful account verification");
            return ResponseEntity.ok(response);
        } else {
            logger.info("Fail Register!" + verificationToken);
            throw new CustomException("Invalid token", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<Map> signin(@RequestBody User user) {
        try {
            logger.info("Try to login " + user.getLogin() + " ---- " + user.getPassword());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));

            Map<Object, Object> response = new HashMap<>();
            response.put("token", jwtProvider.createToken(user.getLogin(), user.getRole()));
            response.put("msg", "Successful login");
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/users")
    public Iterable<User> getAllUsers() {
        return userManager.getAllUsers();
    }

    @DeleteMapping("/remove/{id}")
    public void removeUser(@PathVariable("id") long id) {
        userManager.removeUserById(id);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<Map> register(@RequestBody User user, @RequestParam("token") String verificationToken) {
        if (userManager.getUserByLogin(user.getLogin()) == null
                && userManager.getUserByEmail(user.getEmail()) == null) {
            VerificationToken token = verificationTokenManager.findVerificationToken(verificationToken);
            if (token != null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userManager.updateUserById(user, token.getUserId());
                userManager.activateUser(token.getUserId());
                verificationTokenManager.removeVerificationToken(verificationToken);
                logger.info("Complete Admin Registration! {}", user.getLogin());

                Map<Object, Object> response = new HashMap<>();
                response.put("msg", "Successful registration");
                return ResponseEntity.ok(response);
            } else {
                throw new CustomException("Admin token is invalid.", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/send-admin-reg-mail")//TODO change mapping
    public ResponseEntity<Map> sendAdminRegMail(@RequestBody String mail) {
        User user = new User();
        String tempLogPass = UUID.randomUUID().toString();
        user.setLogin(tempLogPass);
        user.setPassword(tempLogPass);
        user.setEmail(tempLogPass);
        user.setName(tempLogPass);
        user.setRole(Role.ROLE_CLIENT);

        userManager.saveUser(user);
        VerificationToken verificationToken = new VerificationToken(
                userManager.getUserByLogin(user.getLogin()).getUserId());
        verificationTokenManager.saveToken(verificationToken);

        String message = "To register your admin account, please click here : "
                + "https://netbooksfront.herokuapp.com/verification-admin?token="
                + verificationToken.getVerificationToken();
        emailSender.sendMessage(user.getEmail(), "Register admin account!", message);
        logger.info("Admin registration mail sent! {}", user.getLogin() + message);

        Map<Object, Object> response = new HashMap<>();
        response.put("msg", "Successful registration");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recovery/password")
    public ResponseEntity<Map> recoveryPassRequest(@RequestParam("email") String email) {
        User user = userManager.getUserByEmail(email);
        if (user != null) {
            VerificationToken verificationToken = new VerificationToken(user.getUserId());
            verificationTokenManager.saveToken(verificationToken);

            String message = "To recovery your password, please click here : "
                    + "https://netbooksfront.herokuapp.com/recovery-password?token="
                    + verificationToken.getVerificationToken();
            emailSender.sendMessage(user.getEmail(), "Recovery your password", message);
            Map<Object, Object> response = new HashMap<>();
            response.put("msg", "Password recovery letter has been sent successfully");
            return ResponseEntity.ok(response);
        } else {
            throw new CustomException("User with this email not found " + email, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping("/change/password")
    public ResponseEntity<Map> recoveryPass(@RequestParam("token") String verificationToken,
            @RequestParam("pass") String newPass) {
        logger.info("success recovery request " + newPass + " " + verificationToken);
        VerificationToken token = verificationTokenManager.findVerificationToken(verificationToken);
        if (token != null) {
            User user = userManager.getUserById(token.getUserId());
            user.setPassword(passwordEncoder.encode(newPass));
            user.setMinRefreshDate(null);
            userManager.updateUser(user);
            verificationTokenManager.removeVerificationToken(verificationToken);
            // TODO del addled tokens
            Map<Object, Object> response = new HashMap<>();
            response.put("msg", "successful password recovery");
            return ResponseEntity.ok(response);
        } else {
            throw new CustomException("Invalid recovery password link", HttpStatus.NOT_FOUND);
        }
    }
}
