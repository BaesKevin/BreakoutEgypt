/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes;

import com.breakoutegypt.domain.powers.AcidBallPowerUp;
import java.util.concurrent.atomic.AtomicInteger;
import org.jbox2d.common.Vec2;

/**
 *
 * @author kevin
 */
public class Ball extends RegularBody {
    
    private int ballId=0;
    private AcidBallPowerUp acidBall;
    private boolean decoy;
    private boolean startingBall;
    private static AtomicInteger ballIdentifier = new AtomicInteger(1);
    
    private float xSpeed=0;
    private float ySpeed=0;
    public Ball(ShapeDimension s) {
        this(s, false);
    }
    public Ball(ShapeDimension s, float xSpeed, float ySpeed){
        this(s,false);
        this.xSpeed=xSpeed;
        this.ySpeed=ySpeed;
    }
    
    public Ball(ShapeDimension s, boolean decoy) {
        super(s);
        s.setName("Ball"+ballIdentifier.getAndIncrement());
        this.decoy = decoy;
    }
    public void setBallId(int id){
        this.ballId=id;
    }
    
    public int getBallId(){
        return this.ballId;
    }
    
    public float getXspeed(){
        return this.xSpeed;
    }
    
    public float getYspeed(){
        return this.ySpeed;
    }

    public boolean isStartingBall() {
        return startingBall;
    }

    public void setStartingBall(boolean startingBall) {
        this.startingBall = startingBall;
    }

    public boolean isDecoy() {
        return decoy;
    }

    public void setDecoy(boolean decoy) {
        this.decoy = decoy;
    }

    public void setAcidballPowerup(AcidBallPowerUp acidBall) {
        this.acidBall = acidBall;
    }

    public AcidBallPowerUp getAcidBall() {
        return acidBall;
    }

    public void setLinearVelocity(float x, float y) {
        this.getBody().setLinearVelocity(new Vec2(x, y));
    }

    public Vec2 getLinearVelocity() {
        return this.getBody().getLinearVelocity();
    }

    @Override
    public BodyConfiguration getConfig() {
        BodyConfiguration ballBodyConfig = BodyConfigurationFactory.getInstance().createBallConfig(dimension);
        setBox2dConfig(ballBodyConfig);
        
        return config;
    }
    
    
}
