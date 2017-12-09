/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.levelprogression;

import com.breakoutegypt.domain.GameType;
import java.util.List;

/**
 *
 * @author snc
 */
public class LevelProgression {
    private int highestLevelReached;
    private int maxLevel;
    
    public LevelProgression(int maxLevel){
        this.highestLevelReached = 1;
        this.maxLevel = maxLevel;
    }
    
    public LevelProgression(LevelProgression p) {
        this.highestLevelReached = p.highestLevelReached;
        this.maxLevel =p.maxLevel;
    }
            
    public void incrementHighestLevelReached(){
        if(highestLevelReached < maxLevel){
            this.highestLevelReached++;
        }
    }

    public int getHighestLevelReached() {
        return highestLevelReached;
    }
}
