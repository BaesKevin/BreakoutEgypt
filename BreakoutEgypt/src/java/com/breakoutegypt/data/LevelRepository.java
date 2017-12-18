/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.Level;
import java.util.List;

/**
 *
 * @author Bjarne Deketelaere
 */
public interface LevelRepository {
    public List<Level> getLevels();
    public Level getLevelById(int id);
    public void addLevel(Level level);
    public void removeLevel(Level level);
}
