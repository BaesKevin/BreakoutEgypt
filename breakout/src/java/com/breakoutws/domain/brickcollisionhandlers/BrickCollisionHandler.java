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
    private BreakoutWorld world;
    private Brick brick;

    
    public BrickCollisionHandler(BreakoutWorld world, Brick brick) {
        this.world = world;
        this.brick = brick;
    }
    public void handleCollision() {
        // TODO do stuff based on bricktype
        BrickType brickType = brick.getBricktype();
        System.out.printf("BrickCollisionHandle: handleCollsion, bricktype: %s", brickType);
        System.out.println("BrickCollisionHandler: isSwitched: " + brick.isSwitched());
        if (brick.isSwitched()) {
            switch(brickType){
                case EXPLOSIVE:
                    System.out.println("BrickCollisionHandler: explosive collision");
                    new ExplosiveCollision(world, brick, 1).handleCollsion();
                    break;
                case UNBREAKABLE:
                    System.out.println("BrickCollisionHandler: unbreakable collision");
                    break;
                case SWITCH:
                    System.out.println("BrickCollisionHandler: switch Collision");
                    new SwitchCollision(world, brick).handleCollsion();
                    break;
                default:
                    System.out.println("BrickCollisionHandler: regular collision");
                    new RegularCollision(world, brick).handleCollsion();
                    break;
            }
        } else {
            System.out.println("BrickCollisionHandler: collision with switched off brick, going through...");
        }
            
        
    }

}
