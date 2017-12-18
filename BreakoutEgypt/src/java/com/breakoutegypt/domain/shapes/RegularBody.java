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

    protected ShapeDimension dimension;
    private Body body;
    protected BodyConfiguration config;
    private int playerIndex;
    
    public RegularBody(ShapeDimension s) {
        this.dimension = s; // clone
        playerIndex = 1;
    }

    public String getName() { return dimension.getName();}

    public float getX() {
        if(this.body != null){
            return body.getPosition().x;
        }
        return dimension.getPosX(); 
    }
    public float getY() { 
        if(this.body != null){
            return body.getPosition().y;
        }
        
        return dimension.getPosY(); 
    }

    public int getWidth() {  return dimension.getWidth(); }
    public int getHeight() { return dimension.getHeight(); }

    public float getOriginalX() { return dimension.getPosX();  }
    public float getOriginalY() { return dimension.getPosY(); }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public synchronized void moveTo(float x, float y) {
        if(! body.getWorld().isLocked()){
            body.setTransform(new Vec2(x, y), 0);
        }
    }

    public Vec2 getPosition() {
        return body.getPosition();
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }    
    

    //    // uitleg visitor en double dispatch van Mattias De Wael
////    static interface ShapeUser {
////     void   doForBrick(Brick b);
////      void doForPaddle(Paddle p);
////     void  doForBall(Ball b);
////     void doForRegular(RegularBody r);
////   }
////    // Visitor + double dispatch
////    void accept(ShapeUser u);

//    @Override
//    public void accept(ShapeUser u) {
//        u.doForRegular(this);
//    }

    public BodyConfiguration getConfig() {
        return config;
    }

    public void setBox2dConfig(BodyConfiguration config) {
        this.config = config;
    }
    
    public ShapeDimension getInitialShape(){
        return dimension;
    }
    
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder brickkObjectBuilder = Json.createObjectBuilder();
        brickkObjectBuilder.add("name", getName());
        brickkObjectBuilder.add("x", dimension.getPosX());
        brickkObjectBuilder.add("y", dimension.getPosY());
        brickkObjectBuilder.add("width", dimension.getWidth());
        brickkObjectBuilder.add("height", dimension.getHeight());
//        brickkObjectBuilder.add("color", String.format("rgb(%d,%d,%d)", this.color.getRed(), this.color.getGreen(), this.color.getBlue()));
        return brickkObjectBuilder;
    }
}
