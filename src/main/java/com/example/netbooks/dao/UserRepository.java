package com.example.netbooks.dao;

import com.example.netbooks.models.User;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private static final User SuperAdmin = new User("SuperMail", "SuperAdmin", "Admin", "SuperAdmin");
    private static LinkedList<User> UsersList= new LinkedList<User>();

    public UserRepository() {
        UsersList.add(SuperAdmin);
    }
   
    public boolean save(User User) {
        
        return UsersList.add(User);
    }
    
    public LinkedList<User> GetAllUsers()
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
    
    public User findByUserName(String username) {
        //for(User user : UsersList)
        //{
        //    if(user.getUserName().equals(username)) // ERROR
        //        return user;
        //}
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