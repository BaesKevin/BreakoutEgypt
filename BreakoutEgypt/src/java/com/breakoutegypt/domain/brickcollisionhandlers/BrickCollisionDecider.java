/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.brickcollisionhandlers;

import com.breakoutegypt.domain.effects.Effect;
import com.breakoutegypt.domain.effects.EffectHandler;
import com.breakoutegypt.domain.shapes.bricks.Brick;

/**
 *
 * @author kevin
 */
public class BrickCollisionDecider {

    private EffectHandler collisionEventHandler;
    private Brick brick;

    public BrickCollisionDecider(Brick brick, EffectHandler collisionEventHandler) {
        this.brick = brick;
        this.collisionEventHandler = collisionEventHandler;
    }

    public void handleCollision() {
        System.out.printf("Brick visible: %s Brick breakable: %s", brick.isVisible(), brick.isBreakable());
        if (brick.isVisible() && brick.isBreakable()) {
            
            for(Effect effect : brick.getEffects()){
                effect.accept(collisionEventHandler);
            }
        } else {
            System.out.println("BrickCollisionHandler: collision with switched off brick, going through...");
        }

    }

}
