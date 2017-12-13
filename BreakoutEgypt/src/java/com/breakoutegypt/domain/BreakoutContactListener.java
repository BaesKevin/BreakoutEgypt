/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.brickcollisionhandlers.BallPaddleContact;
import com.breakoutegypt.domain.brickcollisionhandlers.BallBrickContact;
import com.breakoutegypt.domain.brickcollisionhandlers.BallGroundContact;
import com.breakoutegypt.domain.brickcollisionhandlers.ProjectileGroundContact;
import com.breakoutegypt.domain.brickcollisionhandlers.ProjectilePaddleContact;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.Projectile;
import com.breakoutegypt.domain.shapes.bricks.Brick;
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

    private BreakoutWorld world;

    public BreakoutContactListener(BreakoutWorld world) {
        this.world = world;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture f1 = contact.getFixtureA();
        Fixture f2 = contact.getFixtureB();

        RegularBody s1 = (RegularBody) f1.getBody().getUserData();
        RegularBody s2 = (RegularBody) f2.getBody().getUserData();

        // stop flow in contactlistener here, add event to ContactDuringStepListener
        if (s1 instanceof Ball && s2 instanceof Brick || s1 instanceof Brick && s2 instanceof Ball) {
            world.addContact(new BallBrickContact(s1, s2));
        } else if ((s1 != null && s1.getName().contains("ground") || s2 != null & s2.getName().contains("ground")) && (s1 instanceof Ball || s2 instanceof Ball)) {
            world.addContact(new BallGroundContact(s1, s2));
        } else if ((s1 instanceof Paddle || s2 instanceof Paddle) && (s1 instanceof Ball || s2 instanceof Ball)) {
            world.addContact(new BallPaddleContact(s1, s2));
        } else if ((s1 instanceof Projectile || s2 instanceof Projectile) && (s1 instanceof Paddle || s2 instanceof Paddle)) {
            world.addContact(new ProjectilePaddleContact(s1, s2));
        } else if ((s1 instanceof Projectile || s2 instanceof Projectile) && (s1 != null && s1.getName().contains("ground") || s2 != null & s2.getName().contains("ground"))) {
            world.addContact(new ProjectileGroundContact(s1, s2));
        }

    }

    // detect if the ball hit the paddle
    // nothing should happen here since we already stored the contact
    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
//        Fixture f1 = contact.getFixtureA();
//        Fixture f2 = contact.getFixtureB();
//
//        RegularBody s1 = (RegularBody) f1.getBody().getUserData();
//        RegularBody s2 = (RegularBody) f2.getBody().getUserData();
//
//        Brick brick = getBrickBallCollidedWith(f1, f2, s1, s2);
//
//        if (brick != null && !brick.isVisible()) {
//            contact.setEnabled(false);
//        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
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

}
