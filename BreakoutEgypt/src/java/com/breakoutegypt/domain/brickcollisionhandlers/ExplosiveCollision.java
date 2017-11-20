/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.brickcollisionhandlers;

import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.shapes.bricks.ExplosiveBrick;

/**
 *
 * @author kevin
 */
public class ExplosiveCollision {

    private CollisionEventHandler collisionEventHandler;
    private ExplosiveBrick brick;
    private String shapeName;
    private int rangeToDestroy;

    public ExplosiveCollision(CollisionEventHandler collisionEventHandler, ExplosiveBrick brick, int rangeToDestroy) {
        this.collisionEventHandler = collisionEventHandler;
        this.brick = brick;
        this.rangeToDestroy = rangeToDestroy;
    }

    public void handleCollsion() {
        collisionEventHandler.handleExplosiveEffect(new ExplosiveEffect(brick, rangeToDestroy));
    }
}
