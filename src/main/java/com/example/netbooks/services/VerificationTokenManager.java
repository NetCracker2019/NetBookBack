package com.example.netbooks.services;

import com.example.netbooks.dao.implementations.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.netbooks.models.VerificationToken;
import org.springframework.stereotype.Component;

@Component
public class VerificationTokenManager {
    
    @Autowired
    VerificationTokenRepository verificationTokenRepository;
    
    public VerificationToken findVerificationToken(String verificationToken) {
        return verificationTokenRepository.findByVerificationToken(verificationToken);
    }
    
    public void removeVerificationToken(String verificationToken) {
        verificationTokenRepository.removeVerificationToken(verificationToken);
    }
    
    public void saveToken(VerificationToken VerTok)
    {
       verificationTokenRepository.save(VerTok);
    }
}
