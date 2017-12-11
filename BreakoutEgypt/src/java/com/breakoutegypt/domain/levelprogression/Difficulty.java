/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.levelprogression;

/**
 *
 * @author kevin
 */
public class Difficulty {
    public static final String[] DIFFICULTIES = new String[] { "easy", "medium", "hard", "brutal"};
    
    private int ballspeed;
    private String name;
    
    public Difficulty(String name, int ballspeed){
        this.name = name;
        this.ballspeed = ballspeed;
    }

    public String getName() {
        return name;
    }
    
    public int getBallspeed() {
        return ballspeed;
    }
    
    
}
