package com.example.netbooks.services;

import com.example.netbooks.dao.UserRepository;
import org.apache.commons.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.netbooks.models.User;
import java.util.LinkedList;

@Component
public class UserManager {

    @Autowired
    UserRepository userRepository;

    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User GetUserByMail(User user) {
        return userRepository.findByEmail(user.getEmail());
    }

    public User GetUserByMailAndRole(String mail, String role) {
        return userRepository.findByEmailAndRole(mail, role);
    }

    public User GetUserByMail(String mail) {
        return userRepository.findByEmail(mail);
    }

    public User GetUserByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    
    public LinkedList<User> GetAllUsers() {
        return userRepository.GetAllUsers();
    }

    public boolean SaveUser(User user) {
        return userRepository.save(user);
    }

    public User CreateAdmin(User user) {
        User adminUser = user;
        adminUser.setRole("admin");
        adminUser.setPassword(adminUser.getPassword() + "admin");
        if (GetUserByMailAndRole(adminUser.getEmail(), adminUser.getRole()) == null) {
            userRepository.save(adminUser);
        } else {
            return null;
        }
        return adminUser;
    }

    public User CreateModerator(User user) {
        User moderatorUser = user;
        moderatorUser.setRole("moderator");
        moderatorUser.setPassword(moderatorUser.getPassword() + "moderator");
        if (GetUserByMailAndRole(moderatorUser.getEmail(), moderatorUser.getRole()) == null) {
            userRepository.save(moderatorUser);
        } else {
            return null;
        }
        return moderatorUser;

    }

}