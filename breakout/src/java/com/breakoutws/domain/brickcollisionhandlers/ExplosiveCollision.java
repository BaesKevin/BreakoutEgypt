/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain.brickcollisionhandlers;

import com.breakoutws.domain.BreakoutWorld;
import com.breakoutws.domain.shapes.Brick;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author kevin
 */
public class ExplosiveCollision {
private BreakoutWorld world;
    private Brick brick;
    private String shapeName;
    private int rangeToDestroy;

    public ExplosiveCollision(BreakoutWorld world, Brick brick, int rangeToDestroy) {
        this.world = world;
        this.brick = brick;
        this.rangeToDestroy = rangeToDestroy;
    }
    
    public void handleCollsion(){
        world.destroyBricksInRange(brick, rangeToDestroy);
    }
}
