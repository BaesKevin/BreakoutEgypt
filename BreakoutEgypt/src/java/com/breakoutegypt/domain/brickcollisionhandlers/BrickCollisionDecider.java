/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.brickcollisionhandlers;

import com.breakoutegypt.domain.shapes.bricks.Brick;
import static com.breakoutegypt.domain.shapes.bricks.BrickType.EXPLOSIVE;
import static com.breakoutegypt.domain.shapes.bricks.BrickType.SWITCH;
import static com.breakoutegypt.domain.shapes.bricks.BrickType.UNBREAKABLE;
import com.breakoutegypt.domain.shapes.bricks.ExplosiveBrick;
import com.breakoutegypt.domain.shapes.bricks.SwitchBrick;

/**
 *
 * @author kevin
 */
public class BrickCollisionDecider {

    private CollisionEventHandler collisionEventHandler;
    private Brick brick;

    public BrickCollisionDecider(Brick brick, CollisionEventHandler collisionEventHandler) {
        this.brick = brick;
        this.collisionEventHandler = collisionEventHandler;
    }

    public void handleCollision() {
        System.out.printf("Brick visible: %s Brick breakable: %s", brick.isVisible(), brick.isBreakable());
        if (brick.isVisible() && brick.isBreakable()) {
            
            if (brick instanceof ExplosiveBrick) {
                ExplosiveBrick explosiveBrick = (ExplosiveBrick) brick;
                new ExplosiveCollision(collisionEventHandler, explosiveBrick, explosiveBrick.getExplosionRadius()).handleCollsion();
            } 
            else if (brick instanceof SwitchBrick) 
            {
                SwitchBrick switchBrick = (SwitchBrick) brick;
                new SwitchCollision(collisionEventHandler, switchBrick).handleCollsion();
            } else {
                new RegularCollision(collisionEventHandler, brick).handleCollsion();
            }
        } else {
            System.out.println("BrickCollisionHandler: collision with switched off brick, going through...");
        }

    }

}
