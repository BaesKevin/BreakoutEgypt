/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.data.DefaultShapeRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.domain.powers.AcidBallPowerUp;
import com.breakoutegypt.domain.powers.BrokenPaddlePowerUp;
import com.breakoutegypt.domain.powers.FloorPowerUp;
import com.breakoutegypt.domain.messages.BallMessage;
import com.breakoutegypt.domain.messages.BallMessageType;
import com.breakoutegypt.domain.messages.Message;
import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.GameDifficulty;
import com.breakoutegypt.domain.powers.FloodPowerDown;
import com.breakoutegypt.domain.powers.InvertedControlsPowerDown;
import com.breakoutegypt.domain.powers.PowerDownType;
import com.breakoutegypt.domain.powers.PowerUpType;
import com.breakoutegypt.domain.powers.ProjectilePowerDown;
import com.breakoutegypt.domain.shapes.BodyConfigurationFactory;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.BodyConfiguration;
import com.breakoutegypt.domain.shapes.DimensionDefaults;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.Projectile;
import com.breakoutegypt.domain.shapes.RegularBody;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author kevin
 */
public class LevelState {

    private List<Brick> bricks;
    private List<Paddle> paddles;
    private List<RegularBody> walls;
    private Ball startingBall;
    private List<Ball> balls;
    private List<Message> messages;
    private FloorPowerUp floor;
    private List<Projectile> projectiles;
    private Difficulty difficulty;

    public List<Message> getMessages() {
        return messages;
    }

    private BodyConfigurationFactory factory;

    public LevelState(Ball ball, Paddle paddle, List<Brick> bricks) {
        this(ball, Arrays.asList(new Paddle[]{paddle}), bricks);
    }

    public LevelState(Ball ball, List<Paddle> paddles, List<Brick> bricks) {
        this(Arrays.asList(new Ball[]{ball}), paddles, bricks);
    }

    public LevelState(List<Ball> balls, List<Paddle> paddles, List<Brick> bricks) {
        this(balls, paddles, bricks, Repositories.getDifficultyRepository().findByName(GameDifficulty.EASY));
    }

    public LevelState(List<Ball> balls, List<Paddle> paddles, List<Brick> bricks, Difficulty difficulty) {
        this(balls, paddles, bricks, difficulty, false);
    }
    
    public LevelState(List<Ball> balls, List<Paddle> paddles, List<Brick> bricks, Difficulty difficulty, boolean hasPowerups) {
        this.bricks = Collections.synchronizedList(new ArrayList());
        this.paddles = Collections.synchronizedList(new ArrayList());
        this.walls = new ArrayList();
        this.balls = Collections.synchronizedList(new ArrayList());
        this.messages = new ArrayList();
        this.projectiles = new ArrayList();
        this.difficulty = difficulty;
        factory = BodyConfigurationFactory.getInstance();

        createBounds();
        addBalls(balls);

        for (Paddle paddle : paddles) {
            addPaddle(paddle);
        }
        for (Brick brick : bricks) {
            addBrick(brick);
        }
        if (hasPowerups) generatePowerUpsAndDowns();
    }

    public void addPaddle(Paddle p) {
        paddles.add(p);
    }
    
    public void addBrick(Brick brick) {
        bricks.add(brick);
    }

    public void addBall(Ball ball) {
        List<Ball> tempBalls = new ArrayList<>();
        tempBalls.add(ball);
        addBalls(tempBalls);
    }

    public void addBalls(List<Ball> balls) {
        for (Ball b : balls) {
            if (b.isStartingBall()) {
                this.startingBall = b;
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

    public FloorPowerUp getFloor() {
        return this.floor;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public Ball getBall() {
        return startingBall;
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
        startingBall.setDecoy(false);
        ShapeDimension originalDimension =
                new ShapeDimension(startingBall.getName(), startingBall.getOriginalX(), startingBall.getOriginalY(), startingBall.getWidth(), startingBall.getHeight());
        BodyConfiguration ballBodyBodyConfig = factory.createBallConfig(originalDimension);
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
        ShapeDimension groundShape = new ShapeDimension("ground", -5 , BreakoutWorld.DIMENSION + 5, BreakoutWorld.DIMENSION + 10, 5);
        ShapeDimension leftWallDim = new ShapeDimension("leftwall", -5, -5, 5, BreakoutWorld.DIMENSION + 10);
        ShapeDimension rightWallDim = new ShapeDimension("rightwall", BreakoutWorld.DIMENSION + 5, -5, 5, BreakoutWorld.DIMENSION + 10);
        ShapeDimension topWallDim = new ShapeDimension("topwall", -5, -5, BreakoutWorld.DIMENSION + 10, 5);

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

    private List<Brick> getRangeOfBricksAroundBodyHelper(Brick centreBrick, int range, List<Brick> bricksToRemove) {
        if (range == 0) {
            bricksToRemove.add(centreBrick);
        } else {
            for (Brick brick : bricks) {
                int horizontalRange = range * DimensionDefaults.BRICK_WIDTH;
                int verticalRange = range * DimensionDefaults.BRICK_HEIGHT;

                boolean isBrickInHorizontalRange = Math.abs(centreBrick.getX() - brick.getX()) <= horizontalRange;
                boolean isBrickInVerticalRange = Math.abs(centreBrick.getY() - brick.getY()) <= verticalRange;
                boolean isNotSwitchBrick = !(brick.hasToggleEffect());
                
                if (brick.isVisible() && isBrickInHorizontalRange && isBrickInVerticalRange && isNotSwitchBrick) {
                    
                    ExplosiveEffect e = brick.getExplosiveEffect();
                        if (e != null && e.getRadius() > 0) {
                            if (!bricksToRemove.contains(brick)) {
                                bricksToRemove.add(brick);
                                bricksToRemove = getRangeOfBricksAroundBodyHelper(brick, e.getRadius(), bricksToRemove);
                            }
                        } else {
                            if (!bricksToRemove.contains(brick)) {
                                bricksToRemove.add(brick);
                            }
                        }
                    
                }
            }
        }
        return bricksToRemove;
    }

    public List<Brick> getRangeOfBricksAroundBody(Brick centreBrick, int range) {

        return getRangeOfBricksAroundBodyHelper(centreBrick, range, new ArrayList<>());

    }

    public void removeBall(Ball ball) {
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
        int paddlewidth = paddles.get(0).getWidth();
        int noOfPaddles = paddles.size();
        int noOfGaps = noOfPaddles - 1;

        int width = noOfPaddles * paddlewidth + noOfGaps * BrokenPaddlePowerUp.GAP;
        return width;
    }

    private void generatePowerUpsAndDowns() {

        List<Brick> regularBricks = new ArrayList();

        for (Brick b : bricks) {
            if (b.isRegular()) {
                regularBricks.add(b);
            }
        }

        int noOfPowers = Math.round(regularBricks.size() * 0.4f);

        int noOfPowerups = (int) Math.ceil(noOfPowers * difficulty.getPercentageOfPowerups());
        int noOfPowerdowns = (int) Math.floor(noOfPowers * difficulty.getPercentageOfPowerdowns());

        if (noOfPowers > 0) {
            List<Brick> bricksToAddPower = bricksToAddPowers(regularBricks, noOfPowerdowns + noOfPowerups);
            int identifier = 0;
            if (noOfPowerdowns > 0) {
                for (int i = 0; i < noOfPowerdowns; i++) {
                    createPowerdown(bricksToAddPower.get(identifier), identifier);
                    identifier++;
                }
            }
            if (noOfPowerups > 0) {
                for (int i = 0; i < noOfPowerups; i++) {
                    createPowerUp(bricksToAddPower.get(identifier), paddles.get(0), identifier);
                    identifier++;
                }
            }
        }
    }

    private List<Brick> bricksToAddPowers(List<Brick> bricks, int noOfBricksNeeded) {
        Random r = new Random();
        List<Integer> bricknrs = new ArrayList();
        List<Brick> bricksWithPowers = new ArrayList();

        int bricknr = r.nextInt(bricks.size());

        Brick powerUpBrick;

        for (int i = 0; i < noOfBricksNeeded; i++) {
            powerUpBrick = bricks.get(bricknr);
            while (bricknrs.contains(bricknr)) {
                bricknr = r.nextInt(bricks.size());
                powerUpBrick = bricks.get(bricknr);
            }
            bricksWithPowers.add(powerUpBrick);
            bricknrs.add(bricknr);
            bricknr = r.nextInt(bricks.size());
        }
        return bricksWithPowers;
    }

    private void createPowerdown(Brick b, int identifier) {

        int noOfPowerdownTypes = PowerDownType.values().length;
        Random r = new Random();
        int powerupNr = r.nextInt(noOfPowerdownTypes) + 1;

        switch (powerupNr) {
            case 1:
                b.setPowerdown(new FloodPowerDown(startingBall, 3, identifier));
                break;
            case 2:
                b.setPowerdown(createProjectilePowerDown(b, identifier));
                break;
            case 3:
                b.setPowerdown(new InvertedControlsPowerDown(difficulty.getPowerdownTime()));
                break;
        }
    }

    private void createPowerUp(Brick b, Paddle p, int identifier) {

        int noOfPowerupTypes = PowerUpType.values().length;
        Random r = new Random();
        int powerupNr = r.nextInt(noOfPowerupTypes) + 1;

        switch (powerupNr) {
            case 1:
                b.setPowerUp(createFloor(identifier));
                break;
            case 2:
                b.setPowerUp(createBrokenPaddle(p, identifier));
                break;
            case 3:
                b.setPowerUp(new AcidBallPowerUp("acidball" + identifier));
                break;
        }
    }

    public FloorPowerUp createFloor(int x) {
        ShapeDimension floorShape = DefaultShapeRepository.getInstance().getDefaultFloor("floor" + x);
        return new FloorPowerUp(floorShape, difficulty.getPowerupTime());
    }

    public BrokenPaddlePowerUp createBrokenPaddle(Paddle p, int x) {
        return new BrokenPaddlePowerUp(p, x, difficulty.getPowerupTime());
    }

    public ProjectilePowerDown createProjectilePowerDown(Brick brick, int x) {
//        ShapeDimension s = new ShapeDimension("projectile" + x, brick.getX() + brick.getWidth() / 2,
//                brick.getY() + brick.getHeight(), 4, 4);
        float projectileX = brick.getX() + brick.getWidth() / 2;
        float projectileY =  brick.getY() + brick.getHeight();
        
        Projectile projectile = DefaultShapeRepository.getInstance().getProjectile("projectile"+x, projectileX, projectileY);
        return new ProjectilePowerDown(projectile);
    }

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    void removeProjectile(Projectile projectile) {
        projectiles.remove(projectile);
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }
}
