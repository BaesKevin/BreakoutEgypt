/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.levelprogression;

import com.breakoutegypt.domain.BreakoutWorld;

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
    private double percentageOfPowerups, percentageOfPowerdowns;
    private int powerupTime, powerdownTime;

    private String name;

    public static final String EASY = "easy";
    public static final String MEDIUM = "medium";
    public static final String HARD = "hard";
    public static final String BRUTAL = "brutal";

    public Difficulty(String name, int ballspeed, int lives, boolean livesRegenBetweenLevels, int pointsPerBlock, int percentageOfPowerups, int powerupTime) {
        this.name = name;
        this.ballspeed = ballspeed;
        this.lives = lives;
        this.livesRegenBetweenLevels = livesRegenBetweenLevels;
        this.pointsPerBlock = pointsPerBlock;
        this.percentageOfPowerups = (float) percentageOfPowerups / 100;
        this.percentageOfPowerdowns = (float) (100 - percentageOfPowerups) / 100;
        this.powerupTime = powerupTime * (int) Math.round(Math.pow(BreakoutWorld.TIMESTEP_DEFAULT, -1));
        this.powerdownTime = (20 - powerupTime) * (int) Math.round(Math.pow(BreakoutWorld.TIMESTEP_DEFAULT, -1));
        if (name.equals(BRUTAL)) {
            powerdownTime = 15 * (int) Math.round(Math.pow(BreakoutWorld.TIMESTEP_DEFAULT, -1));;
        }
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

    public double getPercentageOfPowerups() {
        return percentageOfPowerups;
    }

    public double getPercentageOfPowerdowns() {
        return percentageOfPowerdowns;
    }

    public int getPowerupTime() {
        return powerupTime;
    }

    public int getPowerdownTime() {
        return powerdownTime;
    }

}
