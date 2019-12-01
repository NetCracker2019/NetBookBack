package com.example.netbooks.services;

import java.util.Date;
import java.util.List;

import com.example.netbooks.dao.implementations.AchievementRepository;
import com.example.netbooks.models.Achievement;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.netbooks.dao.implementations.UserRepository;
import com.example.netbooks.exceptions.CustomException;
import com.example.netbooks.models.Role;
import com.example.netbooks.models.User;
import java.util.UUID;

@Service
public class UserManager {

    UserRepository userRepository;
    AchievementRepository achievementRepository;

    @Autowired
    public UserManager(UserRepository userRepository, AchievementRepository achievementRepository) {
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public int getUserIdByName(String name) {
        return userRepository.getUserIdByName(name);
    }

    public void removeUserById(long id) {
        userRepository.removeUserById(id);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public void updateUserById(User user, Long id) {
        userRepository.updateUserById(user, id);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User createAndSaveTempAdmin() {
        User user = new User();
        String tempLogPass = UUID.randomUUID().toString();
        user.setLogin(tempLogPass);
        user.setPassword(tempLogPass);
        user.setEmail(tempLogPass);
        user.setName(tempLogPass);
        user.setRole(Role.ROLE_ADMIN);
        saveUser(user);
        return user;
    }

    public User createAndSaveTempModer() {
        User user = new User();
        String tempLogPass = UUID.randomUUID().toString();
        user.setLogin(tempLogPass);
        user.setPassword(tempLogPass);
        user.setEmail(tempLogPass);
        user.setName(tempLogPass);
        user.setRole(Role.ROLE_MODER);
        saveUser(user);
        return user;
    }

    public void activateUser(long id) {
        userRepository.activateUser(id);
    }

    public void deActivateUser(long id) {
        userRepository.deActivateUser(id);
    }

    public User getUserByMail(String mail) {
        return userRepository.findByEmail(mail);
    }

    public User getUserById(long id) {
        return userRepository.findByUserId(id);
    }

    public Boolean isExistByLogin(String login) {
        return userRepository.isExistByLogin(login);
    }

    public Boolean isExistByMail(String mail) {
        return userRepository.isExistByMail(mail);
    }

    public User getUserByLogin(String login) {
        try {
            return userRepository.findByLogin(login);
        } catch (CustomException ex) {
            throw ex;
        }
    }

    public Iterable<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void setMinRefreshDate(String login, Date date) {
        userRepository.setMinRefreshDate(login, date);
    }

    public Achievement getAchievementByLogin(String login) {
        return achievementRepository.findByAchievementId(
                userRepository.findByLogin(login).getUserId());
    }

    public List<User> getFriendsByLogin(String login, int cntFriends, int offset) {
        return userRepository.getFriendsByLogin(login, cntFriends, offset);
    }

    public List<User> getPersonsBySought(String sought, int cntPersons, int offset) {
        return userRepository.getPersonsBySought(sought, cntPersons, offset);
    }

    public List<User> getClientPersonsBySought(String sought, int cntPersons, int offset) {
        return userRepository.getClientPersonsBySought(sought, cntPersons, offset);
    }

    public List<User> getFriendsBySought(String login, String sought, int cntPersons, int offset) {
        return userRepository.getFriendsBySought(login, sought, cntPersons, offset);
    }

    public String getUserRole(String login) {
        return userRepository.getUserRole(login);
    }

    public int getCountPersonsBySought(String sought) {
        return userRepository.getCountPersonsBySought(sought);
    }

    public int getCountFriendsBySought(String login, String sought) {
        return userRepository.getCountFriendsBySought(login, sought);
    }

    public void addFriend(String ownLogin, String friendLogin) {
        userRepository.addFriend(ownLogin, friendLogin);
    }

    public boolean isFriend(String ownLogin, String friendLogin) {
        return userRepository.isFriend(ownLogin, friendLogin);
    }

    public void deleteFriend(String ownLogin, String friendLogin) {
        userRepository.deleteFriend(ownLogin, friendLogin);
    }

    public void updateUserBookList(String login, Long bookId, boolean reading, boolean favourite, boolean remove) {
        userRepository.updateUserBookList(login, bookId, reading, favourite, remove);
    }
}
