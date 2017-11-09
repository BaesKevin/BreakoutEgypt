/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakout.ws.upgrades;

import com.breakoutws.domain.Level;
import com.breakoutws.domain.effects.AddBallEffect;
import java.util.List;

/**
 *
 * @author kevin
 */
public class MoreBallsUpgrade {
    private List<AddBallEffect> effects;
    private Level level;
    
    public MoreBallsUpgrade(int amount, Level level){
        this.level = level;
        for(int i = 0; i  < amount; i++){
            effects.add(new AddBallEffect());
        }
    }
    
    public void perform(){
        for(AddBallEffect e : effects){
            level.handleAddBalleffect();
        }
    }
    
}
