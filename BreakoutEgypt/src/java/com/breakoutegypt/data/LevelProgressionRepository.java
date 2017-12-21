/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.LevelPack;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.LevelPackProgress;
import java.util.List;

/**
 *
 * @author BenDB
 */
public interface LevelProgressionRepository {
    
    void addNewLevelProgression(int userid, LevelPack lp, Difficulty d, int highestLevelReached, boolean isCampaign);
    void incrementHighestLevelReached(Player p, LevelPack lp, Difficulty d);
    LevelPackProgress getLevelPackProgress(int playerId, LevelPack lp, Difficulty d);
    void removeLevelPackProgress(int playerId, LevelPack lp, Difficulty d);
    List<LevelPackProgress> getAllForPlayer(int userId);
    public void initDefaults(int userid);

    public void setHighestLevelReached(int levelid, Player p, LevelPack lp, Difficulty difficulty);
}
