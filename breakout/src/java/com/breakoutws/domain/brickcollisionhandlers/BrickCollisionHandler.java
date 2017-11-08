/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain.brickcollisionhandlers;

import com.breakoutws.domain.BreakoutWorld;
import com.breakoutws.domain.shapes.Brick;
import com.breakoutws.domain.shapes.BrickType;
import org.jbox2d.dynamics.Fixture;

/**
 *
 * @author kevin
 */
public class BrickCollisionHandler {

    private Fixture fixtureA, fixtureB;
    private BreakoutWorld world;

    private Brick brick;

    
    public BrickCollisionHandler(Fixture fixtureA, Fixture fixtureB, BreakoutWorld world, Brick brick) {
        this.fixtureA = fixtureA;
        this.fixtureB = fixtureB;
        this.world = world;
        this.brick = brick;
    }
    public void handleCollision() {
        // TODO do stuff based on bricktype
        BrickType brickType = brick.getBricktype();
        System.out.printf("BrickCollisionHandle: handleCollsion, bricktype: %s", brickType);
        switch(brickType){
            case EXPLOSIVE:
                new ExplosiveCollision(world, fixtureA.getBody(), brick.getShape().getName(), 1).handleCollsion();
                break;
            case UNBREAKABLE:
                break;
            default:
                new RegularCollision(world, fixtureA.getBody(), brick.getShape().getName()).handleCollsion();
                break;
        }
    }

}
