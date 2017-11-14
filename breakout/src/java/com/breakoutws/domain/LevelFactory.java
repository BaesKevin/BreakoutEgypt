/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

/**
 *
 * @author kevin
 */
public abstract class LevelFactory {
    protected Game game;
    protected int currentLevel;
    protected int totalLevels;

    public LevelFactory(Game game) {
        this.currentLevel = 1;
        this.totalLevels = 2;
        this.game = game;
    }

    public boolean hasNextLevel() {

        return currentLevel <= totalLevels;
    }

    public abstract Level getCurrentLevel();

    public Level getNextLevel() {
        System.out.println("LevelFactory: Get next level");
        currentLevel++;
        return getCurrentLevel();
    }
}
