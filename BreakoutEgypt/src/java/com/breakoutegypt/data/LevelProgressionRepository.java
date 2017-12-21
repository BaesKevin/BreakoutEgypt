/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.LevelPack;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.LevelPackProgress;

/**
 *
 * @author BenDB
 */
public interface LevelProgressionRepository {
    
    void addNewLevelProgression(Player p, LevelPack lp, Difficulty d, int highestLevelReached, boolean isCampaign);
    void incrementHighestLevelReached(int levelProgressionId);
    LevelPackProgress getLevelPackProgress(int playerId, LevelPack lp, Difficulty d);
    
}
