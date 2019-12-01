package com.example.netbooks.services;

import com.example.netbooks.dao.implementations.AchievementRepository;
import com.example.netbooks.models.Achievement;
import org.springframework.stereotype.Service;

@Service
public class AchievementService {

    AchievementRepository achievementRepository;

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
}
