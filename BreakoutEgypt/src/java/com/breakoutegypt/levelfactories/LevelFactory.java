/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.levelfactories;

import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import com.breakoutegypt.exceptions.BreakoutException;

/**
 *
 * @author kevin
 */
public abstract class LevelFactory {

    protected Game game;
    protected int currentLevelId;
    protected int totalLevels;
    protected Level currentLevel;
    protected int defaultOpenLevels;
    protected LevelProgress defaultProgression;
    protected final static int DEFAULT_STARTING_LEVEL = 1;
    
    public LevelFactory(Game game, int totalLevels) {
        this(game, totalLevels, totalLevels);
    }

    public LevelFactory(Game game, int totalLevels, int defaultOpenLevels) {
        this.currentLevelId = 1;
        this.totalLevels = totalLevels;
        this.game = game;
        this.defaultOpenLevels = defaultOpenLevels;
    }

    public boolean hasNextLevel() {

        return currentLevelId <= totalLevels;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    protected abstract void createCurrentLevel();

    public Level getNextLevel() {
        currentLevelId++;// TODO don't do this
        createCurrentLevel();
        return getCurrentLevel();
    }

    public boolean hasNextLevel(LevelProgress progression) {
        return hasNextLevel() && currentLevelId < progression.getHighestLevelReached();
    }

    public int getDefaultOpenLevels(){
        return this.defaultOpenLevels;
    }
    
    public int getTotalLevels(){
        return this.totalLevels;
    }

    public void setCurrentLevel(int id, LevelProgress levelProgression) {
        boolean levelExists = 1 <= id && id <= totalLevels;
        boolean levelIsOpenByDefault = id <= defaultOpenLevels;
        boolean levelExistsAndIsOpen = levelExists && levelIsOpenByDefault;

        boolean playerReachedLevel = id <= levelProgression.getHighestLevelReached();
        boolean levelExistsAndPlayerReachedLevel = levelExists && playerReachedLevel;

        if (levelExistsAndIsOpen || levelExistsAndPlayerReachedLevel) {
            this.currentLevelId = id;
            createCurrentLevel();
        } else {
            throw new BreakoutException("Level not unlocked");
        }
    }

}
