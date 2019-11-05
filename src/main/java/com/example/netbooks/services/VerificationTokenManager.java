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
    
    public VerificationToken FindVerificationToken(String verificationToken) {
        return verificationTokenRepository.findByVerificationToken(verificationToken);
    }
    
    public boolean RemoveVerificationToken(String verificationToken) {
        return verificationTokenRepository.removeVerificationToken(FindVerificationToken(verificationToken));
    }
    
    public VerificationTokenManager(VerificationTokenRepository verificationTokenRepository)
    {
        this.verificationTokenRepository = verificationTokenRepository;
    }
    
    public LinkedList<VerificationToken> GetAllVerificationTokens()
    {
        return verificationTokenRepository.GetAllVerificationTokens();
    }
   
    public VerificationToken SaveToken(User user)
    {
       VerificationToken VerTok = new VerificationToken(user);
       verificationTokenRepository.save(VerTok);
       return VerTok;
    }
}