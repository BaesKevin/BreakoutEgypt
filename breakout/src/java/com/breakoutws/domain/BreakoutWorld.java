/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import com.breakoutws.data.StaticDummyHighscoreRepo;
import com.breakoutws.domain.shapes.Brick;
import com.breakoutws.domain.shapes.IShape;
import java.util.ArrayList;
import java.util.List;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

/**
 *
 * @author kevin
 */
public class BreakoutWorld {

    private World world;

    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    private final float timestep = 1.0f / 60.0f;
    private final int velocityIterations = 8;
    private final int positionIterations = 8;
    private Level currentLevel;
    private boolean isBallOutOfBounds = false;

    // keep a seperate list of bodies to dstroy in the frontend since we only want to send this info once
    private List<Body> bodiesToDestroy;
    private List<String> keysOfBodiesToDestroy;
    private boolean ballHitPaddle = false;
    private List<BrickMessage> messages;
    
    public BreakoutWorld(Level level) {
        bodiesToDestroy = new ArrayList();
        keysOfBodiesToDestroy = new ArrayList();
        world = new World(new Vec2(0.0f, 0.0f));
        world.setContactListener(new BreakoutContactListener(this));

        this.currentLevel = level;
        messages = new ArrayList();
    }

    public World getWorld() {
        return world;
    }

    public void setLevel(Level level) {
        this.currentLevel = level;
    }
    
    public Level getLevel(  ){
        return currentLevel;
    }

    public void movePaddle(float x, float y) {
        currentLevel.getPaddle().moveTo(x, y);
//        System.out.println(currentLevel.getPaddle().getPosition().x);
    }

    public void destroyBrick(Brick brick) {
        List<Brick> bricks = new ArrayList();
        bricks.add(brick);
        destroyBricks(bricks);
    }

    public void destroyBricksInRange(Brick brickBody, int range) {
        List<Brick> bodiesInRange = currentLevel.getRangeOfBricksAroundBody(brickBody, range);
        String brickName;
        for (Brick brickBodyInRange : bodiesInRange) {
            if (!bodiesToDestroy.contains(brickBodyInRange.getBody())) {
                brickName = brickBodyInRange.getName();
                currentLevel.removeBrick(brickBodyInRange);
                bodiesToDestroy.add(brickBodyInRange.getBody());
                messages.add(new BrickMessage(brickName, BrickMessageType.DESTROY));
            }
        }

        if (currentLevel.allTargetBricksDestroyed()) {
            System.out.println("BreakoutWorld: all brick destroyed");
            currentLevel.getScoreTimer().stop();
            
            StaticDummyHighscoreRepo dummyRepo = new StaticDummyHighscoreRepo();
            
            Score scoreOfPlayer = new Score(currentLevel.getId(), new User("This is a new user"), currentLevel.getScoreTimer().getDuration(), "hard");
            dummyRepo.addScore(scoreOfPlayer);
            System.out.println(dummyRepo.getRank(currentLevel.getId(), scoreOfPlayer));
            
            dummyRepo.getScoresByLevel(currentLevel.getId(), "hard");
            currentLevel.initNextLevel();
        }
    }

    public void toggleBricks(List<Brick> switchBricks) {
        for (Brick switchBrick : switchBricks) {
            
            switchBrick.toggle();
            
            BrickMessageType toggleType;
            if(switchBrick.isSwitched()){
                toggleType = BrickMessageType.SHOW;
            } else {
                toggleType = BrickMessageType.HIDE;
            }
            
            messages.add(new BrickMessage(switchBrick.getName(), toggleType));
            
        }
    }

    void ballHitPaddle() {
        ballHitPaddle = true;
    }

    public List<BrickMessage> getBrickMessages() {
        return messages;
    }

    void clearBrickMessages() {
        messages.clear();
    }

    // any changes to the world state must be made here to try to avoid concurrency issues where the game is 
    // updating some state while we are changing it too
    public void step() {
        world.step(timestep, velocityIterations, positionIterations);
        for (Body brick : bodiesToDestroy) {

            world.destroyBody(brick);
        }
        bodiesToDestroy.clear();

        if (ballHitPaddle) {
            adjustBallDirection();
            ballHitPaddle = false;
        } else if (isBallOutOfBounds) {
            currentLevel.setLevelStarted(false);
            world.destroyBody(currentLevel.getBall().getBody());
            currentLevel.resetBall();
            isBallOutOfBounds = false;
        }
    }

    private void adjustBallDirection() {
        float width = currentLevel.getPaddle().getShape().getWidth();
        float adjustedX = currentLevel.getPaddle().getBody().getPosition().x - width / 2;
        float relativeDistance = (currentLevel.getBall().getBody().getPosition().x - adjustedX) / width;

        float newX = -100 + relativeDistance * 200;
//        System.out.printf("Relative position: %f, newx: %f", relativeDistance,newX);
        System.out.println("Adjust ball direction for: " + currentLevel.getBall().getName());
        currentLevel.getBall().setLinearVelocity(newX, currentLevel.getBall().getLinearVelocity().y);
    }

    public World getBox2dWorld() {
        return world;
    }

    void resetBall() {
        isBallOutOfBounds = true;
    }

}
