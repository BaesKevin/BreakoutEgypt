/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Bjarne Deketelaere
 */
public class StaticUserRepository implements UserRepository {

    private static final StaticUserRepository REPO = new StaticUserRepository();
    private List<User> users;

    private StaticUserRepository() {
        this.users = new ArrayList<>();
    }

    public static StaticUserRepository getInstance() {
        return REPO;
    }

    @Override
    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public User getUser(String email,String password){
        for(User selectedUser:this.users){
            if(selectedUser.getEmail().equals(email) && BCrypt.checkpw(password, selectedUser.getHash())){
                return selectedUser;
            }
        }
        return null;
    }

    @Override
    public boolean alreadyExists(User user) {
        boolean found = false;
        
        for (User u : users) {
            if (u.getEmail().equals(user.getEmail())) {
                found = true;
            }
        }
        return found;
    }

    @Override
    public void deleteUser(String email) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User getUserById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
