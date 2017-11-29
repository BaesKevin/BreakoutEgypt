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
    private int groupid;

    public FixtureDefConfig(float density, float friction, float restitution) {
        this(density, friction, restitution, false, 0);
    }
    

    public FixtureDefConfig(float density, float friction, float restitution, boolean isSensor) {
        this(density, friction, restitution, isSensor, 0);
    }
    
    public FixtureDefConfig(float density, float friction, float restitution, boolean isSensor, int groupid) {
        this.density = density;
        this.friction = friction;
        this.restitution = restitution;
        this.isSensor = isSensor;
        this.groupid = groupid;
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
        def.filter.groupIndex = groupid;
        return def;
    }
}
