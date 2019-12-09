package com.example.netbooks.controllers;


import com.example.netbooks.models.Achievement;
import com.example.netbooks.models.ViewBook;
import com.example.netbooks.services.AchievementService;
import com.example.netbooks.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping("/achievement-manager")
public class AchievementController {
    private AchievementService achievementService;

    @Autowired
    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @PostMapping("/add-achievement")
    public boolean addAchievement(@RequestBody Achievement achievement) {
        boolean result = achievementService.addAchievement(achievement);
        log.info("Achievement: "+achievement);
        return result;
    }
    @GetMapping("/achievements")
    public List<Achievement> getAllAchievements() {
        return  achievementService.getAllAchievements();
    }
}
