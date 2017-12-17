/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.UserRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.exceptions.BreakoutException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;


/**
 *
 * @author Bjarne Deketelaere
 */
public class MysqlUserRepository implements UserRepository{
    private List<User> users;
    private final String SELECT_ALL_USERS = "select * from users";
    private final String INSERT_USER = "insert into users(username,email,password,gold,diamonds) values(?, ?, ?, ?, ?)";
    private final String SELECT_USER_BYEMAIL_BYPASS = "select * from users where email = ?";
    
    @Override
    public List<User> getUsers() {
        this.users=new ArrayList<>();
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(SELECT_ALL_USERS);
                ResultSet rs=prep.executeQuery();
                ){
            while(rs.next()){
                String username=rs.getString("username");
                String email=rs.getString("email");
                String password=rs.getString("password");
                int gold=rs.getInt("gold");
                int diamonds=rs.getInt("diamonds");
                User user=new User(username,email,password,gold,diamonds);
                this.users.add(user);
            }
            return this.users;
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load users",ex);
        }
    }

    @Override
    public void addUser(User user) {
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(INSERT_USER,PreparedStatement.RETURN_GENERATED_KEYS);
                ){
            prep.setString(1, user.getUsername());
            prep.setString(2, user.getEmail());
            prep.setString(3, user.getPassword());
            prep.setInt(4, user.getGold());
            prep.setInt(5, user.getDiamonds());
            prep.executeUpdate();
            try(ResultSet rs = prep.getGeneratedKeys())
            {
                int userId = -1;
                
                if (rs.next())
                {
                    userId = rs.getInt(1);
                }
                
                if (userId < 0)
                {
                    throw new BreakoutException("Unable to add user to database.");
                }
                user.setUserId(userId);
            }
            
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't add user",ex);
        }
    }

    @Override
    public boolean alreadyExists(User user) {
        boolean found=false;
        for(int i=0;i<this.users.size();i++){
            User selectedUser=this.users.get(i);
            if(selectedUser.equals(user)){
                found=true;
            }   
        }
        return found;
    }

    @Override
    public User getUser(String email, String password) {
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(SELECT_USER_BYEMAIL_BYPASS);
                ){
            prep.setString(1, email);
            try(
                    ResultSet rs=prep.executeQuery();
                    ){
                User user=null;
                while(rs.next()){
                    if(BCrypt.checkpw(password, rs.getString("password"))){
                        String username=rs.getString("username");
                        String curEmail=rs.getString("email");
                        String curPassword=rs.getString("password");
                        int gold=rs.getInt("gold");
                        int diamonds=rs.getInt("diamonds");
                        user=new User(username,curEmail,curPassword,gold,diamonds);
                    }   
                }
                return user;
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't find user",ex);
        }
    }
    
}
