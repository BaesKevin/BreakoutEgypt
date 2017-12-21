/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test.persistence;

import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.data.UserRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.Score;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.exceptions.BreakoutException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
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

        User newUser = new User(0, name, mail, pass, diamonds, gold);

        repo.addUser(newUser);

        User fetchedUser = repo.getUser(mail, pass);

        Assert.assertTrue(newUser.equals(fetchedUser));

        repo.deleteUser(mail);
    }

    @Test
    public void testAddHighscore() {
        String name = randomName(6);
        String mail = name + "@mail.com";
        String pass = "password";
        int diamonds = 3;
        int gold = 50;

        User newUser = new User(1, name, mail, pass, diamonds, gold);
        Score s = new Score(0, 1, newUser, 123456, Difficulty.EASY, 654321);

        Repositories.getHighscoreRepository().addScore(s);

        List<Score> scores = Repositories.getHighscoreRepository().getScoresByLevel(1, Difficulty.EASY);

        assertTrue(scores.size() > 0);
    }

    private String randomName(int length) {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random r = new Random(12);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randIndex = r.nextInt(letters.length());
            sb.append(letters.substring(randIndex, randIndex + 1));
        }
        return sb.toString();
    }
}
