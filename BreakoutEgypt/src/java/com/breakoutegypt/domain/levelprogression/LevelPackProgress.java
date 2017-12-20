/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.levelprogression;

import com.breakoutegypt.data.LevelProgressionRepository;
import com.breakoutegypt.domain.GameType;
import java.io.Serializable;

/**
 *
 * @author kevin
 */
public class LevelPackProgress implements Serializable {
    private GameType type;
    private String difficulty;
    private LevelProgress progress;
    // speler kent enkel zijn eigen voortgang dus hier geen player

    public LevelPackProgress(GameType type, String difficulty) {
        this.type = type;
        this.difficulty = difficulty;
        this.progress = LevelProgressionRepository.getDefault(type);
    }
    
    public LevelProgress getLevelProgress(){
        return new LevelProgress(progress);
    }
    
    public void incrementLevel(){
        this.progress.incrementHighestLevelReached();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final LevelPackProgress other = (LevelPackProgress) obj;
        
        return this.type == other.type && this.difficulty == other.difficulty;
    }
    
    
}
