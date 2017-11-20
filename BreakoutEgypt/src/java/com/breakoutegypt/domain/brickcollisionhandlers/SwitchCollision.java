/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.brickcollisionhandlers;

import com.breakoutegypt.domain.effects.ToggleEffect;
import com.breakoutegypt.domain.shapes.bricks.SwitchBrick;

/**
 *
 * @author snc
 */
public class SwitchCollision {
    private CollisionEventHandler collisionEventHandler;
    private SwitchBrick brick;

    public SwitchCollision(CollisionEventHandler collisionEventHandler, SwitchBrick brick) {
        this.collisionEventHandler = collisionEventHandler;
        this.brick = brick;
    }
    
    public void handleCollsion(){
        System.out.println("SwitchCollision: Handling switch collision");
        
        collisionEventHandler.handleToggleEffect(new ToggleEffect(brick.getSwitchBricks()));
    }
}
