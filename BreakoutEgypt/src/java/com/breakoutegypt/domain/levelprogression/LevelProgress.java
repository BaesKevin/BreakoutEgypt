/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.levelprogression;

import com.breakoutegypt.domain.GameType;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author snc
 */
public class LevelProgress implements Serializable {
    private int highestLevelReached;
    private int maxLevel;
    
    public LevelProgress(int maxLevel){
        this.highestLevelReached = 1;
        this.maxLevel = maxLevel;
    }
    
    public LevelProgress(LevelProgress p) {
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
