/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain.shapes;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author kevin
 */
public class RegularBody implements IShape {

       private Shape shape;
    private BodyType bodyType;
    private Body body;
    
    public RegularBody(Shape s){
        this.bodyType = BodyType.REGULAR;
        this.shape = s;
    }
    
    @Override
    public String getName(){
        return shape.getName();
    }
    @Override
    public BodyType getBodyType() {
        return bodyType;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
    
    public void moveTo(float x, float y){
        body.setTransform(new Vec2(x, y), 0);
    }
    
    public Vec2 getPosition(){
        return body.getPosition();
    }
}
