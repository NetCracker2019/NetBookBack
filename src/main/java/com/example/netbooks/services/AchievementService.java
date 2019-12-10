package com.example.netbooks.services;

import com.example.netbooks.dao.implementations.AchievementRepository;
import com.example.netbooks.models.Achievement;
import com.example.netbooks.models.User;
import com.example.netbooks.models.UserAchievement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;


     @Autowired
    public AchievementService(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;

    }


    long getAchvIdByParameters(int count, String type, int n) {
        long achvId = 0;
        switch (count) {
            case 1:
                achvId = achievementRepository.getAchvIdByDescription(type, n);
                break;
            case 10:
                achvId = achievementRepository.getAchvIdByDescription(type, 10 * n);
                break;
            case 100:
                achvId = achievementRepository.getAchvIdByDescription(type, 100 * n);
                break;
        }

        return achvId;
    }
    UserAchievement addAchievementToUser(long achvId, long userId ) {
        if (achvId > 0){
            boolean in = achievementRepository.checkAchvInUserAchv(userId, achvId);
            if (!in){
               // Thread notifThread = new Thread(() -> {
               //     User tmpUser = userManager.getUserById(userId);
               //     List<User> friends = userManager.getFriendsByUsername(tmpUser.getLogin());
               //     List<User> subscribers = userManager.getSubscribersByLogin(tmpUser.getLogin());
               //     friends.addAll(subscribers);
               //     friends.add(tmpUser);
               //     notificationService.createAndSaveAchievNotif(userId, friends, achvId);
               // });
//
               // notifThread.start();
                achievementRepository.addAchievementForUser(achvId, userId);
                return achievementRepository.getLastUserAchievement(userId);
            }
        }
        return null;
    }

    public boolean addAchievement(Achievement achievement) {
        String validatedTitle = achievement.getTitle().trim().replaceAll(" +", " ");
        achievement.setTitle(validatedTitle.substring(0,1).toUpperCase() + validatedTitle.substring(1));
        String validatedDescription = achievement.getDescription().trim().replaceAll(" +", " ");
        achievement.setDescription(validatedDescription.substring(0,1).toUpperCase() + validatedDescription.substring(1));
        return achievementRepository.addAchievement(achievement);
    }
    public List<Achievement> getAllAchievements() {
        return achievementRepository.getAllAchievements();
    }
}
