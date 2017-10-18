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
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 *
 * @author kevin
 */
public class Paddle implements Box2dObject{
     //X and Y position of the ball in JBox2D world
    private float posX;
    private float posY;
    private int width, height;
    //Ball radius in pixels
    private Color color;
    private Body body;
    
    public Paddle(float posX, float posY, int width, int height, Color color, World world){
        this.posX = posX;
        this.posY = posY;
        this.color = color;
        this.width = width;
        this.height = height;
        body = create(world);
    }
    
    private Body create(World world){
        BodyDef bd = new BodyDef();
        bd.type = BodyType.KINEMATIC;
        bd.position.set(posX, posY);
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(width / 2, height /2);
        
        // Create a fixture for ball
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
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

    public void move(float x, float y){
        body.setTransform(new Vec2(x,y), 0);
    }
    
    @Override
    public String getJsonKey() {
        return "paddle";
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder brickkObjectBuilder = Json.createObjectBuilder();
        brickkObjectBuilder.add("x", Math.round(this.getBody().getPosition().x));
        brickkObjectBuilder.add("y", Math.round(this.getBody().getPosition().y));
        brickkObjectBuilder.add("width", this.width);
        brickkObjectBuilder.add("height", this.height);
        brickkObjectBuilder.add("color", String.format("rgb(%d,%d,%d)", this.color.getRed(), this.color.getGreen(), this.color.getBlue()));
        return brickkObjectBuilder.build();
    }
    
    
    
}