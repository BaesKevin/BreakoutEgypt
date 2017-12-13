/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.brickcollisionhandlers;

import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.Projectile;
import com.breakoutegypt.domain.shapes.RegularBody;

/**
 *
 * @author BenDB
 */
public class ProjectilePaddleContact implements Contact {

    private Projectile projectile;
    private Paddle paddle;
    
    public ProjectilePaddleContact(RegularBody body1, RegularBody body2) {
        if(body1 instanceof Projectile){
            projectile = (Projectile) body1;
            paddle = (Paddle) body2;
        } else if (body2 instanceof Projectile) {
            projectile = (Projectile) body2;
            paddle = (Paddle) body1;
        }
    }

    public Projectile getProjectile() {
        return projectile;
    }

    public RegularBody getPaddle() {
        return paddle;
    }
    
    @Override
    public void accept(ContactHandler ch) {
        ch.handle(this);
    }
    
}
