package com.example.netbooks.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.netbooks.dao.implementations.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
@Getter
@Setter
public class User {
	@JsonProperty("id")
	private long userId;
	@JsonProperty("firstName")
	private String name;
	@JsonProperty("username")
	private String login;
	@JsonProperty("email")
	private String email;
	@JsonProperty("password")
    private String password;
	@JsonProperty("sex")
	private String sex;
	@JsonProperty("regDate")
	private Date regDate;
	@JsonProperty("avatarFilePath")
    private String avatarFilePath;
	@JsonProperty("country")
    private String country;
	@JsonProperty("city")
    private String city;
	@JsonProperty("status")
    private String status;
    private boolean activity;
    private boolean turnOnNotif;
	@JsonProperty("role")
    private Role role;
    private Date minRefreshDate;

	public void setRoleInt(int roleId) {
		this.role = Role.values()[roleId - 1];
	}

    public void compareAndReplace(User user) {
		this.name = (Strings.isNullOrEmpty(user.getName()) && this.name != null) ? this.name : user.name;
		this.email = (Strings.isNullOrEmpty(user.getEmail()) && this.email != null) ? this.email : user.email;
		this.password = (Strings.isNullOrEmpty(user.getPassword())
				&& this.password != null) ? this.password : user.password;
		this.sex = (Strings.isNullOrEmpty(user.getSex()) && this.sex != null) ? this.sex : user.sex;
		this.avatarFilePath = (Strings.isNullOrEmpty(user.getAvatarFilePath())
				&& this.avatarFilePath != null) ? this.avatarFilePath : user.avatarFilePath;
		this.country = (Strings.isNullOrEmpty(user.getCountry())
				&& this.country != null) ? this.country : user.country;
		this.city = (Strings.isNullOrEmpty(user.getCity()) && this.city != null) ? this.city : user.city;
		this.status = (Strings.isNullOrEmpty(user.getStatus())
				&& this.status != null) ? this.status: user.status;
		this.turnOnNotif = user.turnOnNotif;
    }

}

