/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.levelprogression;

import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.domain.LevelPack;
import com.breakoutegypt.domain.Player;
import java.io.Serializable;

/**
 *
 * @author snc
 */
public class LevelProgress implements Serializable {

    private int highestLevelReached;
    private int maxLevel;

    public LevelProgress(int maxLevel) {
        this.highestLevelReached = 1;
        this.maxLevel = maxLevel;
    }
    
    public LevelProgress(int maxLevel, int highestLevelReached) {
        this.maxLevel = maxLevel;
        this.highestLevelReached = highestLevelReached;
    }

    public LevelProgress(LevelProgress p) {
        this.highestLevelReached = p.highestLevelReached;
        this.maxLevel = p.maxLevel;
    }

    public void incrementHighestLevelReached() {
        if (highestLevelReached < maxLevel) {
            this.highestLevelReached++;
        }
    }
    
    public void incrementHighestLevelReached(Player p, LevelPack lp, Difficulty d) {
        if (highestLevelReached < maxLevel) {
            this.highestLevelReached++;
            Repositories.getLevelProgressionRepository().incrementHighestLevelReached(p, lp, d);
        }
    }

    public int getHighestLevelReached() {
        return highestLevelReached;
    }

    void sethighestLevelReached(int playerid, Player p, LevelPack lp, Difficulty difficulty) {
        Repositories.getLevelProgressionRepository().setHighestLevelReached(playerid, p, lp, difficulty);
    }
}
