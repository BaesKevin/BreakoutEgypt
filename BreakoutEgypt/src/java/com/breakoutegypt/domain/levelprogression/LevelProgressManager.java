/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.levelprogression;

import com.breakoutegypt.data.LevelProgressionRepository;
import com.breakoutegypt.domain.GameType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class LevelProgressManager {

    private List<LevelPackProgress> packProgresses;

    public LevelProgressManager() {
        packProgresses = new ArrayList();
    }
    
    public LevelPackProgress getProgress(GameType type, GameDifficulty difficulty){
        LevelPackProgress toFind = new LevelPackProgress(type, difficulty);
        
         for (LevelPackProgress p : packProgresses) {
            if(p.equals(toFind)){
                return p;
            }
        }
         
         return null;
    }
    
    public LevelProgress getLevelProgressOrDefault(GameType type, GameDifficulty difficulty){
        LevelPackProgress packProgress = getProgress(type, difficulty);
        LevelProgress progress = LevelProgressionRepository.getDefault(type);
        
        if(packProgress != null)
            progress = packProgress.getLevelProgress();
        
        return progress;
    }

    public void addNewProgression(GameType type, GameDifficulty difficulty) {
        LevelPackProgress newProgress = new LevelPackProgress(type, difficulty);

        if (!levelPackProgressExists(newProgress)) {
            packProgresses.add(newProgress);
        }
    }

    private boolean levelPackProgressExists(LevelPackProgress progress) {
        for (LevelPackProgress p : packProgresses) {
            if (p.equals(progress)) {
                return true;
            }
        }

        return false;
    }

    public void incrementHighestLevel(GameType gameType, GameDifficulty difficulty) {
        getProgress(gameType, difficulty).incrementLevel();
    }
    
    public int getHighestLevelReached(GameType gameType, GameDifficulty difficulty){
        LevelPackProgress p = getProgress(gameType, difficulty);
        
        return p != null? p.getLevelProgress().getHighestLevelReached() : 1; 
    }


}
