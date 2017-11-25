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
// deze klass zou je kunnen gebruiken om de map besproken in Player.java bij te houden
// elke speler zou dan 1 levelprogression object hebben dat je gebruikt om operaties op de map
// te vereenvoudigen en de gebruikte datastructuur te encapsuleren.
public class LevelProgression {
    
    private List<UserLevel> userLevels;
    
    public LevelProgression( List<UserLevel> userLevels) {
        this.userLevels = userLevels;
    }
    
    public int getTotalNumberOfLevels() {
        return userLevels.size();
    }
    
    
    
    
}
