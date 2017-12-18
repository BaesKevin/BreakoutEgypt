/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.User;
import java.util.List;

/**
 *
 * @author Bjarne Deketelaere
 */
public interface UserRepository {
    public List<User> getUsers();
    public void addUser(User user);
    public boolean alreadyExists(User user);
    public User getUser(String email,String password);
    public void deleteUser(String email);
}
