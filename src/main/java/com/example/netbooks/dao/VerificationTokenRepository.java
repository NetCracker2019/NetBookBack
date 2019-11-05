package com.example.netbooks.dao;

import com.example.netbooks.models.User;
import com.example.netbooks.models.VerificationToken;
import java.util.LinkedList;
import org.springframework.stereotype.Repository;

@Repository
public class VerificationTokenRepository {

    private static LinkedList<VerificationToken> VerificationTokensList= new LinkedList<VerificationToken>();
    
    
    public boolean save(VerificationToken verificationToken) {
        return VerificationTokensList.add(verificationToken);
    }
    
    public boolean removeVerificationToken(VerificationToken verificationToken) {
        return VerificationTokensList.remove(verificationToken);
    }

    public LinkedList<VerificationToken> GetAllVerificationTokens()
    {
        return VerificationTokensList;
    }
    
    public VerificationToken findByVerificationToken(String verificationToken) {
        for(VerificationToken VT : VerificationTokensList)
        {
            if(VT.getVerificationToken().equals(verificationToken))
                return VT;
        }
        return null;
    }
}