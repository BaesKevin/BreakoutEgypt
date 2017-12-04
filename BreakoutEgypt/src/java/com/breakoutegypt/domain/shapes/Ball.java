/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes;

import com.breakoutegypt.domain.effects.AcidBallPowerUp;
import org.jbox2d.common.Vec2;

/**
 *
 * @author kevin
 */
public class Ball extends RegularBody {

    private AcidBallPowerUp acidBall;

    public void setAcidballPowerup(AcidBallPowerUp acidBall) {
        this.acidBall = acidBall;
    }

    public AcidBallPowerUp getAcidBall() {
        return acidBall;
    }
    
    public Ball(ShapeDimension s) {
        super(s);
    }

    public void setLinearVelocity(float x, float y){
        this.getBody().setLinearVelocity(new Vec2(x,y));
    }
    
    public Vec2 getLinearVelocity(){
        return this.getBody().getLinearVelocity();
    }
}
