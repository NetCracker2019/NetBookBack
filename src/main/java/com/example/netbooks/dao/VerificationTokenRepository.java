package com.example.netbooks.dao;

import org.springframework.stereotype.Repository;

import com.example.netbooks.services.VerificationToken;

@Repository
public class VerificationTokenRepository {

	
	public void save(VerificationToken verificationToken) {
		//save into db
	}
	
	
	public VerificationToken findByVerificationToken(String verificationToken) {
		//////////
		return new VerificationToken();
	}
}

