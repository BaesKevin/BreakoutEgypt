/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.levelfactories;

import com.breakoutegypt.domain.shapes.BodyConfigurationFactory;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.powers.AcidBallPowerUp;
import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.powers.PowerUpType;
import com.breakoutegypt.domain.effects.ToggleEffect;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.powers.BrokenPaddlePowerUp;
import com.breakoutegypt.domain.powers.FloodPowerDown;
import com.breakoutegypt.domain.powers.FloorPowerUp;
import com.breakoutegypt.domain.powers.ProjectilePowerDown;
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
            case 18:
                currentLevel = getLevelWithFloodPowerDown();
                break;
            case 19:
                currentLevel = getLevelWithProjectile();
                break;
            case 20:
                currentLevel = getLevelWithPowerDownAndExplosive();
        }
    }
    
    public Level getLevelWithPowerDownAndExplosive() {
        
        
        ShapeDimension ballShape = new ShapeDimension("ball", 120, 40, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS);
        ShapeDimension brickshape1 = new ShapeDimension("brick1", 120, 80, 20, 20);
        ShapeDimension brickshape2 = new ShapeDimension("brick2", 1, 1, 1, 1);
        ShapeDimension brickshape3 = new ShapeDimension("brick3", 95, 80, 20, 20);
        ShapeDimension paddleShape = new ShapeDimension("paddle", 45, 250, 100, 4, Color.BLUE);
        Paddle paddle = new Paddle(paddleShape);
        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        

        List<Ball> balls = new ArrayList();
        List<Brick> bricks = new ArrayList();
        Ball b = new Ball(ballShape);
        b.setStartingBall(true);
        balls.add(b);

        Brick powerdownBrick = new Brick(brickshape1, new Point());
        powerdownBrick.setPowerdown(new ProjectilePowerDown(new Projectile(new ShapeDimension("projectile", 120, 80, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS))));

        Brick explosiveBrick = new Brick(brickshape3, new Point(0,1));
        explosiveBrick.addEffect(new ExplosiveEffect(explosiveBrick, 1));
        
        bricks.add(powerdownBrick);
        bricks.add(explosiveBrick);
        bricks.add(new Brick(brickshape2, new Point(9, 9), true, false));

        LevelState initialState = new LevelState(balls, paddles, bricks);
        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithProjectile() {

        ShapeDimension ballShape = new ShapeDimension("ball", 120, 40, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS);
        ShapeDimension brickshape1 = new ShapeDimension("brick1", 120, 80, 20, 20);
        ShapeDimension brickshape2 = new ShapeDimension("brick2", 1, 1, 1, 1);
        ShapeDimension paddleShape = new ShapeDimension("paddle", 45, 250, 100, 4, Color.BLUE);
        Paddle paddle = new Paddle(paddleShape);
        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);

        List<Ball> balls = new ArrayList();
        List<Brick> bricks = new ArrayList();
        Ball b = new Ball(ballShape);
        b.setStartingBall(true);
        balls.add(b);

        Brick powerdownBrick = new Brick(brickshape1, new Point());
        powerdownBrick.setPowerdown(new ProjectilePowerDown(new Projectile(new ShapeDimension("projectile", 120, 80, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS))));

        bricks.add(powerdownBrick);
        bricks.add(new Brick(brickshape2, new Point(9, 9), true, false));

        LevelState initialState = new LevelState(balls, paddles, bricks);
        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithFloodPowerDown() {
        ShapeDimension ballShape = new ShapeDimension("ball", 120, 40, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS);
        ShapeDimension brickshape1 = new ShapeDimension("brick1", 120, 80, 20, 20);
        ShapeDimension brickshape2 = new ShapeDimension("brick2", 1, 1, 1, 1);

        List<Ball> balls = new ArrayList();
        List<Brick> bricks = new ArrayList();
        Ball b = new Ball(ballShape);
        b.setStartingBall(true);
        balls.add(b);

        Brick powerdownBrick = new Brick(brickshape1, new Point());
        powerdownBrick.setPowerdown(new FloodPowerDown(b, 5));

        bricks.add(powerdownBrick);
        bricks.add(new Brick(brickshape2, new Point(9, 9), true, false));

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);
        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWith2BrokenPaddles() {
        ShapeDimension paddleShape = new ShapeDimension("paddle", 180, 250, 100, 4, Color.BLUE);
        ShapeDimension ballShape = new ShapeDimension("ball", 150, 40, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        List<Ball> balls = new ArrayList();
        Ball b = new Ball(ballShape);
        b.setStartingBall(true);
        balls.add(b);

        List<Brick> bricks = new ArrayList();
        ShapeDimension brickshape1 = new ShapeDimension("brick1", 120, 70, 20, 20);
        ShapeDimension brickshape2 = new ShapeDimension("brick2", 145, 70, 20, 20);
        ShapeDimension brickshape3 = new ShapeDimension("brick3", 170, 70, 20, 20);
        ShapeDimension brickshape4 = new ShapeDimension("brick4", 1, 1, 1, 1);
        bricks.add(new Brick(brickshape1, new Point(1, 1)));
        bricks.add(new Brick(brickshape2, new Point(0, 1)));
        bricks.add(new Brick(brickshape3, new Point(1, 0)));
        bricks.add(new Brick(brickshape4, new Point(9, 9), true, false));

        bricks.get(0).setPowerUp(new BrokenPaddlePowerUp(paddle, 1));
        bricks.get(1).addEffect(new ExplosiveEffect(bricks.get(1), 1));
        bricks.get(2).setPowerUp(new BrokenPaddlePowerUp(paddle, 2));

        LevelState initialState = new LevelState(balls, paddles, bricks);
        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;

    }

    public Level getLevelWith2Floors() {

        ShapeDimension ballShape = new ShapeDimension("ball", 150, 40, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        Ball b = new Ball(ballShape);
        b.setStartingBall(true);
        balls.add(b);

        List<Brick> bricks = new ArrayList();
        ShapeDimension brickshape1 = new ShapeDimension("brick1", 120, 70, 20, 20);
        ShapeDimension brickshape2 = new ShapeDimension("brick2", 145, 70, 20, 20);
        ShapeDimension brickshape3 = new ShapeDimension("brick3", 170, 70, 20, 20);
        ShapeDimension brickshape4 = new ShapeDimension("brick4", 1, 1, 1, 1);
        bricks.add(new Brick(brickshape1, new Point(1, 1)));
        bricks.add(new Brick(brickshape2, new Point(0, 1)));
        bricks.add(new Brick(brickshape3, new Point(1, 0)));
        bricks.add(new Brick(brickshape4, new Point(9, 9), true, false));

        bricks.get(0).setPowerUp(new FloorPowerUp(new ShapeDimension("floor" + 1, 0, 290, 300, 3)));
        bricks.get(1).addEffect(new ExplosiveEffect(bricks.get(1), 1));
        bricks.get(2).setPowerUp(new FloorPowerUp(new ShapeDimension("floor" + 2, 0, 290, 300, 3)));

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);
        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;

    }

    public Level getLevelWith2AcidBalls() {

        ShapeDimension ballShape = new ShapeDimension("ball", 150, 40, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        Ball b = new Ball(ballShape);
        b.setStartingBall(true);
        balls.add(b);

        List<Brick> bricks = new ArrayList();
        ShapeDimension brickshape1 = new ShapeDimension("brick1", 120, 70, 20, 20);
        ShapeDimension brickshape2 = new ShapeDimension("brick2", 145, 70, 20, 20);
        ShapeDimension brickshape3 = new ShapeDimension("brick3", 170, 70, 20, 20);
        ShapeDimension brickshape4 = new ShapeDimension("brick4", 1, 1, 1, 1);
        bricks.add(new Brick(brickshape1, new Point(1, 1)));
        bricks.add(new Brick(brickshape2, new Point(0, 1)));
        bricks.add(new Brick(brickshape3, new Point(1, 0)));
        bricks.add(new Brick(brickshape4, new Point(9, 9), true, false));

        bricks.get(0).setPowerUp(new AcidBallPowerUp("acidball1"));
        bricks.get(1).addEffect(new ExplosiveEffect(bricks.get(1), 1));
        bricks.get(2).setPowerUp(new AcidBallPowerUp("acidball2"));

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);
        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;

    }

    public Level getSimpleScoreTestLevel() {
        ShapeDimension ballShape = new ShapeDimension("ball", 150, 10, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        Ball b = new Ball(ballShape);
        b.setStartingBall(true);
        balls.add(b);

        List<Brick> bricks = new ArrayList();
        ShapeDimension brickshape1 = new ShapeDimension("brick1", 150, 70, 20, 20);
        ShapeDimension brickshape2 = new ShapeDimension("brick2", 110, 70, 20, 20);
        ShapeDimension brickshape3 = new ShapeDimension("brick3", 130, 65, 20, 20);
        bricks.add(new Brick(brickshape1, new Point(1, 1)));
        bricks.add(new Brick(brickshape2, new Point(0, 1)));
        bricks.add(new Brick(brickshape3, new Point(1, 0)));

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);
        Level level = new Level(1, game, initialState, 1);
        level.setRunManual(true);
        return level;
    }

    public Level getOutOfBoundsTest() {
//        targetBlocks = 5;
        ShapeDimension paddleShape = new ShapeDimension("paddle", 180, 250, 100, 4, Color.BLUE);
        ShapeDimension ballShape = new ShapeDimension("ball2", 45, 250, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        Ball b = new Ball(ballShape);
        b.setStartingBall(true);

        LevelState initialState = new LevelState(b, paddle, new ArrayList());
        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;
    }

    public Level getOneTargetBrickTest() {
        ShapeDimension paddleShape = new ShapeDimension("paddle", 180, 250, 100, 4, Color.BLUE);
        ShapeDimension ballShape = new ShapeDimension("ball", 50, 100, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);
        ball.setStartingBall(true);

        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new ShapeDimension("targetbrick1", 40f, 40f, 20, 10), new Point(1, 1)));

        bricks.get(0).setTarget(true);

        LevelState initialState = new LevelState(ball, paddle, bricks);
        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;
    }

    public Level getTargetBrickTest() {
        ShapeDimension paddleShape = new ShapeDimension("paddle", 180, 250, 100, 4, Color.BLUE);
        ShapeDimension ballShape = new ShapeDimension("ball", 50, 100, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);
        ball.setStartingBall(true);
        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new ShapeDimension("targetbrick1", 40f, 40f, 20, 10), new Point(1, 1)));
        bricks.add(new Brick(new ShapeDimension("targetbrick2", 62f, 40f, 20, 10), new Point(1, 2)));

        bricks.get(0).setTarget(true);
        bricks.get(1).setTarget(true);

        LevelState initialState = new LevelState(ball, paddle, bricks);
        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;
    }

    public Level getSiwtchBrickTest() {
        ShapeDimension paddleShape = new ShapeDimension("paddle", 180, 250, 100, 4, Color.BLUE);
        ShapeDimension ballShape = new ShapeDimension("ball", 70, 100, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);
        ball.setStartingBall(true);
        Brick switchBrick = new Brick(new ShapeDimension("switchbrick", 62f, 40f, 20, 10), new Point(1, 2), false, true, false);
        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new ShapeDimension("targetbrick", 10f, 40f, 20, 10), new Point(0, 1)));
        bricks.add(new Brick(new ShapeDimension("regularbrick", 40f, 40f, 20, 10), new Point(1, 1)));
        bricks.add(switchBrick);

        bricks.get(0).setTarget(true);

        List<Brick> switchTargets = new ArrayList();
        switchTargets.add(bricks.get(1));

        switchBrick.addEffect(new ToggleEffect(switchTargets));

        LevelState initialState = new LevelState(ball, paddle, bricks);
        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;
    }

    public Level getExplosiveBrickTest() {
        ShapeDimension paddleShape = new ShapeDimension("paddle", 180, 250, 100, 4, Color.BLUE);
        ShapeDimension ballShape = new ShapeDimension("ball", 70, 100, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);
        ball.setStartingBall(true);
        Brick explosive = new Brick(new ShapeDimension("explosivebrick", 62f, 40f, 20, 10), new Point(2, 1));

        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new ShapeDimension("targetbrick", 10f, 40f, 20, 10), new Point(0, 1)));
        bricks.add(new Brick(new ShapeDimension("regularbrick", 40f, 40f, 20, 10), new Point(1, 1)));
        bricks.add(explosive);
        bricks.add(new Brick(new ShapeDimension("regularbrick", 84f, 40f, 20, 10), new Point(3, 1)));

        bricks.get(0).setTarget(true);

        explosive.addEffect(new ExplosiveEffect(explosive, 1));

        LevelState initialState = new LevelState(ball, paddle, bricks);
        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithMultipleBalls() {
        ShapeDimension shape;
        List<Ball> balls = new ArrayList();
        for (int i = 0; i < 100; i++) {
            shape = new ShapeDimension("ball" + i, 70 + i, 250, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
            balls.add(new Ball(shape));
        }
        balls.get(0).setStartingBall(true);

        LevelState initialState = new LevelState(balls, new ArrayList(), new ArrayList());
        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithOnlyOneLife() {
        ShapeDimension ballShape = new ShapeDimension("ball", 70, 250, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape2 = new ShapeDimension("ball2", 70, 290, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        balls.add(new Ball(ballShape));
        balls.add(new Ball(ballShape2));
        balls.get(0).setStartingBall(true);

        LevelState initialState = new LevelState(balls, new ArrayList(), new ArrayList());
        Level level = new Level(1, game, initialState, 1);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithOneBrick() {
        ShapeDimension ballShape = new ShapeDimension("ball", 70, 125, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        balls.add(new Ball(ballShape));
        balls.get(0).setStartingBall(true);
        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new ShapeDimension("regularbrick", 70, 100, 20, 10), new Point(0, 1)));

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);
        Level level = new Level(1, game, initialState, 1);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithBrokenPaddle() {

        ShapeDimension basePaddleShape = new ShapeDimension("paddle", 70, 250, 100, 4);
        Paddle basePaddle = new Paddle(basePaddleShape);

        BrokenPaddlePowerUp bp = new BrokenPaddlePowerUp(basePaddle, 0);
        List<Paddle> brokenPaddle = bp.getBrokenPaddle();

        ShapeDimension ballShape = new ShapeDimension("ball", brokenPaddle.get(0).getShape().getPosX(), 200, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape2 = new ShapeDimension("ball2", brokenPaddle.get(0).getShape().getPosX() + brokenPaddle.get(0).getShape().getWidth(), 200, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape3 = new ShapeDimension("ball3", brokenPaddle.get(1).getShape().getPosX(), 200, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        balls.add(new Ball(ballShape));
        balls.add(new Ball(ballShape2));
        balls.add(new Ball(ballShape3));
        balls.get(0).setStartingBall(true);
        LevelState initialState = new LevelState(balls, brokenPaddle, new ArrayList());
        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;
    }

    private Level getLevelWithFloor() {
        ShapeDimension ballShape = new ShapeDimension("ball", 70, 250, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape2 = new ShapeDimension("ball2", 70, 150, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);

        List<Ball> balls = new ArrayList();
        balls.add(new Ball(ballShape));
        balls.add(new Ball(ballShape2));
        balls.get(0).setStartingBall(true);
        LevelState initialState = new LevelState(balls, new ArrayList(), new ArrayList());

        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;
    }

    private Level getLevelWithPowerUpBrick() {
        ShapeDimension ballShape = new ShapeDimension("ball", 70, 130, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        balls.add(new Ball(ballShape));
        ShapeDimension floorShape = new ShapeDimension("floor", 0, 290, 300, 3);
        balls.get(0).setStartingBall(true);
        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new ShapeDimension("regularbrick", 70, 100, 20, 10), new Point(0, 1), false, true));
        bricks.add(new Brick(new ShapeDimension("targetbrick", 150, 100, 20, 10), new Point(0, 1), true, true));
        bricks.get(0).setPowerUp(new FloorPowerUp(floorShape));

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);

        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;

    }

    private Level getLevelWithExplosiveAndPowerUpBrick() {
        ShapeDimension ballShape = new ShapeDimension("ball", 70, 50, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        balls.add(new Ball(ballShape));
        ShapeDimension floorShape = new ShapeDimension("floor", 0, 290, 300, 3);
        balls.get(0).setStartingBall(true);
        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new ShapeDimension("regularbrick", 100, 100, 20, 10), new Point(0, 1)));
        bricks.add(new Brick(new ShapeDimension("explosivebrick", 70, 100, 20, 10), new Point(1, 1)));
        bricks.get(0).setPowerUp(new FloorPowerUp(floorShape));
        bricks.get(1).addEffect(new ExplosiveEffect(bricks.get(1), 1));
        bricks.get(1).setType(BrickType.EXPLOSIVE);

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);

        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;

    }

    public Level getLevelWithBrokenPaddlePowerup() {
        ShapeDimension basePaddleShape = new ShapeDimension("paddle", 70, 250, 100, 4);
        Paddle basePaddle = new Paddle(basePaddleShape);
        ShapeDimension ballShape = new ShapeDimension("ball", 70, 200, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape2 = new ShapeDimension("ball2", 30, 200, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape3 = new ShapeDimension("ball3", 295, 200, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        balls.add(new Ball(ballShape));
        balls.add(new Ball(ballShape2));
        balls.add(new Ball(ballShape3));
        balls.get(0).setStartingBall(true);
        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new ShapeDimension("regularbrick", 70, 150, 20, 10), new Point(0, 1)));
        bricks.add(new Brick(new ShapeDimension("regularbrick2", 1, 1, 1, 1), new Point(1, 1), true, true));
        bricks.get(0).setPowerUp(new BrokenPaddlePowerUp(basePaddle, 1));
        bricks.get(1).setTarget(true);

        List<Paddle> paddles = new ArrayList();
        paddles.add(basePaddle);

        LevelState initialState = new LevelState(balls, paddles, bricks);

        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;

    }
}
