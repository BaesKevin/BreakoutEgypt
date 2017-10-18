/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import java.awt.Color;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 *
 * @author kevin
 */
public class Ball implements Box2dObject{
    //X and Y position of the ball in JBox2D world
    private float posX;
    private float posY;
     
    //Ball radius in pixels
    private int radius;
    private Color color;
    private Body body;
    
    public Ball(float posX, float posY, int radius, Color color, World world){
        this.posX = posX;
        this.posY = posY;
        this.radius = radius;
        this.color = color;
        body = create(world);
    }
    
    private Body create(World world){
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(posX, posY);
//        bd.linearVelocity.x = -100;
        bd.linearVelocity.y = -100;
        CircleShape cs = new CircleShape();
        cs.m_radius = radius;  //We need to convert radius to JBox2D equivalent
        
        // Create a fixture for ball
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 0.6f;
        fd.friction = 0f;        
        fd.restitution = 1f;
        
        /**
        * Virtual invisible JBox2D body of ball. Bodies have velocity and position. 
        * Forces, torques, and impulses can be applied to these bodies.
        */
        Body body = world.createBody(bd);
        body.createFixture(fd);
        body.setUserData(this);
        return body;
    }
    
    public Body getBody(){
        return body;
    }

    @Override
    public String getJsonKey() {
        return "ball";
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder ballObjectBuilder = Json.createObjectBuilder();
        ballObjectBuilder.add("x", Math.round(this.getBody().getPosition().x));
        ballObjectBuilder.add("y", Math.round(this.getBody().getPosition().y));
        ballObjectBuilder.add("radius", this.radius);
        ballObjectBuilder.add("color", String.format("rgb(%d,%d,%d)", this.color.getRed(), this.color.getGreen(), this.color.getBlue()));
        return ballObjectBuilder.build();
    }
    
    
}
