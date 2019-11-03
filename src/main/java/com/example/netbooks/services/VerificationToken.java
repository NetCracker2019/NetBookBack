package com.example.netbooks.services;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class VerificationToken {

	private long tokenId;

    private String verificationToken;

    private Date createdDate;
    
    private User user;
    
    public VerificationToken() {
    }

    public VerificationToken(User user) {
        this.user = user;
        createdDate = new Date();
        verificationToken = UUID.randomUUID().toString();
    }

	
    public long getTokenId() {
		return tokenId;
	}

	public void setTokenId(long tokenId) {
		this.tokenId = tokenId;
	}

	public String getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User userService) {
		this.user = userService;
	}

	
    
}