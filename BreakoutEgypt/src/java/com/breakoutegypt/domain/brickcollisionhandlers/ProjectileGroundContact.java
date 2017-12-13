/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.brickcollisionhandlers;

import com.breakoutegypt.domain.shapes.Projectile;
import com.breakoutegypt.domain.shapes.RegularBody;

/**
 *
 * @author BenDB
 */
public class ProjectileGroundContact implements Contact {

    private Projectile projectile;
    private RegularBody ground;
    
    public ProjectileGroundContact(RegularBody body1, RegularBody body2) {
        if(body1 instanceof Projectile){
            projectile = (Projectile) body1;
            ground = body2;
        } else if (body2 instanceof Projectile) {
            projectile = (Projectile) body2;
            ground = body1;
        }
    }

    public Projectile getProjectile() {
        return projectile;
    }

    public RegularBody getGround() {
        return ground;
    }
    
    @Override
    public void accept(ContactHandler ch) {
        ch.handle(this);
    }
    
}
