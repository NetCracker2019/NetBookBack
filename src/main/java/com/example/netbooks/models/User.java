package com.example.netbooks.models;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.netbooks.dao.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class User {
	@JsonProperty("id")
	private long userId;
	@JsonProperty("name")
	private String name;
	@JsonProperty("login")
	private String login;
	@JsonProperty("email")
	private String email;
	@JsonProperty("password")
    private String password;
    private LocalDate regDate;
    private String avatarFilePath;    
    private String country;    
    private String city;    
    private String status;
    private boolean activity;
    @JsonProperty("role")
    private Role role;


	public void setRole(Role role) {
		this.role = role;
	}


	public User() {
    	
    }
    
    
    public User(int id, String email, String password, String login, String name, Role role) {
    	this.userId = id;//rewrite
        this.email = email;
        this.password = password;
        this.login = login;
        this.name = name;
        this.role = role;
        
        this.activity = true;
    }
   
    public Role getRole() {
		return role;
	}
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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


	public boolean isActivity() {
		return activity;
	}


	public void setActivity(boolean activity) {
		this.activity = activity;
	}

	
    
   

}