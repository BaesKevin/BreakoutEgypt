/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kevin
 */
public class LevelProgressionRepository {
    private static Map<GameType, LevelProgress> defaultProgressions;
    
    // default levelprogression is not different between difficulties of the same levelpack
    static{
        defaultProgressions = new HashMap();
        
        defaultProgressions.put(GameType.TEST, new LevelProgress(1000));
        defaultProgressions.put(GameType.ARCADE, new LevelProgress(4));
    }
    
    public static LevelProgress getDefault(GameType type){
        return new LevelProgress(defaultProgressions.get(type));
    }
}
