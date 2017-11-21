/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes;

import org.jbox2d.dynamics.FixtureDef;

/**
 *
 * @author kevin
 */
public class FixtureDefConfig {
    private float density, friction, restitution;
    private boolean isSensor;

    public FixtureDefConfig(float density, float friction, float restitution) {
        this(density, friction, restitution, false);
    }

    public FixtureDefConfig(float density, float friction, float restitution, boolean isSensor) {
        this.density = density;
        this.friction = friction;
        this.restitution = restitution;
        this.isSensor = isSensor;
    }
    
    

    public float getDensity() {
        return density;
    }

    public float getFriction() {
        return friction;
    }

    public float getRestitution() {
        return restitution;
    }
    
    public boolean isSensor(){
        return isSensor;
    }
    
    public FixtureDef getBox2dFixtureDef(){
        FixtureDef def = new FixtureDef();
        
        def.density = density;
        def.friction = friction;
        def.restitution = restitution;
        def.isSensor = isSensor;
        
        return def;
    }
}
