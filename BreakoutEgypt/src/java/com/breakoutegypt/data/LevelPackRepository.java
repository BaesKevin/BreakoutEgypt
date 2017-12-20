/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.LevelPack;

/**
 *
 * @author kevin
 */
public interface LevelPackRepository {
    void add(LevelPack pack);
    LevelPack getByName(String name, Game game);
    LevelPack getByNameWithoutLevels(String name);
}
