package com.example.netbooks.dto;

import com.example.netbooks.models.Role;

public class UserDataDTO {
	
	  private String username;
	  private String email;
	  private String password;
	  Role role;

	  public String getUsername() {
	    return username;
	  }

	  public void setUsername(String username) {
	    this.username = username;
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

	  public Role getRole() {
	    return role;
	  }

	  public void setRole(Role role) {
	    this.role = role;
	  }

}
