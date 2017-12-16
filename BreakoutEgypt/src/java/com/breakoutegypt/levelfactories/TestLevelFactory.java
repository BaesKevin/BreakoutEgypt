/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.levelfactories;

import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.powers.AcidBallPowerUp;
import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.effects.ToggleEffect;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.powers.BrokenPaddlePowerUp;
import com.breakoutegypt.domain.powers.FloodPowerDown;
import com.breakoutegypt.domain.powers.FloorPowerUp;
import com.breakoutegypt.domain.powers.ProjectilePowerDown;
import com.breakoutegypt.domain.shapes.DimensionDefaults;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.Projectile;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import com.breakoutegypt.domain.shapes.bricks.BrickType;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class TestLevelFactory extends LevelFactory {

    public static final int DEFAULT_OPEN_LEVELS = 500;
    public final int totalLevels = 1000;

    public TestLevelFactory(Game game) {
        super(game, 1000, DEFAULT_OPEN_LEVELS);
    }

    @Override
    public Level getCurrentLevel() {
        return currentLevel;
    }

    @Override
    protected void createCurrentLevel() {
        switch (currentLevelId) {
            case 1:
                currentLevel = getOutOfBoundsTest();
                break;
            case 2:
                currentLevel = getTargetBrickTest();
                break;
            case 3:
                currentLevel = getSiwtchBrickTest();
                break;
            case 4:
                currentLevel = getExplosiveBrickTest();
                break;
            case 5:
                currentLevel = getLevelWithMultipleBalls();
                break;
            case 6:
                currentLevel = getLevelWithOnlyOneLife();
                break;
            case 7:
                currentLevel = getLevelWithOneBrick();
                break;
            case 8:
                currentLevel = getLevelWithBrokenPaddle();
                break;
            case 9:
                currentLevel = getLevelWithFloor();
                break;
            case 10:
                currentLevel = getLevelWithPowerUpBrick();
                break;
            case 11:
                currentLevel = getLevelWithExplosiveAndPowerUpBrick();
                break;
            case 12:
                currentLevel = getLevelWithBrokenPaddlePowerup();
                break;
            case 13:
                currentLevel = getSimpleScoreTestLevel();
                break;
            case 14:
                currentLevel = getLevelWith2AcidBalls();
                break;
            case 15:
                currentLevel = getLevelWith2Floors();
                break;
            case 16:
                currentLevel = getLevelWith2BrokenPaddles();
                break;
            case 17:
                currentLevel = getOneTargetBrickTest();
                break;
//                currentLevel = getLevelWithPowerDownAndExplosive();
//                break;
            case 18:
                currentLevel = getLevelWithFloodPowerDown();
                break;
            case 19:
                currentLevel = getLevelWithProjectile();
                break;
            case 20:
                currentLevel = getLevelWithPowerDownAndExplosive();
                break;
            default:
                // if there is a last level in this factory the liferegeneration can't be tested
                currentLevel = getOneTargetBrickTest();
        }
    }

    public Level getLevelWithPowerDownAndExplosive() {

        ShapeDimension ballShape = shapeRepo.getDefaultBall(50, 40);
        ShapeDimension brickshape1 = new ShapeDimension("brick1", 40, 20, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT);
        ShapeDimension brickshape2 = new ShapeDimension("brick2", 1, 1, 1, 1);
        ShapeDimension brickshape3 = new ShapeDimension("brick3", 50, 20, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT);
        ShapeDimension paddleShape = shapeRepo.getDefaultPaddle();
        Paddle paddle = new Paddle(paddleShape);
        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);

        List<Ball> balls = new ArrayList();
        List<Brick> bricks = new ArrayList();
        Ball b = new Ball(ballShape);
        b.setStartingBall(true);
        balls.add(b);

        Brick powerdownBrick = new Brick(brickshape1);
        powerdownBrick.setPowerdown(new ProjectilePowerDown(new Projectile(new ShapeDimension("projectile", 40, 20, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS))));

        Brick explosiveBrick = new Brick(brickshape3);
        explosiveBrick.addEffect(new ExplosiveEffect(explosiveBrick, 1));

        bricks.add(powerdownBrick);
        bricks.add(explosiveBrick);
        bricks.add(new Brick(brickshape2, true, false));

        LevelState initialState = new LevelState(balls, paddles, bricks);
        Level level = new Level(1, game, initialState);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithProjectile() {

        ShapeDimension ballShape = new ShapeDimension("ball", 30, 20, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS);
        ShapeDimension brickshape1 = new ShapeDimension("brick1", 50, 20, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT);
        ShapeDimension brickshape2 = new ShapeDimension("brick2", 1, 1, 1, 1);
        ShapeDimension paddleShape = shapeRepo.getDefaultPaddle();
        Paddle paddle = new Paddle(paddleShape);
        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);

        List<Ball> balls = new ArrayList();
        List<Brick> bricks = new ArrayList();
        Ball b = new Ball(ballShape);
        b.setStartingBall(true);
        balls.add(b);

        Brick powerdownBrick = new Brick(brickshape1);
        powerdownBrick.setPowerdown(new ProjectilePowerDown(new Projectile(new ShapeDimension("projectile", 50, 20, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS))));

        bricks.add(powerdownBrick);
        bricks.add(new Brick(brickshape2, true, false));

        LevelState initialState = new LevelState(balls, paddles, bricks);
        Level level = new Level(1, game, initialState);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithFloodPowerDown() {

        ShapeDimension ballShape = shapeRepo.getDefaultBall(40, 10);

        ShapeDimension brickshape1 = new ShapeDimension("brick1", 40, 20, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT);
        ShapeDimension brickshape2 = new ShapeDimension("brick2", 1, 1, 1, 1);

        List<Ball> balls = new ArrayList();
        List<Brick> bricks = new ArrayList();
        Ball b = new Ball(ballShape);
        b.setStartingBall(true);
        balls.add(b);

        Brick powerdownBrick = new Brick(brickshape1);
        powerdownBrick.setPowerdown(new FloodPowerDown(b, 5, 15));

        bricks.add(powerdownBrick);
        bricks.add(new Brick(brickshape2, true, false));

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);
        Level level = new Level(1, game, initialState);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWith2BrokenPaddles() {
        ShapeDimension paddleShape = shapeRepo.getDefaultPaddle(35);
        ShapeDimension ballShape = shapeRepo.getDefaultBall(35, 40);

        Paddle paddle = new Paddle(paddleShape);
        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        List<Ball> balls = new ArrayList();
        Ball b = new Ball(ballShape);
        b.setStartingBall(true);
        balls.add(b);

        List<Brick> bricks = new ArrayList();
        Brick brick1 = shapeRepo.getDefaultBrick("brick1", 20, 20);
        Brick brick2 = shapeRepo.getDefaultBrick("brick2", 30, 20);
        Brick brick3 = shapeRepo.getDefaultBrick("brick3", 40, 20);
        Brick brick4 = shapeRepo.getDefaultBrick("brick4", 60, 20);
        brick4.setTarget(true);
        brick4.setVisible(false);
        
        bricks.add(brick1);
        bricks.add(brick2);
        bricks.add(brick3);
        bricks.add(brick4);

        bricks.get(0).setPowerUp(new BrokenPaddlePowerUp(paddle, 1));
        bricks.get(1).addEffect(new ExplosiveEffect(bricks.get(1), 1));
        bricks.get(2).setPowerUp(new BrokenPaddlePowerUp(paddle, 2));

        LevelState initialState = new LevelState(balls, paddles, bricks);
        Level level = new Level(1, game, initialState);
        level.setRunManual(true);
        return level;

    }

    public Level getLevelWith2Floors() {

        ShapeDimension ballShape = new ShapeDimension("ball", 150, 40, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        Ball b = new Ball(ballShape);
        b.setStartingBall(true);
        balls.add(b);

        List<Brick> bricks = new ArrayList();
        ShapeDimension brickshape1 = new ShapeDimension("brick1", 120, 70, 20, 20);
        ShapeDimension brickshape2 = new ShapeDimension("brick2", 145, 70, 20, 20);
        ShapeDimension brickshape3 = new ShapeDimension("brick3", 170, 70, 20, 20);
        ShapeDimension brickshape4 = new ShapeDimension("brick4", 1, 1, 1, 1);
        bricks.add(new Brick(brickshape1));
        bricks.add(new Brick(brickshape2));
        bricks.add(new Brick(brickshape3));
        bricks.add(new Brick(brickshape4, true, false));

        bricks.get(0).setPowerUp(new FloorPowerUp(new ShapeDimension("floor" + 1, 0, 290, BreakoutWorld.DIMENSION, 3)));
        bricks.get(1).addEffect(new ExplosiveEffect(bricks.get(1), 1));
        bricks.get(2).setPowerUp(new FloorPowerUp(new ShapeDimension("floor" + 2, 0, 290, BreakoutWorld.DIMENSION, 3)));

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);
        Level level = new Level(2, game, initialState);
        level.setRunManual(true);
        return level;

    }

    public Level getLevelWith2AcidBalls() {

        ShapeDimension ballShape = new ShapeDimension("ball", 150, 40, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        Ball b = new Ball(ballShape);
        b.setStartingBall(true);
        balls.add(b);

        List<Brick> bricks = new ArrayList();
        ShapeDimension brickshape1 = new ShapeDimension("brick1", 120, 70, 20, 20);
        ShapeDimension brickshape2 = new ShapeDimension("brick2", 145, 70, 20, 20);
        ShapeDimension brickshape3 = new ShapeDimension("brick3", 170, 70, 20, 20);
        ShapeDimension brickshape4 = new ShapeDimension("brick4", 1, 1, 1, 1);
        bricks.add(new Brick(brickshape1));
        bricks.add(new Brick(brickshape2));
        bricks.add(new Brick(brickshape3));
        bricks.add(new Brick(brickshape4, true, false));

        bricks.get(0).setPowerUp(new AcidBallPowerUp("acidball1"));
        bricks.get(1).addEffect(new ExplosiveEffect(bricks.get(1), 1));
        bricks.get(2).setPowerUp(new AcidBallPowerUp("acidball2"));

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);
        Level level = new Level(3, game, initialState);
        level.setRunManual(true);
        return level;

    }

    public Level getSimpleScoreTestLevel() {
        ShapeDimension ballShape = new ShapeDimension("ball", 150, 10, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
        ShapeDimension paddleShape = new ShapeDimension("paddle", 180, 250, 100, 4, Color.BLUE);
        List<Ball> balls = new ArrayList();
        Ball b = new Ball(ballShape);
        b.setStartingBall(true);
        balls.add(b);

        List<Brick> bricks = new ArrayList();
        ShapeDimension brickshape1 = new ShapeDimension("brick1", 150, 70, 20, 20);
        ShapeDimension brickshape2 = new ShapeDimension("brick2", 110, 70, 20, 20);
        ShapeDimension brickshape3 = new ShapeDimension("brick3", 130, 65, 20, 20);
        bricks.add(new Brick(brickshape1));
        bricks.add(new Brick(brickshape2));
        bricks.add(new Brick(brickshape3));

        Paddle paddle = new Paddle(paddleShape);

        LevelState initialState = new LevelState(b, paddle, bricks);
        Level level = new Level(4, game, initialState);
        level.setRunManual(true);
        return level;
    }

    public Level getOutOfBoundsTest() {
//        targetBlocks = 5;
        ShapeDimension paddleShape = shapeRepo.getDefaultPaddle(70);
        ShapeDimension ballShape = shapeRepo.getDefaultBall(10, 90);

        Paddle paddle = new Paddle(paddleShape);
        Ball b = new Ball(ballShape);
        b.setStartingBall(true);

        LevelState initialState = new LevelState(b, paddle, new ArrayList());
        Level level = new Level(5, game, initialState);
        level.setRunManual(true);
        return level;
    }

    public Level getOneTargetBrickTest() {
        ShapeDimension paddleShape = shapeRepo.getDefaultPaddle(75);
        ShapeDimension ballShape = shapeRepo.getDefaultBall();

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);
        ball.setStartingBall(true);

        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new ShapeDimension("targetbrick1", 45f, 40f, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT)));

        bricks.get(0).setTarget(true);

        LevelState initialState = new LevelState(ball, paddle, bricks);
        Level level = new Level(6, game, initialState);
        level.setRunManual(true);
        return level;
    }

    public Level getTargetBrickTest() {
        ShapeDimension paddleShape = shapeRepo.getDefaultPaddle();
        ShapeDimension ballShape = shapeRepo.getDefaultBall();

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);
        ball.setStartingBall(true);
        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new ShapeDimension("targetbrick1", 20, 20, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT)));
        bricks.add(new Brick(new ShapeDimension("targetbrick2", 45, 20, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT)));

        bricks.get(0).setTarget(true);
        bricks.get(1).setTarget(true);

        LevelState initialState = new LevelState(ball, paddle, bricks);
        Level level = new Level(7, game, initialState);
        level.setRunManual(true);
        return level;
    }

    public Level getSiwtchBrickTest() {
//        ShapeDimension paddleShape = new ShapeDimension("paddle", 180, 250, 100, 4, Color.BLUE);
        ShapeDimension paddleShape = shapeRepo.getDefaultPaddle(65);
        ShapeDimension ballShape = shapeRepo.getDefaultBall(70, 50);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);
        ball.setStartingBall(true);
        Brick switchBrick = new Brick(new ShapeDimension("switchbrick", 62f, 40f, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT), false, true, false);
        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new ShapeDimension("targetbrick", 10f, 40f, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT)));
        bricks.add(new Brick(new ShapeDimension("regularbrick", 40f, 40f, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT)));
        bricks.add(switchBrick);

        bricks.get(0).setTarget(true);

        List<Brick> switchTargets = new ArrayList();
        switchTargets.add(bricks.get(1));

        switchBrick.addEffect(new ToggleEffect(switchTargets));

        LevelState initialState = new LevelState(ball, paddle, bricks);
        Level level = new Level(8, game, initialState);
        level.setRunManual(true);
        return level;
    }

    public Level getExplosiveBrickTest() {
        ShapeDimension paddleShape = shapeRepo.getDefaultPaddle();
        ShapeDimension ballShape = shapeRepo.getDefaultBall(65, 50);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);
        ball.setStartingBall(true);

        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new ShapeDimension("targetbrick", 40f, 40f, 20, 10)));
        bricks.add(new Brick(new ShapeDimension("regularbrick", 50f, 40f, 20, 10)));
        Brick explosive = new Brick(new ShapeDimension("explosivebrick", 60f, 40f, 20, 10));
        bricks.add(explosive);
        bricks.add(new Brick(new ShapeDimension("regularbrick", 70f, 40f, 20, 10)));

        bricks.get(0).setTarget(true);

        explosive.addEffect(new ExplosiveEffect(explosive, 1));

        LevelState initialState = new LevelState(ball, paddle, bricks);
        Level level = new Level(9, game, initialState);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithMultipleBalls() {
        ShapeDimension shape;
        List<Ball> balls = new ArrayList();
        for (int i = 0; i < 100; i++) {
            shape = new ShapeDimension("ball" + i, 0 + i, 50, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
            balls.add(new Ball(shape));
        }
        balls.get(0).setStartingBall(true);

        LevelState initialState = new LevelState(balls, new ArrayList(), new ArrayList());
        Level level = new Level(10, game, initialState);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithOnlyOneLife() {
        ShapeDimension ballShape = new ShapeDimension("ball", 70, 250, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape2 = new ShapeDimension("ball2", 70, 290, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        balls.add(new Ball(ballShape));
        balls.add(new Ball(ballShape2));
        balls.get(0).setStartingBall(true);

        LevelState initialState = new LevelState(balls, new ArrayList(), new ArrayList());
        Level level = new Level(11, game, initialState);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithOneBrick() {
        ShapeDimension ballShape = new ShapeDimension("ball", 70, 125, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        balls.add(new Ball(ballShape));
        balls.get(0).setStartingBall(true);
        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new ShapeDimension("regularbrick", 70, 100, 20, 10)));

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);
        Level level = new Level(12, game, initialState);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithBrokenPaddle() {

        ShapeDimension basePaddleShape = shapeRepo.getDefaultPaddle();
        Paddle basePaddle = new Paddle(basePaddleShape);

        BrokenPaddlePowerUp bp = new BrokenPaddlePowerUp(basePaddle, 0);
        List<Paddle> brokenPaddle = bp.getBrokenPaddle();

        ShapeDimension ballShape = new ShapeDimension("ball1", brokenPaddle.get(0).getX(), 60, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape2 = new ShapeDimension("ball2", brokenPaddle.get(0).getX() + brokenPaddle.get(0).getWidth(), 60, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape3 = new ShapeDimension("ball3", brokenPaddle.get(1).getX(), 60, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);

        List<Ball> balls = new ArrayList();
        balls.add(new Ball(ballShape));
        balls.add(new Ball(ballShape2));
        balls.add(new Ball(ballShape3));
        balls.get(0).setStartingBall(true);
        LevelState initialState = new LevelState(balls, brokenPaddle, new ArrayList());
        Level level = new Level(13, game, initialState);
        level.setRunManual(true);
        return level;
    }

    private Level getLevelWithFloor() {
        ShapeDimension ballShape = new ShapeDimension("ball", 70, 250, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape2 = new ShapeDimension("ball2", 70, 150, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);

        List<Ball> balls = new ArrayList();
        balls.add(new Ball(ballShape));
        balls.add(new Ball(ballShape2));
        balls.get(0).setStartingBall(true);
        LevelState initialState = new LevelState(balls, new ArrayList(), new ArrayList());

        Level level = new Level(14, game, initialState);
        level.setRunManual(true);
        return level;
    }

    private Level getLevelWithPowerUpBrick() {
        ShapeDimension ballShape = new ShapeDimension("ball", 70, 130, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        balls.add(new Ball(ballShape));
        ShapeDimension floorShape = new ShapeDimension("floor", 0, BreakoutWorld.DIMENSION - 10, BreakoutWorld.DIMENSION, 3);
        balls.get(0).setStartingBall(true);
        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new ShapeDimension("regularbrick", 70, 100, 20, 10), false, true));
        bricks.add(new Brick(new ShapeDimension("targetbrick", 150, 100, 20, 10), true, true));
        bricks.get(0).setPowerUp(new FloorPowerUp(floorShape));

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);

        Level level = new Level(15, game, initialState);
        level.setRunManual(true);
        return level;

    }

    private Level getLevelWithExplosiveAndPowerUpBrick() {
//        ShapeDimension ballShape = new ShapeDimension("ball", 70, 50, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape = shapeRepo.getDefaultBall();
        Paddle paddle = new Paddle(shapeRepo.getDefaultPaddle());

        List<Ball> balls = new ArrayList();
        List<Paddle> paddles = new ArrayList();

        balls.add(new Ball(ballShape));
        paddles.add(paddle);

        ShapeDimension floorShape = shapeRepo.getDefaultFloor();
        balls.get(0).setStartingBall(true);
        List<Brick> bricks = new ArrayList();

        bricks.add(shapeRepo.getDefaultBrick("regularBrick", 35, 40));
        bricks.add(shapeRepo.getDefaultBrick("explosiveBrick", 45, 40));

        bricks.get(0).setPowerUp(new FloorPowerUp(floorShape));
        bricks.get(1).addEffect(new ExplosiveEffect(bricks.get(1), 1));

        LevelState initialState = new LevelState(balls, paddles, bricks);

        Level level = new Level(16, game, initialState);
        level.setRunManual(true);
        return level;

    }

    public Level getLevelWithBrokenPaddlePowerup() {
        ShapeDimension basePaddleShape = new ShapeDimension("paddle", 70, 250, 100, 4);
        Paddle basePaddle = new Paddle(basePaddleShape);
        ShapeDimension ballShape = new ShapeDimension("ball", 70, 200, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape2 = new ShapeDimension("ball2", 30, 200, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape3 = new ShapeDimension("ball3", 295, 200, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        balls.add(new Ball(ballShape));
        balls.add(new Ball(ballShape2));
        balls.add(new Ball(ballShape3));
        balls.get(0).setStartingBall(true);
        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new ShapeDimension("regularbrick", 70, 150, 20, 10)));
        bricks.add(new Brick(new ShapeDimension("regularbrick2", 1, 1, 1, 1), true, true));
        bricks.get(0).setPowerUp(new BrokenPaddlePowerUp(basePaddle, 1));
        bricks.get(1).setTarget(true);

        List<Paddle> paddles = new ArrayList();
        paddles.add(basePaddle);

        LevelState initialState = new LevelState(balls, paddles, bricks);

        Level level = new Level(17, game, initialState);
        level.setRunManual(true);
        return level;

    }
}
