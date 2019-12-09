package com.example.netbooks.dao.interfaces;

import com.example.netbooks.models.User;

import java.util.Date;
import java.util.List;

public interface UserRepository {
    void save(User user);

    void updateUser(User user);

    void updateUserById(User user, Long id);

    Iterable<User> getAllUsers();

    Integer getUserIdByLogin(String login);

    Integer countFriendsForUser(long userId);

    User findByEmail(String email);

    User findByLogin(String login);

    User findByUserId(Long id);

    Boolean isExistByLogin(String login);

    Boolean isExistByMail(String mail);

    void removeUserById(Long id);

    void activateUser(Long id);

    void deActivateUser(Long id);

    void setMinRefreshDate(String login, Date date);

    List<User> getFriendsByLogin(String login, int cntFriends, int offset);

    List<User> getPersonsBySought(String sought, int cntPersons, int offset);

    List<User> getClientPersonsBySought(String sought, int cntPersons, int offset);

    List<User> getFriendsBySought(String login, String sought, int cntPersons, int offset);

    String getUserRole(String login);

    Integer getCountFriendsBySought(String login, String sought);

    Integer getCountPersonsBySought(String sought);

    void addFriend(String ownLogin, String friendLogin);

    int isFriend(String ownLogin, String friendLogin);

    void deleteFriend(String ownLogin, String friendLogin);
}
