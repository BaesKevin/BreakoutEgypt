/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test.persistence;

import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.data.UserRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author snc
 */
public class PersistenceTest {
    
    public PersistenceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testConnection() {
        try {
            Connection c = DbConnection.getConnection();
        } catch (SQLException ex) {
            Assert.fail();
        }
    }
    
    @Test
    public void addingUser() {
        UserRepository repo = Repositories.getUserRepository();
        
        String name = randomName(6);
        String mail = name + "@mail.com";
        String pass = "password";
        int diamonds = 3;
        int gold = 50;
        
        User newUser = new User(name, mail, pass, diamonds, gold);
        
        repo.addUser(newUser);
        
        User fetchedUser = repo.getUser(mail, pass);
        
        Assert.assertTrue(newUser.equals(fetchedUser));
        
        repo.deleteUser(mail);
        
    }
    
    private String randomName(int length) {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random r = new Random(12);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++) {
            int randIndex = r.nextInt(letters.length());
            sb.append(letters.substring(randIndex, randIndex + 1));
        }
        return sb.toString();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
