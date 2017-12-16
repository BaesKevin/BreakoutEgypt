/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes;

import com.breakoutegypt.domain.powers.AcidBallPowerUp;
import org.jbox2d.common.Vec2;

/**
 *
 * @author kevin
 */
public class Ball extends RegularBody {

    private AcidBallPowerUp acidBall;
    private boolean decoy;
    private boolean startingBall;

    public Ball(ShapeDimension s) {
        this(s, false);
    }

    public Ball(ShapeDimension s, boolean decoy) {
        super(s);
        this.decoy = decoy;

        BodyConfiguration ballBodyConfig = new BodyConfigurationFactory().createBallConfig(s);
        setBox2dConfig(ballBodyConfig);
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
}
