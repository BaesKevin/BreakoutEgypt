/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes;

import org.jbox2d.dynamics.Body;

/**
 *
 * @author kevin
 */
public interface IShape {
    
    String getName();
    ShapeDimension getShape();
    void setBody(Body b);
    
    // uitleg visitor en double dispatch van Mattias De Wael
//    static interface ShapeUser {
//     void   doForBrick(Brick b);
//      void doForPaddle(Paddle p);
//     void  doForBall(Ball b);
//     void doForRegular(RegularBody r);
//   }
//    // Visitor + double dispatch
//    void accept(ShapeUser u);
    
}
