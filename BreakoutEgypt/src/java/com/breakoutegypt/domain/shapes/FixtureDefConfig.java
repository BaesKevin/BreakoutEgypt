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
    private int maskBits;
    private int categoryBits;

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
    
    public FixtureDefConfig(float density, float friction, float restitution, boolean isSensor, int categoryBits, int maskBits) {
        this.density = density;
        this.friction = friction;
        this.restitution = restitution;
        this.isSensor = isSensor;
        this.maskBits = maskBits;
        this.categoryBits = categoryBits;
    }
    
    public void setMaskBits(int bits) {
        maskBits = bits;
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
        def.filter.categoryBits = categoryBits == 0 ? 0x0001 : categoryBits;
        def.filter.maskBits = maskBits == 0 ? 0x0001 : maskBits;
        return def;
    }
}
