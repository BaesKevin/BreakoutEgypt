/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.brickcollisionhandlers;

import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.RegularBody;

/**
 *
 * @author BenDB
 */
public class BallPaddleContact implements Contact {

    private Ball ball;
    private Paddle paddle;
    
    public BallPaddleContact(RegularBody s1, RegularBody s2) {
        if (s1 instanceof Ball) {
            ball = (Ball) s1;
            paddle = (Paddle) s2;
        } else {
            ball = (Ball) s2;
            paddle = (Paddle) s1;
        }
    }

    @Override
    public void accept(ContactHandler ch) {
        ch.handle(this);
    }
    
}
