/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.brickcollisionhandlers;

import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.RegularBody;
import com.breakoutegypt.domain.shapes.bricks.Brick;

/**
 *
 * @author kevin
 */
public class BallBrickContact implements Contact{
    private Brick brick;
    private Ball ball;

    public BallBrickContact(RegularBody body1, RegularBody body2) {
        if(body1 instanceof Ball){
            this.ball = (Ball)body1;
            this.brick = (Brick) body2;
        } else {
            this.ball = (Ball)body2;
            this.brick = (Brick) body1;
        }
    }

    public Brick getBrick() {
        return brick;
    }

    public Ball getBall() {
        return ball;
    }
    
    @Override
    public void accept(ContactHandler ch) {
        ch.handle(this);
    }
}
