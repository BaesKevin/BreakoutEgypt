/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain.brickcollisionhandlers;

import com.breakoutws.domain.Level;
import com.breakoutws.domain.shapes.Brick;
import com.breakoutws.domain.shapes.BrickType;

/**
 *
 * @author kevin
 */
public class BrickCollisionHandler {
    private Level level;
    private Brick brick;

    
    public BrickCollisionHandler(Brick brick, Level level) {
        this.brick = brick;
        this.level = level;
    }
    public void handleCollision() {
        // TODO do stuff based on bricktype
        BrickType brickType = brick.getBricktype();
        System.out.printf("BrickCollisionHandle: handleCollsion, bricktype: %s", brickType);
        switch(brickType){
            case EXPLOSIVE:
                new ExplosiveCollision(brick, 1, level).handleCollsion();
                break;
            case UNBREAKABLE:
                break;
            default:
                System.out.println("wtf");
                new RegularCollision(level, brick).handleCollsion();
                break;
        }
    }

}
