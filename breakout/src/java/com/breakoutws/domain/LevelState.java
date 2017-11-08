/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import com.breakoutws.domain.shapes.Ball;
import com.breakoutws.domain.shapes.Brick;
import com.breakoutws.domain.shapes.Paddle;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author kevin
 */
public class LevelState {
    
    private List<Body> brickBodies;
    private Body ballBody;
    private Body paddleBody;
    private Ball startingBall;

    private BodyFactory factory;
    private BreakoutWorld breakoutWorld;

    public LevelState(BreakoutWorld breakoutWorld, Ball ball, Paddle paddle, List<Brick> bricks) {
        brickBodies = new ArrayList();
        this.breakoutWorld = breakoutWorld;
        factory = new BodyFactory(breakoutWorld.getWorld());

        createBounds();
        addBall(ball);
        addPaddle(paddle);

        for (Brick brick : bricks) {
            addBrick(brick);
        }

    }

    public void addPaddle(Paddle p) {
        paddleBody = factory.createPaddle(p);
    }

    // TODO based on bricktype it might be necessary to do more here
    // e.g. all surrounding bricks an explosive brick
    public void addBrick(Brick brick) {
        brickBodies.add(factory.createTriangle(brick));
    }

    public void addBall(Ball b) {
        this.startingBall = b;
        ballBody = factory.createCircle(b);
    }

    public List<Body> getBricks() {
        return brickBodies;
    }

    public Body getBall() {
        return ballBody;
    }

    public Body getPaddle() {
        return paddleBody;
    }

    public void removeBrick(Body brick) {
        brickBodies.remove(brick);
    }
    
    void resetBall() {
        ballBody = new BodyFactory(breakoutWorld.getBox2dWorld()).createCircle(startingBall);
    }
    
    public int getTargetBricksLeft() {
        int targetsLeft = 0;
        for (Body body : brickBodies) {
            Brick brick = (Brick) body.getUserData();
            if (brick.isTarget()) {
                ++targetsLeft;
            }
        }
        return targetsLeft;
    }

    private void createBounds() {
        factory.addGround(300, 300);

        factory.addWall(0, 0, 1, 300); //Left wall
        factory.addWall(300, 0, 1, 300); //Right wall, keep in mind 

        factory.addWall(0, 0, 300, 1); //roof 
    }

    List<Body> getRangeOfBricksAroundBody(Body brickBody, int range) {
        List<Body> bodies = new ArrayList();
        Brick centreBrick = (Brick)brickBody.getUserData();
        Point centre = centreBrick.getGridPosition();
        
        Brick currentBrick;
        Point currentBrickPosition;
        
        if(range == 0){
            bodies.add( brickBody );
            return bodies;
        } else {
            for(Body body : brickBodies){
                currentBrick = (Brick) body.getUserData();
                currentBrickPosition = currentBrick.getGridPosition();
                System.out.println("Current brick: " + currentBrickPosition);
                if(Math.abs(centre.x - currentBrickPosition.x) <= range && Math.abs(centre.y - currentBrickPosition.y) <= range ){
                    bodies.add(body);
                }
            }
        }
        
        return bodies;
    }
}
