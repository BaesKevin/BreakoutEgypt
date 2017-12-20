/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.LevelPack;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kevin
 */
public class LevelPackCache implements LevelPackRepository {

    private LevelPackRepository repo;
    private Map<String, LevelPack> levelPacksWithLevels;
    private Map<String, LevelPack> levelPacksWithoutLevels;

    public LevelPackCache(LevelPackRepository repo) {
        this.repo = repo;
        levelPacksWithLevels = new HashMap();
        levelPacksWithoutLevels = new HashMap();
    }

    @Override
    public void add(LevelPack pack) {
        repo.add(pack);
    }

    @Override
    public LevelPack getByName(String name, Game game) {
        LevelPack packFromCache = levelPacksWithLevels.get(name);

        if (packFromCache == null) {
            packFromCache = repo.getByName(name, game);
            
            if(packFromCache != null){
                levelPacksWithLevels.put(name, packFromCache);
            }
            
        }

        return packFromCache;
    }

    @Override
    public LevelPack getByNameWithoutLevels(String name) {
        LevelPack packFromCache = levelPacksWithoutLevels.get(name);

        if (packFromCache == null) {
            packFromCache = repo.getByNameWithoutLevels(name);
            levelPacksWithoutLevels.put(name, packFromCache);
        }

        return packFromCache;
    }

}
