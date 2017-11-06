/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import java.util.ArrayList;
import java.util.List;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

/**
 *
 * @author kevin
 */
class BreakoutWorld {
    private World world;
    
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    private final float timestep = 10.0f/60.0f;
    private final int velocityIterations = 8;
    private final int positionIterations = 8;
    private Level currentLevel;
    private boolean isBallOutOfBounds = false;
    
    // keep a seperate list of bodies to dstroy in the frontend since we only want to send this info once
    private List<Body> bodiesToDestroy;
    private List<String> keysOfBodiesToDestroy;
    private boolean ballHitPaddle = false;
    
    public BreakoutWorld(Level level) {
        bodiesToDestroy = new ArrayList();
        keysOfBodiesToDestroy = new ArrayList();
        world = new World(new Vec2(0.0f, 0.0f));
        world.setContactListener(new BreakoutContactListener(this));
        
        this.currentLevel = level;
    }
    
    public World getWorld() {
        return world;
    }
    
    public void setLevel(Level level){ this.currentLevel = level; }
    
    public void movePaddle(float x, float y){
        currentLevel.getPaddle().setTransform(new Vec2(x,y), 0);;
//        System.out.println(currentLevel.getPaddle().getPosition().x);
    }

    public void destroyBrick(Body brick, String key) {
        if (!bodiesToDestroy.contains(brick)) {
            currentLevel.removeBrick(brick);
            bodiesToDestroy.add(brick);
            keysOfBodiesToDestroy.add(key);
        }
        
        if (currentLevel.allTargetBricksDestroyed()) {
            currentLevel.getBall().setLinearVelocity(new Vec2(0.0f, 0.0f));        
        }
    }
    
    void ballHitPaddle() {
        ballHitPaddle = true;
    }

    public List<String> getKeysOfBodiesToDestroy() {
        return keysOfBodiesToDestroy;
    }
    
    void clearKeysOfBodiesToDestroy() {
        keysOfBodiesToDestroy.clear();
    }

    // any changes to the world state must be made here to try to avoid concurrency issues where the game is 
    // updating some state while we are changing it too
    public void step() {
        world.step(timestep, velocityIterations, positionIterations);
        for(Body brick : bodiesToDestroy){
            
            world.destroyBody(brick);
        }
        bodiesToDestroy.clear();
        
        if(ballHitPaddle){
            adjustBallDirection();
            ballHitPaddle = false;
        } 
        else if (isBallOutOfBounds)
        {
            world.destroyBody(currentLevel.getBall());
            currentLevel.resetBall();
            isBallOutOfBounds = false;
        }
    }

    private void adjustBallDirection(){
        float width = ((Shape)currentLevel.getPaddle().getUserData()).getWidth();
        float adjustedX = currentLevel.getPaddle().getPosition().x - width/2;
        float relativeDistance = (currentLevel.getBall().getPosition().x - adjustedX) / width ;


        float newX = -100 + relativeDistance * 200;
        System.out.printf("Relative position: %f, newx: %f", relativeDistance,newX);
        currentLevel.getBall().setLinearVelocity(new Vec2(newX, currentLevel.getBall().getLinearVelocity().y));
    }
    
    public World getBox2dWorld() {
        return world;
    }

    void resetBall() {
        isBallOutOfBounds = true;
    }
    
       
}
