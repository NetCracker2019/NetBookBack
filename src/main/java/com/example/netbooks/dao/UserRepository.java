package com.example.netbooks.dao;

import com.example.netbooks.models.Role;
import com.example.netbooks.models.User;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

	private static final User SuperAdmin = new User(1, "SuperMail", "SuperAdmin", "Admin","Admin", Role.ROLE_SUPER_ADMIN);
	
	
    private static LinkedList<User> UsersList= new LinkedList<User>();

    public UserRepository() {
    	UsersList.add(SuperAdmin);
    }
   
    public boolean save(User User) {
        
        return UsersList.add(User);
    }
    
    public LinkedList<User> getAllUsers()
    {
        return UsersList;
    }

    public User findByEmail(String mail) {
        for(User user : UsersList)
        {
            if(user.getEmail().equals(mail))
                return user;
        }
        return null;
    }
    
    public User findByLogin(String login) {
        for(User user : UsersList)
        {
            if(user.getLogin().equals(login)) 
                return user;
        }
        return null;
    }
    public User findByUserId(long id) {
        for(User user : UsersList)
        {
            if(user.getUserId() == id)
                return user;
        }
        return null;
    }
    
    
    public User findByEmailAndRole(String mail, String role) {
        for(User user : UsersList)
        {
            if(user.getEmail().equals(mail)&& user.getRole().equals(role))
                return user;
        }
        return null;
    }

}