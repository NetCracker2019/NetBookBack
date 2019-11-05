package com.example.netbooks.models;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.netbooks.dao.UserRepository;

import lombok.Data;


@Data
public class User {
	private long userId;
	private String userName;	
	private String login;
	private String email;
    private String password;
    private LocalDate regDate;
    private String avatarFilePath;    
    private String country;    
    private String city;    
    private String status;
    private String role;
    
    


	public void setRole(String role) {
		this.role = role;
	}


	public User() {
    	
    }
    
    
    public User(String email, String password, String username, String role) {
        this.email = email;
        this.password = password;
        this.userName = login;
        this.role = role;
    }
    
    public User(Map<String, String> user) {
    	this.userName = user.get("firstName");
    	this.login = user.get("lastName");
    	this.email = user.get("username");
    	this.password = user.get("password");
    	
    	/*
    	 * userName == firstName
    	login == lastName
    	email == username
    	 */
    }
    
    
    public String getRole() {
		return role;
	}
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDate regDate) {
		this.regDate = regDate;
	}

	public String getAvatarFilePath() {
		return avatarFilePath;
	}

	public void setAvatarFilePath(String avatarFilePath) {
		this.avatarFilePath = avatarFilePath;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
    
   

}