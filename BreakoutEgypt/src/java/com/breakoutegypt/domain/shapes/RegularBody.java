/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author kevin
 */
public class RegularBody {

    private ShapeDimension shape;
    private Body body;
    private BodyConfiguration config;
    
    public RegularBody(ShapeDimension s) {
        this.shape = s; // clone
    }

    public String getName() {
        return shape.getName();
    }

    public ShapeDimension getShape() {
        return shape;
    }

    public void setShape(ShapeDimension shape) {
        this.shape = shape;
    }

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
}
