/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.connectionmanagement.PlayerConnection;
import com.breakoutegypt.domain.levelprogression.LevelProgressManager;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class Player extends User implements Serializable {

    private transient PlayerConnection conn;
    private LevelProgressManager levelPackProgressions;
    private int index;
    private int lives;
    
    public Player(String username) {
        this(username, "", "");
    }

    public Player(String email, String password) {
        this("", email, password);
    }
    
    public Player(String username, String email, String password) {
        this(0, username, email, password, new LevelProgressManager());
    }

    public Player(int id, String username, String email, String password, LevelProgressManager lpm) {
        super(username, email, password);
        super.setUserId(id);
        this.levelPackProgressions = lpm;
        this.index = 1;
    }

    public PlayerConnection getConnection() {
        return conn;
    }

    public void setConnection(PlayerConnection conn) {
        this.conn = conn;
    }

    public LevelProgressManager getProgressions() {
        return levelPackProgressions;
    }

    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index){
        this.index = index;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    void decreaseLives() {
        this.lives--;
    }

    public boolean noLivesLeft() {
        return lives == 0;
    }
    
    public User getUser() throws CloneNotSupportedException {
        return (User)super.clone();
    }
}
