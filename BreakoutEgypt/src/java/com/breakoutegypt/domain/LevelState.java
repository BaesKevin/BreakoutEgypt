/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.shapes.BodyConfigurationFactory;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.BodyConfiguration;
import com.breakoutegypt.domain.shapes.Brick;
import com.breakoutegypt.domain.shapes.BrickType;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.RegularBody;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class LevelState {

    private List<Brick> bricks;
    private Ball ball;
    private List<Paddle> paddles;
    private List<RegularBody> walls;
    private Ball startingBall;

    private BodyConfigurationFactory factory;

    public LevelState(Ball ball, List<Paddle> paddles, List<Brick> bricks) {
        this.bricks = new ArrayList();
        this.paddles = new ArrayList();
        this.walls = new ArrayList();
        
        factory = new BodyConfigurationFactory();

        createBounds();
        addBall(ball);

        for (Paddle paddle : paddles) {
            addPaddle(paddle);
        }
        for (Brick brick : bricks) {
            addBrick(brick);
        }

    }

    public void addPaddle(Paddle p) {
//        Body paddleBody = factory.createPaddleConfig(p);
        BodyConfiguration domePaddleConfig = factory.createDomePaddleConfig(p.getShape());
        p.setBox2dConfig(domePaddleConfig);

        paddles.add(p);
    }

    // TODO based on bricktype it might be necessary to do more here
    // e.g. all surrounding bricks an explosive brick
    public void addBrick(Brick brick) {
        BodyConfiguration brickBody = factory.createTriangleConfig(brick.getShape());
        brick.setBox2dConfig(brickBody);
        
        bricks.add(brick);
    }

    public void addBall(Ball ball) {
        this.startingBall = ball;

        BodyConfiguration ballBodyConfig = factory.createBallConfig(ball.getShape());
        ball.setBox2dConfig(ballBodyConfig);
        this.ball = ball;
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

    void resetBall(BreakoutWorld breakoutWorld) {

        BodyConfiguration ballBodyBodyConfig = new BodyConfigurationFactory().createBallConfig(startingBall.getShape());
        startingBall.setBox2dConfig(ballBodyBodyConfig);
        breakoutWorld.spawn(startingBall);
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
        ShapeDimension groundShape = new ShapeDimension("ground", 0, 300, 300, 1);
        ShapeDimension leftWallDim = new ShapeDimension("leftwall", 0, 0, 1, 300);
        ShapeDimension rightWallDim = new ShapeDimension("rightwall", 300, 0, 1, 300);
        ShapeDimension topWallDim = new ShapeDimension("topwall", 0, 0, 300, 1);

        RegularBody ground = new RegularBody(groundShape);
        RegularBody leftWall = new RegularBody(leftWallDim);
        RegularBody rightWall = new RegularBody(rightWallDim);
        RegularBody topWall = new RegularBody(topWallDim);

        BodyConfiguration groundConfig = factory.createWallConfig(groundShape, true);
        BodyConfiguration leftWallConfig = factory.createWallConfig(leftWallDim, false);
        BodyConfiguration rightWallConfig = factory.createWallConfig(rightWallDim, false);
        BodyConfiguration topWallConfig = factory.createWallConfig(topWallDim, false);

        ground.setBox2dConfig(groundConfig);
        leftWall.setBox2dConfig(leftWallConfig);
        rightWall.setBox2dConfig(rightWallConfig);
        topWall.setBox2dConfig(topWallConfig);

        walls.add(ground);
        walls.add(leftWall);
        walls.add(rightWall);
        walls.add(topWall);
    }

    public void spawnAllObjects(BreakoutWorld breakoutWorld) {
        for (Brick b : bricks) {
            breakoutWorld.spawn(b);
        }

        breakoutWorld.spawn(ball);

        for (Paddle p : paddles) {
            breakoutWorld.spawn(p);
        }
        
        for( RegularBody wall : walls){
            breakoutWorld.spawn(wall);
        }
    }

    public List<Brick> getRangeOfBricksAroundBody(Brick centreBrick, int range) {
        List<Brick> bricksToRemove = new ArrayList();
        Point centre = centreBrick.getGridPosition();

        Point currentBrickPosition;

        if (range == 0) {
            bricksToRemove.add(centreBrick);
        } else {
            for (Brick brick : bricks) {
                currentBrickPosition = brick.getGridPosition();
                if (Math.abs(centre.x - currentBrickPosition.x) <= range && Math.abs(centre.y - currentBrickPosition.y) <= range) {
                    if (brick.isSwitched() && brick.getBrickType() != BrickType.SWITCH) {
                        bricksToRemove.add(brick);
                    }

                }
            }
        }

        return bricksToRemove;
    }
}
