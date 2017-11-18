/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.brickcollisionhandlers;

import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.shapes.Brick;

/**
 *
 * @author kevin
 */
public class ExplosiveCollision {

    private Level level;
    private Brick brick;
    private String shapeName;
    private int rangeToDestroy;

    public ExplosiveCollision(Level level, Brick brick, int rangeToDestroy) {
        this.level = level;
        this.brick = brick;
        this.rangeToDestroy = rangeToDestroy;
    }

    public void handleCollsion() {
        level.handleExplosiveEffect(new ExplosiveEffect(brick, rangeToDestroy));
    }
}
