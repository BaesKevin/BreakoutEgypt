/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.levelprogression.LevelProgression;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kevin
 */
public class LevelProgressionRepository {
    private static Map<GameType, LevelProgression> defaultProgressions;
    
    static{
        defaultProgressions = new HashMap();
        
        defaultProgressions.put(GameType.TEST, new LevelProgression(1000));
        defaultProgressions.put(GameType.ARCADE, new LevelProgression(4));
    }
    
    public static LevelProgression getDefault(GameType type){
        return new LevelProgression(defaultProgressions.get(type));
    }
}
