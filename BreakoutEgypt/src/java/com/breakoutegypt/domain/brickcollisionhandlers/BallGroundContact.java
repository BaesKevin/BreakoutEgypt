/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.brickcollisionhandlers;

import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.RegularBody;

/**
 *
 * @author kevin
 */
public class BallGroundContact implements Contact{
    Ball outofbounds;
    RegularBody ground;

    public BallGroundContact(RegularBody body1, RegularBody body2) {
        if(body1 instanceof Ball){
            outofbounds = (Ball) body1;
            ground = body2;
        } else {
            outofbounds = (Ball) body2;
            ground = body1;
        }
    }

    public Ball getOutofbounds() {
        return outofbounds;
    }
    
    public RegularBody getGround(){
        return ground;
    }

    @Override
    public void accept(ContactHandler ch) {
        ch.handle(this);
    }
}
