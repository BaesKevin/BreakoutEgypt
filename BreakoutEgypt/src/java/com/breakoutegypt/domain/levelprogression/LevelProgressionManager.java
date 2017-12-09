/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.levelprogression;

import com.breakoutegypt.data.LevelProgressionRepository;
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
    
    public void addNewProgression(GameType type){
        // TODO check if levelprogression is in map
        if(levelProgressions.get(type) == null){
            levelProgressions.put(type, LevelProgressionRepository.getDefault(type));
        }
    }
    
    public void incrementHighestLevel(GameType type){
        getProgressionOrDefault(type).incrementHighestLevelReached();
    }
    
    // TODO clone
    public LevelProgression getProgressionOrDefault(GameType type){
        LevelProgression prog = levelProgressions.get(type);
        
        if(prog == null){
            prog = LevelProgressionRepository.getDefault(type);
        }
        
        return prog;
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
