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

//    private Paddle paddle;
//    private List<Ball> balls;
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
        super(username, email, password);
        this.levelPackProgressions = new LevelProgressManager();
        this.index = 1;
    }
//
//    public Paddle getPaddle() {
//        return paddle;
//    }
//
//    public void setPaddle(Paddle paddle) {
//        this.paddle = paddle;
//    }

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
}
