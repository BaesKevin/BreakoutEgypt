/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author kevin
 */
public class BreakoutContactListener implements ContactListener{
    private BreakoutWorld world;
    
    public BreakoutContactListener(BreakoutWorld world){
        this.world = world;
    }
    
    
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        
        Ball ball;
        Brick brick;
        Object data1 = a.getBody().getUserData();
        Object data2 = b.getBody().getUserData();

        if( data1 instanceof Brick && data2 instanceof Ball){
            brick = (Brick) data1;
            ball = (Ball) data2;
            world.destroyBrick(brick);
        }else if( data1 instanceof Ball && data2 instanceof Brick){
            brick = (Brick) data2;
            ball = (Ball) data1;
            world.destroyBrick(brick);
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
    
}
