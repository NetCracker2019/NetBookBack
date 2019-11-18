package com.example.netbooks.services;

import java.util.Date;
import java.util.List;

import com.example.netbooks.dao.AchievementRepository;
import com.example.netbooks.models.Achievement;
import com.example.netbooks.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.netbooks.dao.UserRepository;
import com.example.netbooks.models.User;

@Service
public class UserManager {
	@Autowired
	UserRepository userRepository;
	@Autowired
	AchievementRepository achievementRepository;

	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void removeUserById(long id) {
		userRepository.removeUserById(id);
	}
	
	public void updateUser(User user) {
		userRepository.updateUser(user);
	}
	
	public void saveUser(User user) {
		userRepository.save(user);
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

	public User getUserByLogin(String login) {
		return userRepository.findByLogin(login);
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
}