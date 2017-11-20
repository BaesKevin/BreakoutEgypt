/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.brickcollisionhandlers;

import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.shapes.bricks.Brick;

/**
 *
 * @author kevin
 */
public class RegularCollision {
    private CollisionEventHandler collisionEventHandler;
    private Brick brick;

    public RegularCollision(CollisionEventHandler collisionEventHandler, Brick brick) {
        this.collisionEventHandler = collisionEventHandler;
        this.brick = brick;
    }
    
    public void handleCollsion(){
        System.out.println("RegularCollision: Handling regular collision");
        collisionEventHandler.handleExplosiveEffect(new ExplosiveEffect(brick, 0));
    }
}
