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
        Fixture f1 = contact.getFixtureA();
        Fixture f2 = contact.getFixtureB();
        
        Shape s1 = (Shape) f1.getBody().getUserData();
        Shape s2 = (Shape) f2.getBody().getUserData();
        
       if(  ballHitBrick(f1, f2, s1, s2))
       {
           world.destroyBrick(f1.getBody(), s1.getName());
       } 
       else if (isBallOutOfBounds(f1, f2, s1, s2))
       {
           // System.out.println("Ball is out of bounds");
           world.resetBall();
       }
    }
    
    private boolean ballHitBrick(Fixture f1, Fixture f2, Shape s1, Shape s2){
        boolean hitBrick = false;
        
        if( s1 != null && s1.getName().contains("brick")){
            hitBrick = true;
        }else if(s2 != null && s2.getName().contains("brick")){
            hitBrick = true;
        }
        
        return hitBrick;
    }
    
    private boolean isBallOutOfBounds (Fixture fix1, Fixture fix2, Shape s1, Shape s2){
        boolean outOfBounds = false;
        
        if( s1 != null && s1.getName().contains("ground")){
            outOfBounds = true;
        }else if(s2 != null && s2.getName().contains("ground")){
            outOfBounds = true;
        }
        
        return outOfBounds;
        
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
