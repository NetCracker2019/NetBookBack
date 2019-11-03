package com.example.netbooks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.netbooks.dao.UserRepository;


@Component
public class User {
	
	private UserRepository userRepository;
	
	private long userId;

    private String email;

    private String password;

    private String firstName;

    private String lastName;
    
    private String role;
    
    public User() {
    	
	}
    
    public User(String email, String password, String firstName, String lastName, String role) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
	}
    @Autowired	
    public User(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public void save() {
    	userRepository.save(this);
    }
    
    
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String emailId) {
		this.email = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}