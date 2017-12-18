/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes;

/**
 *
 * @author BenDB
 */
public class Projectile extends RegularBody {
    
    public Projectile(ShapeDimension s) {
        super(s);
        BodyConfiguration projectileBodyConfig = BodyConfigurationFactory.getInstance().createProjectileConfig(s);
        setBox2dConfig(projectileBodyConfig);
    }
    
}
