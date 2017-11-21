/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.levelfactories;

import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.Level;

/**
 *
 * @author kevin
 */
public abstract class LevelFactory {
    protected Game game;
    protected int currentLevelId;
    protected int totalLevels;
    protected Level currentLevel;

    public LevelFactory(Game game, int totalLevels) {
        this.currentLevelId = 1;
        this.totalLevels = totalLevels;
        this.game = game;
    }

    public boolean hasNextLevel() {

        return currentLevelId <= totalLevels;
    }

    public Level getCurrentLevel(){
        return currentLevel;
    }
    protected abstract void createCurrentLevel();
    
    public Level getNextLevel() {
        currentLevelId++;
        createCurrentLevel();
        return getCurrentLevel();
    }
    
    public void setCurrentLevel(int id){
        if(1 <= id && id <= totalLevels){
            this.currentLevelId = id;
            createCurrentLevel();
        }
    }

}
