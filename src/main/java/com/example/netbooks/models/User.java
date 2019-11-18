package com.example.netbooks.models;

import java.util.Date;

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
	private String sex;
	private Date regDate;
    private String avatarFilePath;    
    private String country;    
    private String city;    
    private String status;
    private boolean activity;
    private boolean turnOnNotif;
    
    @JsonProperty("role")
    private Role role;
    private Date minRefreshDate;


	public User() {
    	
    }
    
    
    public User(int id, String email, String password, String login, String name, Role role) {
    	this.userId = id;//rewrite
        this.email = email;
        this.password = password;
        this.login = login;
        this.name = name;
        this.role = role;
        this.activity = true;//to
    }

	public User(long id, String email, String password, String login, String name, Role role,
				String sex, String avatarFilePath, String country, String city, String status) {
		this.userId = id;//rewrite
		this.email = email;
		this.password = password;
		this.login = login;
		this.name = name;
		this.role = role;
		this.sex = sex;
		this.avatarFilePath = avatarFilePath;
		this.country = country;
		this.city = city;
		this.status = status;
		this.activity = true;//to
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

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
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

	
	public Date getMinRefreshDate() {
		return minRefreshDate;
	}


	public void setMinRefreshDate(Date minRefreshDate) {
		this.minRefreshDate = minRefreshDate;
	}


	public String getSex() {
		return sex;
	}


	public void setSex(String sex) {
		this.sex = sex;
	}


	public boolean isTurnOnNotif() {
		return turnOnNotif;
	}


	public void setTurnOnNotif(boolean turnOnNotif) {
		this.turnOnNotif = turnOnNotif;
	}
	
	public void setRoleInt(int roleId) {
		this.role = Role.values()[roleId - 1];
	}
    public void setRole(Role role) {
		this.role = role;
	}

   

}
