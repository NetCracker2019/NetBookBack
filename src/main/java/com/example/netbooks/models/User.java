package com.example.netbooks.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.netbooks.dao.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
@Getter @Setter
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
	private String sex;
	private Date regDate;
    private String avatarFilePath;    
    private String country;    
    private String city;    
    private String status;
    private boolean activity;
    private boolean turnOnNotif;
    private Role role;
    private Date minRefreshDate;

	public void setRoleInt(int roleId) {
		this.role = Role.values()[roleId - 1];
	}
}