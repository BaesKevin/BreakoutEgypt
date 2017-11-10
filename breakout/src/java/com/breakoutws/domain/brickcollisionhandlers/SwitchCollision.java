/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain.brickcollisionhandlers;

import com.breakoutws.domain.Level;
import com.breakoutws.domain.effects.ToggleEffect;
import com.breakoutws.domain.shapes.Brick;

/**
 *
 * @author snc
 */
public class SwitchCollision {
    private Level level;
    private Brick brick;

    public SwitchCollision(Level level, Brick brick) {
        this.level = level;
        this.brick = brick;
    }
    
    public void handleCollsion(){
        System.out.println("SwitchCollision: Handling switch collision");
        
        level.handleToggleEffect(new ToggleEffect(brick.getSwitchBricks()));
        
      
        //world.destroyBrick(brick);
    }
}
