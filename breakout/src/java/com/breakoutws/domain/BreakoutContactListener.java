/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
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
        
        Shape data1 = (Shape) a.getBody().getUserData();
        Shape data2 = (Shape) b.getBody().getUserData();
        
        if( data1 != null && data1.getName().contains("brick")){
            world.destroyBrick(a.getBody(), data1.getName());
            
        }else if(data2 != null && data2.getName().contains("brick")){
            world.destroyBrick(b.getBody(), data2.getName());
        }
    }

    // detect if the ball hit the paddle
    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        
        Shape data1 = (Shape) a.getBody().getUserData();
        Shape data2 = (Shape) b.getBody().getUserData();
        
         if ( data1 != null && data1.getName().equals("ball") && 
                    data2 != null && data2.getName().equals("paddle")){
            world.ballHitPaddle();
        } else if ( data1 != null && data1.getName().equals("paddle") && 
                    data2 != null && data2.getName().equals("ball")){
            world.ballHitPaddle();
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
    
}
