/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.LevelPack;
import com.breakoutegypt.domain.levelprogression.LevelProgress;

/**
 *
 * @author kevin
 * levelpackrepositories  should not add levels to the pack, this is just to get information
 */
public interface LevelPackRepository {
    void add(LevelPack pack);
    LevelPack getByName(String name);
    LevelPack getById(int levelpackid);
}
