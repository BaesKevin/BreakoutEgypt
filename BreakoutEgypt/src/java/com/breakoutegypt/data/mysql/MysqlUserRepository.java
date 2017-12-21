/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.data.UserRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.exceptions.BreakoutException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Bjarne Deketelaere
 */
public class MysqlUserRepository implements UserRepository {

    private List<User> users;
    private final String SELECT_ALL_USERS = "select * from users";
    private final String INSERT_USER = "insert into users(username,email,`hash`,gold,diamonds) values(?, ?, ?, 0, 0)";
    private final String SELECT_USER_BYEMAIL_BYPASS = "select * from users where email = ?";
    private final String DELETE_USER_BYEMAIL = "delete from users where email = ?;";
    private final String SELECT_USER_BY_ID = "select * from users where userid = ?";
    private final String GET_DEFAULT_PLAYER = "select * from users where username = player and email = player@test.be";

    @Override
    public List<User> getUsers() {
        this.users = new ArrayList<>();
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_ALL_USERS);
                ResultSet rs = prep.executeQuery();) {
            while (rs.next()) {
                int id = rs.getInt("userid");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String hash = rs.getString("hash");
                int gold = rs.getInt("gold");
                int diamonds = rs.getInt("diamonds");
                User user = new User(id, username, email, hash, gold, diamonds, true);
                this.users.add(user);
            }
            return this.users;
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load users", ex);
        }
    }

    @Override
    public void addUser(User user) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(INSERT_USER, PreparedStatement.RETURN_GENERATED_KEYS);) {
            prep.setString(1, user.getUsername());
            prep.setString(2, user.getEmail());
            prep.setString(3, user.getHash());
            prep.executeUpdate();
            try (ResultSet rs = prep.getGeneratedKeys()) {
                int userId = -1;

                if (rs.next()) {
                    userId = rs.getInt(1);
                }

                if (userId < 0) {
                    throw new BreakoutException("Unable to add user to database.");
                }
                user.setUserId(userId);
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't add user", ex);
        }
    }

    @Override
    public User getUserById(int id) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_USER_BY_ID);) {

            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            User user = null;
            while (rs.next()) {

                //userid, username, email, hash, gold, diamonds
                int userid = rs.getInt("userid");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String hash = rs.getString("hash");
                int gold = rs.getInt("gold");
                int diamonds = rs.getInt("diamonds");

                user = new User(userid, username, email, hash, gold, diamonds, true);
            }
            return user;
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't find user", ex);
        }
    }

    @Override
    public boolean alreadyExists(User user) {
        boolean found = false;
        getUsers();
        for (int i = 0; i < this.users.size(); i++) {
            User selectedUser = this.users.get(i);
            if (selectedUser.getEmail().equals(user.getEmail())) {
                found = true;
            }
        }
        return found;
    }

    @Override
    public User getUser(String email, String password) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_USER_BYEMAIL_BYPASS);) {
            prep.setString(1, email);
            try (
                    ResultSet rs = prep.executeQuery();) {
                User user = null;
                while (rs.next()) {
                    if (BCrypt.checkpw(password, rs.getString("hash"))) {
                        int id = rs.getInt("userid");
                        String username = rs.getString("username");
                        String curEmail = rs.getString("email");
                        String curHash = rs.getString("hash");
                        int gold = rs.getInt("gold");
                        int diamonds = rs.getInt("diamonds");
                        user = new User(id, username, curEmail, curHash, gold, diamonds, true);
                    }
                }
                return user;
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't find user", ex);
        }
    }

    @Override
    public void deleteUser(String email) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(DELETE_USER_BYEMAIL);) {
            prep.setString(1, email);
            prep.execute();

        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't find user", ex);
        }
    }

}
