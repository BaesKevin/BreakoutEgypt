/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.levelfactories;

import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.levelprogression.LevelProgression;
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
    protected LevelProgression defaultProgression;
    protected final static int DEFAULT_STARTING_LEVEL = 1;

    public LevelFactory(Game game, int totalLevels) {
        this(game, totalLevels, totalLevels);
    }

    public LevelFactory(Game game, int totalLevels, int defaultOpenLevels) {
        this.currentLevelId = 1;
        this.totalLevels = totalLevels;
        this.game = game;
        this.defaultOpenLevels = defaultOpenLevels;
        setDefaultLevel(DEFAULT_STARTING_LEVEL);
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

    public boolean hasNextLevel(LevelProgression progression) {
        return hasNextLevel() && currentLevelId < progression.getHighestLevelReached();
    }

    private void setDefaultLevel(int id) {
        this.currentLevelId = id;
        createCurrentLevel();
    }
    
    public int getDefaultOpenLevels(){
        return this.defaultOpenLevels;
    }
    
    public int getTotalLevels(){
        return this.totalLevels;
    }

    public void setCurrentLevel(int id, LevelProgression levelProgression) {
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
