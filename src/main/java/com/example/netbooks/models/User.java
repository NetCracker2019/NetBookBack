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
		this.name = ((user.name == null || user.name.equals("")) && this.name != null) ? this.name : user.name;
		this.email = ((user.email == null || user.email.equals("")) && this.email != null) ? this.email : user.email;
		this.password = ((user.password == null || user.password.equals(""))
				&& this.password != null) ? this.password : user.password;
		this.sex = ((user.sex == null || user.sex.equals("")) && this.sex != null) ? this.sex : user.sex;
		this.avatarFilePath = ((user.avatarFilePath == null || user.avatarFilePath.equals(""))
				&& this.avatarFilePath != null) ? this.avatarFilePath : user.avatarFilePath;
		this.country = ((user.country == null || user.country.equals(""))
				&& this.country != null) ? this.country : user.country;
		this.city = ((user.city == null || user.city.equals("")) && this.city != null) ? this.city : user.city;
		this.status = ((user.status == null || user.status.equals(""))
				&& this.status != null) ? this.status: user.status;
		this.turnOnNotif = user.turnOnNotif;
    }
}