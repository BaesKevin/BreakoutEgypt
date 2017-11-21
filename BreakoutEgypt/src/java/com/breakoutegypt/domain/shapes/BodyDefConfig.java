/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

/**
 *
 * @author kevin
 */
public class BodyDefConfig {
    private BodyType type;
    private Vec2 position;

    public BodyDefConfig(BodyType type, Vec2 position) {
        this.type = type;
        this.position = position;
    }

    public BodyType getType() {
        return type;
    }

    public Vec2 getPosition() {
        return position;
    }
    
    public BodyDef getBox2dBodyDef(){
        BodyDef def = new BodyDef();
        def.type = type;
        def.position = position;
        
        return def;
    }
    
}
