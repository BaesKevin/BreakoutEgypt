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

    private int ballspeed;
    private int lives;
    public static int INFINITE_LIVES = -1;
    private boolean livesRegenBetweenLevels;
    private int pointsPerBlock;
    
    private String name;

    public static final String EASY = "easy";
    public static final String MEDIUM = "medium";
    public static final String HARD = "hard";
    public static final String BRUTAL = "brutal";
    
    
    public Difficulty(String name, int ballspeed, int lives, boolean livesRegenBetweenLevels, int pointsPerBlock) {
        this.name = name;
        this.ballspeed = ballspeed;
        this.lives = lives;
        this.livesRegenBetweenLevels = livesRegenBetweenLevels;
        this.pointsPerBlock = pointsPerBlock;
    }
    
    public String getName() {
        return name;
    }

    public int getBallspeed() {
        return ballspeed;
    }

    public int getLives() {
        return lives;
    }

    public boolean isLivesRegenBetweenLevels() {
        return livesRegenBetweenLevels;
    }

    public int getPointsPerBlock() {
        return pointsPerBlock;
    }

}
