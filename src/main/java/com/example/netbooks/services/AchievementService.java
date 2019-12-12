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

    public AchievementService(AchievementRepository achievementRepository) {
        this.achievementRepository = achievementRepository;

    }

    public void checkBookPatternAchievementsAndSendNotification(long userId, long bookId, String favOrRead) {
        boolean addedAuthorAchv = achievementRepository.checkAchievementAuthor(userId, bookId, favOrRead);
        if (addedAuthorAchv) {
            UserAchievement userAchievement = achievementRepository.getLastUserAchievement(userId);
            // TODO Notification sending must be here.
        }
        boolean addedGenreAchv = achievementRepository.checkAchievementGenre(userId, bookId, favOrRead);
        if (addedGenreAchv) {
            UserAchievement userAchievement = achievementRepository.getLastUserAchievement(userId);
            // TODO Notification sending must be here.
        }
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
