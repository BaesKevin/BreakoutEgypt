/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain.brickcollisionhandlers;

import com.breakoutws.domain.BreakoutWorld;
import com.breakoutws.domain.shapes.Brick;

/**
 *
 * @author snc
 */
public class SwitchCollision {
    private BreakoutWorld world;
    private Brick brick;

    public SwitchCollision(BreakoutWorld world, Brick brick) {
        this.world = world;
        this.brick = brick;
    }
    
    public void handleCollsion(){
        System.out.println("SwitchCollision: Handling switch collision");
        
        for (Brick switchBrick: brick.getSwitchBricks()) {
            switchBrick.toggle();
        }
        //world.destroyBrick(brick);
    }
}
