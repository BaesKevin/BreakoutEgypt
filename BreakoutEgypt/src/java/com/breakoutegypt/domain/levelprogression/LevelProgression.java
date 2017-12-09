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
    
    public LevelProgression(){
        this.highestLevelReached = 1;
    }
            
    public void incrementHighestLevelReached(){
        this.highestLevelReached++;
    }

    public int getHighestLevelReached() {
        return highestLevelReached;
    }
}
