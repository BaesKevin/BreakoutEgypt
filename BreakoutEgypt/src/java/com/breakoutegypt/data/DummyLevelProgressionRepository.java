/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.LevelPack;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.LevelPackProgress;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kevin
 */
public class DummyLevelProgressionRepository implements LevelProgressionRepository {
    private static Map<GameType, LevelProgress> defaultProgressions;
    
    // default levelprogression is not different between difficulties of the same levelpack
    static{
        defaultProgressions = new HashMap();
        
        defaultProgressions.put(GameType.TEST, new LevelProgress(1000));
        defaultProgressions.put(GameType.ARCADE, new LevelProgress(4));
        defaultProgressions.put(GameType.MULTIPLAYER, new LevelProgress(2));
    }
    
    public static LevelProgress getDefault(GameType type){
        return new LevelProgress(defaultProgressions.get(type));
    }

    @Override
    public void addNewLevelProgression(int userid, LevelPack lp, Difficulty d, int highestLevelReached, boolean isCampaign) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void incrementHighestLevelReached(Player p, LevelPack lp, Difficulty d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LevelPackProgress getLevelPackProgress(int playerId, LevelPack lp, Difficulty d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeLevelPackProgress(int playerId, LevelPack lp, Difficulty d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LevelPackProgress> getAllForPlayer(int userId) {
        List<LevelPackProgress> progress = new ArrayList<LevelPackProgress>();
        progress.add(new LevelPackProgress(GameType.TEST, "easy", defaultProgressions.get(GameType.TEST)));
        return progress;
    }

    @Override
    public void initDefaults(int userid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setHighestLevelReached(int levelid, Player p, LevelPack lp, Difficulty difficulty) {
        
        LevelProgress progress = defaultProgressions.get(GameType.TEST);
        
        progress.incrementHighestLevelReached();
    }
}
