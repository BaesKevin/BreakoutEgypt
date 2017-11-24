/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.levelprogression;

import java.util.List;

/**
 *
 * @author snc
 */
public class LevelProgression {
    
    private List<UserLevel> userLevels;
    
    public LevelProgression( List<UserLevel> userLevels) {
        this.userLevels = userLevels;
    }
    
    public int getTotalNumberOfLevels() {
        return userLevels.size();
    }
    
    
    
    
}
