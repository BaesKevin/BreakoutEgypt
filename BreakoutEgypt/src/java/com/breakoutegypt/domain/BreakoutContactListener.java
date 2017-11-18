/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.brickcollisionhandlers.BrickCollisionHandler;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Brick;
import com.breakoutegypt.domain.shapes.IShape;
import com.breakoutegypt.domain.shapes.Paddle;
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

    private BreakoutWorld world;

    public BreakoutContactListener(BreakoutWorld world) {
        this.world = world;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture f1 = contact.getFixtureA();
        Fixture f2 = contact.getFixtureB();

        IShape s1 = (IShape) f1.getBody().getUserData();
        IShape s2 = (IShape) f2.getBody().getUserData();

        Brick brick = getBrickBallCollidedWith(f1, f2, s1, s2);
        boolean isBallOutOfBounds = isBallOutOfBounds(f1, f2, s1, s2);

        if (brick != null) {
            new BrickCollisionHandler(brick, world.getLevel()).handleCollision();
        } else if (isBallOutOfBounds) {
            // System.out.println("Ball is out of bounds");
            world.getLevel().getBreakoutWorld().resetBall();
        }
    }

    private Brick getBrickBallCollidedWith(Fixture f1, Fixture f2, IShape s1, IShape s2) {
        Brick brick = null;

        if (s1 != null && s1 instanceof Brick) {
            brick = (Brick) s1;
        } else if (s2 != null && s2 instanceof Brick) {
            brick = (Brick) s2;
        }

        return brick;
    }

    private boolean isBallOutOfBounds(Fixture fix1, Fixture fix2, IShape s1, IShape s2) {
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

        IShape data1 = (IShape) a.getBody().getUserData();
        IShape data2 = (IShape) b.getBody().getUserData();
        Ball ball;
        Paddle paddle;
        if (data1 != null && data1 instanceof Ball
                && data2 != null && data2 instanceof Paddle) {
            ball = (Ball) data1;
            paddle = (Paddle) data2;
            world.ballHitPaddle(ball, paddle);
        } else if (data1 != null && data1 instanceof Paddle
                && data2 != null && data2 instanceof Ball) {
            ball = (Ball) data2;
            paddle = (Paddle) data1;
            world.ballHitPaddle(ball, paddle);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture f1 = contact.getFixtureA();
        Fixture f2 = contact.getFixtureB();

        IShape s1 = (IShape) f1.getBody().getUserData();
        IShape s2 = (IShape) f2.getBody().getUserData();

        Brick brick = getBrickBallCollidedWith(f1, f2, s1, s2);

        if (brick != null && !brick.isSwitched()) {
            contact.setEnabled(false);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}
