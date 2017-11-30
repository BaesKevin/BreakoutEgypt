/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.effects.BrokenPaddlePowerUp;
import com.breakoutegypt.domain.effects.FloorPowerUp;
import com.breakoutegypt.domain.messages.BallMessage;
import com.breakoutegypt.domain.messages.BallMessageType;
import com.breakoutegypt.domain.messages.Message;
import com.breakoutegypt.domain.effects.Effect;
import com.breakoutegypt.domain.effects.PowerUp;
import com.breakoutegypt.domain.effects.PowerUpType;
import com.breakoutegypt.domain.effects.ToggleEffect;
import com.breakoutegypt.domain.shapes.BodyConfigurationFactory;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.BodyConfiguration;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.RegularBody;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    private List<Ball> balls;
    private List<Message> messages;
    private FloorPowerUp floor;

    public List<Message> getMessages() {
        return messages;
    }

    private BodyConfigurationFactory factory;

    public LevelState(Ball ball, Paddle paddle, List<Brick> bricks) {
        this(ball, new ArrayList(), bricks);
        addPaddle(paddle);
    }

    public LevelState(Ball ball, List<Paddle> paddles, List<Brick> bricks) {
        this(new ArrayList(), paddles, bricks);
        addBall(ball);
    }

    public LevelState(List<Ball> balls, List<Paddle> paddles, List<Brick> bricks) {
        this(balls, paddles, bricks, 0);
    }

    public LevelState(List<Ball> balls, List<Paddle> paddles, List<Brick> bricks, int noOfPowerups) {
        this.bricks = Collections.synchronizedList(new ArrayList());
        this.paddles = new ArrayList();
        this.walls = new ArrayList();
        this.balls = Collections.synchronizedList(new ArrayList());
        this.messages = new ArrayList();

        factory = new BodyConfigurationFactory();

        createBounds();
        addBalls(balls);

        for (Paddle paddle : paddles) {
            addPaddle(paddle);
        }
        for (Brick brick : bricks) {
            addBrick(brick);
        }

        if (noOfPowerups > 0) {
            List<PowerUp> powerups = createPowerups(3, paddles.get(0));
            bricks = generatePowerUps(bricks, powerups);
            bricks.get(5).setPowerUp(createBrokenPaddle(paddles.get(0)));
        }
    }

    public void addPaddle(Paddle p) {
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
        List<Ball> tempBalls = new ArrayList<>();
        tempBalls.add(ball);
        addBalls(tempBalls);
    }

    public void addBalls(List<Ball> balls) {
        boolean first = true;
        for (Ball b : balls) {
            BodyConfiguration ballBodyConfig = factory.createBallConfig(b.getShape());
            b.setBox2dConfig(ballBodyConfig);

            if (first) {
                this.startingBall = b;
                this.ball = b;
                first = false;
            }
            this.balls.add(b);
        }
    }

    public void addFloor(FloorPowerUp floor) {
        this.floor = floor;
    }

    public void removePaddle(Paddle p) {
        paddles.remove(p);
    }

    public void removeFloor() {
        //TODO remove floor
        this.floor = null;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public Ball getBall() {
        return ball;
    }

    public List<Ball> getBalls() {
        return balls;
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
        messages.add(new BallMessage(startingBall, BallMessageType.ADD));
        balls.add(startingBall);
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

    public List<RegularBody> getAllObjectsToSpawn() {
        List<RegularBody> bodies = new ArrayList();
        bodies.addAll(balls);
        bodies.addAll(walls);
        bodies.addAll(bricks);
        bodies.addAll(paddles);
        return bodies;
    }

    // TODO calculate range without Points, test this monstrosity
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
                    if (brick.isVisible() && !(hasToggleEffect(brick.getEffects()))) {
                        bricksToRemove.add(brick);
                    }

                }
            }
        }

        return bricksToRemove;
    }

    private boolean hasToggleEffect(List<Effect> effects) {
        boolean hasSwitch = false;

        for (Effect e : effects) {
            if (e instanceof ToggleEffect) {
                hasSwitch = true;
                break;
            }
        }

        return hasSwitch;
    }

    void removeBall(Ball ball) {
        for (Ball b : balls) {
            if (b.getName().equals(ball.getName())) {
                balls.remove(b);
                messages.add(new BallMessage(ball.getName(), BallMessageType.REMOVE));
                break;
            }
        }
    }

    public void clearMessages() {
        messages.clear();
    }

    public int calculatePaddleWidthWithGaps() {
        List<Paddle> paddles = getPaddles();
        int paddlewidth = paddles.get(0).getShape().getWidth();
        int noOfPaddles = paddles.size();
        int noOfGaps = noOfPaddles - 1;
        int width = noOfPaddles * paddlewidth + noOfGaps * paddlewidth;
        return width;
    }

    private List<Brick> generatePowerUps(List<Brick> bricks, List<PowerUp> powerups) {
        PowerUpType[] poweruptypes = PowerUpType.values();
        Random r = new Random();
        List<Integer> bricksWithPowerup = new ArrayList();
        int bricknr = r.nextInt(bricks.size());
        Brick powerUpBrick;
        for (int i = 0; i < powerups.size(); i++) {
            powerUpBrick = bricks.get(bricknr);

            while (bricksWithPowerup.contains(bricknr) || !powerUpBrick.getType().equals("REGULAR")) {
                bricknr = r.nextInt(bricks.size());
                powerUpBrick = bricks.get(bricknr);
            }

            powerUpBrick.setPowerUp(powerups.get(i));
            bricksWithPowerup.add(bricknr);
            bricknr = r.nextInt(bricks.size());
        }

        return bricks;
    }

    private List<PowerUp> createPowerups(int noOfPowerups, Paddle paddle) {

        List<PowerUp> powerups = new ArrayList();
        PowerUpType[] poweruptypes = PowerUpType.values();

        Random r = new Random();
        int powerupNr = r.nextInt(poweruptypes.length);

        for (int i = 0; i < noOfPowerups; i++) {
            if (poweruptypes[powerupNr].name().equals("FLOOR")) {
                powerups.add(createFloor());
            } else if (poweruptypes[powerupNr].name().equals("BROKENPADDLE")) {
                powerups.add(createBrokenPaddle(paddle));
            }
            powerupNr = r.nextInt(poweruptypes.length);
        }

        return powerups;
    }

    public FloorPowerUp createFloor() {
        ShapeDimension floorShape = new ShapeDimension("floor", 0, 290, 300, 3);
        return new FloorPowerUp(floorShape);
    }

    public BrokenPaddlePowerUp createBrokenPaddle(Paddle p) {
        return new BrokenPaddlePowerUp(p);
    }
}
