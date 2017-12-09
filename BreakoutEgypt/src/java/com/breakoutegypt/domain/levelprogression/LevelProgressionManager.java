/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.levelprogression;

import com.breakoutegypt.domain.GameType;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kevin
 */
public class LevelProgressionManager {
    private Map<GameType, LevelProgression> levelProgressions;
    
    public LevelProgressionManager() {
        levelProgressions = new HashMap();
    }
    
    public void addNewProgression(GameType type, LevelProgression progressions){
        // TODO check if levelprogression is in map
        if(levelProgressions.get(type) == null){
            levelProgressions.put(type, progressions);
        }
    }
    
    public void incrementHighestLevel(GameType type){
        getProgression(type).incrementHighestLevelReached();
    }
    
    // TODO clone
    public LevelProgression getProgression(GameType type){
        return levelProgressions.get(type);
    }

    public int getHighestLevelReached(GameType gameType) {
        LevelProgression progression = levelProgressions.get(gameType);
        if(progression == null){
            return 1;
        } else {
            return progression.getHighestLevelReached();
        }
    }
    
}
