/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import com.breakoutws.domain.shapes.Ball;
import com.breakoutws.domain.shapes.Brick;
import com.breakoutws.domain.shapes.BrickType;
import com.breakoutws.domain.shapes.Paddle;
import com.breakoutws.domain.shapes.RegularBody;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author kevin
 */
public class LevelState {
    
    private List<Brick> bricks;
    private Ball ball;
    private List<Paddle> paddles;
    private Ball startingBall;

    private BodyFactory factory;
    private BreakoutWorld breakoutWorld;

    public LevelState(BreakoutWorld breakoutWorld, Ball ball, List<Paddle> paddles, List<Brick> bricks) {
        this.bricks = new ArrayList();
        this.paddles = new ArrayList();
        this.breakoutWorld = breakoutWorld;
        factory = new BodyFactory(breakoutWorld.getWorld());

        
        createBounds();
        addBall(ball);

        for(Paddle paddle : paddles){
            addPaddle(paddle);
        }
        for (Brick brick : bricks) {
            addBrick(brick);
        }

    }

    public void addPaddle(Paddle p) {
        Body paddleBody = factory.createPaddle(p);
        p.setBody(paddleBody);
        
        paddles.add(p);
    }

    // TODO based on bricktype it might be necessary to do more here
    // e.g. all surrounding bricks an explosive brick
    public void addBrick(Brick brick) {
        Body brickBody = factory.createTriangle(brick);
        brick.setBody(brickBody);
        bricks.add(brick);
    }

    public void addBall(Ball b) {
        this.startingBall = b;
        
        Body ballBody = factory.createCircle(b);
        b.setBody(ballBody);
        this.ball = b;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public Ball getBall() {
        return ball;
    }

    public List<Paddle> getPaddles() {
        return paddles;
    }

    public void removeBrick(Brick brick) {
        bricks.remove(brick);
    }
    
    void resetBall() {
        
        Body ballBody = new BodyFactory(breakoutWorld.getBox2dWorld()).createCircle(startingBall);
        ball.setBody(ballBody);
    }
    
    public int getTargetBricksLeft() {
        int targetsLeft = 0;
        for (Brick brick : bricks) {
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

    public List<Brick> getRangeOfBricksAroundBody(Brick centreBrick, int range) {
        List<Brick> bricksToRemove = new ArrayList();
        Point centre = centreBrick.getGridPosition();
        
        Point currentBrickPosition;
        
        if(range == 0){
            bricksToRemove.add( centreBrick );
        } else {
            for(Brick brick : bricks){
                currentBrickPosition = brick.getGridPosition();
                if(Math.abs(centre.x - currentBrickPosition.x) <= range && Math.abs(centre.y - currentBrickPosition.y) <= range ){
                    if(brick.isSwitched() && brick.getBrickType() != BrickType.SWITCH){
                        bricksToRemove.add(brick);
                    }
                    
                }
            }
        }
        
        return bricksToRemove;
    }
}
