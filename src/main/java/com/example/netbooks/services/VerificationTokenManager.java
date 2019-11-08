package com.example.netbooks.services;

import com.example.netbooks.dao.UserRepository;
import com.example.netbooks.dao.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.netbooks.models.User;
import com.example.netbooks.models.VerificationToken;
import java.util.LinkedList;
import org.springframework.stereotype.Component;

@Component
public class VerificationTokenManager {
    
    @Autowired
    VerificationTokenRepository verificationTokenRepository;
    
    public VerificationToken findVerificationToken(String verificationToken) {
        return verificationTokenRepository.findByVerificationToken(verificationToken);
    }
    
    public boolean removeVerificationToken(String verificationToken) {
        return verificationTokenRepository.removeVerificationToken(findVerificationToken(verificationToken));
    }
    
    public LinkedList<VerificationToken> getAllVerificationTokens()
    {
        return verificationTokenRepository.GetAllVerificationTokens();
    }
   
    public void saveToken(VerificationToken VerTok)
    {
       verificationTokenRepository.save(VerTok);
    }
}