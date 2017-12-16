/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author kevin
 */
public class RegularBody {

    private String name;

//    private ShapeDimension shape;
    private Body body;
    protected BodyConfiguration config;

    private float x, y;
    private float originalX, originalY;
    private int width, height;
    //Ball radius in pixels

    public RegularBody(ShapeDimension s) {
//        this.shape = s; // clone
        this.x = s.getPosX();
        this.y = s.getPosY();
        this.originalX = x;
        this.originalY = y;
        this.width = s.getWidth();
        this.height = s.getHeight();
        this.name = s.getName();
    }

    public String getName() { return name;}

    public float getX() {
        if(this.body != null){
            return body.getPosition().x;
        }
        return x; 
    }
    public float getY() { 
        if(this.body != null){
            return body.getPosition().y;
        }
        
        return y; 
    }

    public int getWidth() {  return width; }
    public int getHeight() { return height; }

    public float getOriginalX() { return originalX;  }
    public float getOriginalY() { return originalY; }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void moveTo(float x, float y) {
        body.setTransform(new Vec2(x, y), 0);
    }

    public Vec2 getPosition() {
        return body.getPosition();
    }

    public BodyConfiguration getConfig() {
        return config;
    }

    public void setBox2dConfig(BodyConfiguration config) {
        this.config = config;
    }
    
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder brickkObjectBuilder = Json.createObjectBuilder();
        brickkObjectBuilder.add("name", this.name);
        brickkObjectBuilder.add("x", this.x);
        brickkObjectBuilder.add("y", this.y);
        brickkObjectBuilder.add("width", this.width);
        brickkObjectBuilder.add("height", this.height);
//        brickkObjectBuilder.add("color", String.format("rgb(%d,%d,%d)", this.color.getRed(), this.color.getGreen(), this.color.getBlue()));
        return brickkObjectBuilder;
    }
}
