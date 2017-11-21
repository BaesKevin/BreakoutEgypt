/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.brickcollisionhandlers.BrickCollisionDecider;
import com.breakoutegypt.domain.effects.EffectHandler;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.RegularBody;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author kevin
 */
public class BreakoutContactListener implements ContactListener {

    private EffectHandler effectHandler;
    private BallEventHandler ballEventHandler; 
    
    public BreakoutContactListener(EffectHandler effectHandler, BallEventHandler ballEventHandler) {
        this.effectHandler = effectHandler;
        this.ballEventHandler = ballEventHandler;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture f1 = contact.getFixtureA();
        Fixture f2 = contact.getFixtureB();

        RegularBody s1 = (RegularBody) f1.getBody().getUserData();
        RegularBody s2 = (RegularBody) f2.getBody().getUserData();

        Brick brick = getBrickBallCollidedWith(f1, f2, s1, s2);
        boolean isBallOutOfBounds = isBallOutOfBounds(f1, f2, s1, s2);

        if (brick != null) {
            new BrickCollisionDecider(brick, effectHandler).handleCollision();
        } else if (isBallOutOfBounds) {
            Ball ball = getOutOfBoundsBall(s1, s2);
            System.out.println("Out of bounds ball: " + ball.getName());
            // System.out.println("Ball is out of bounds");
            ballEventHandler.setResetBallFlag(ball);
        }
    }

    private Brick getBrickBallCollidedWith(Fixture f1, Fixture f2, RegularBody s1, RegularBody s2) {
        Brick brick = null;

        if (s1 != null && s1 instanceof Brick) {
            brick = (Brick) s1;
        } else if (s2 != null && s2 instanceof Brick) {
            brick = (Brick) s2;
        }

        return brick;
    }

    private boolean isBallOutOfBounds(Fixture fix1, Fixture fix2, RegularBody s1, RegularBody s2) {
        boolean outOfBounds = false;

        if (s1 != null && s1.getName().contains("ground")) {
            outOfBounds = true;
        } else if (s2 != null && s2.getName().contains("ground")) {
            outOfBounds = true;
        }

        return outOfBounds;

    }

    // detect if the ball hit the paddle
    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        RegularBody data1 = (RegularBody) a.getBody().getUserData();
        RegularBody data2 = (RegularBody) b.getBody().getUserData();
        Ball ball;
        Paddle paddle;
        if (data1 != null && data1 instanceof Ball
                && data2 != null && data2 instanceof Paddle) {
            ball = (Ball) data1;
            paddle = (Paddle) data2;
            ballEventHandler.ballHitPaddle(ball, paddle);
        } else if (data1 != null && data1 instanceof Paddle
                && data2 != null && data2 instanceof Ball) {
            ball = (Ball) data2;
            paddle = (Paddle) data1;
            ballEventHandler.ballHitPaddle(ball, paddle);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture f1 = contact.getFixtureA();
        Fixture f2 = contact.getFixtureB();

        RegularBody s1 = (RegularBody) f1.getBody().getUserData();
        RegularBody s2 = (RegularBody) f2.getBody().getUserData();

        Brick brick = getBrickBallCollidedWith(f1, f2, s1, s2);

        if (brick != null && !brick.isVisible()) {
            contact.setEnabled(false);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private Ball getOutOfBoundsBall(RegularBody s1, RegularBody s2) {
        Ball ball = null;
        
        if(s1 != null && s1 instanceof Ball){
            ball = (Ball) s1;
        } 
        else if (s2 != null && s2 instanceof Ball){
            ball = (Ball) s2;
        }
        
        return ball;
    }

}
