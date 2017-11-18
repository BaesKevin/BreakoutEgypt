/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.brickcollisionhandlers;

import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.shapes.Brick;
import com.breakoutegypt.domain.shapes.BrickType;

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
        
        BrickType brickType = brick.getBricktype();
        if (brick.isSwitched()) {
            switch(brickType){
                case EXPLOSIVE:
                    new ExplosiveCollision(level, brick, brick.getExplosionRadius()).handleCollsion();
                    break;
                case UNBREAKABLE:
                    break;
                case SWITCH:
                    new SwitchCollision(level, brick).handleCollsion();
                    break;
                default:
                    new RegularCollision(level, brick).handleCollsion();
                    break;
            }
        } else {
            System.out.println("BrickCollisionHandler: collision with switched off brick, going through...");
        }
            
        
    }

}
