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
import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.effects.PowerUpType;
import com.breakoutegypt.domain.effects.ToggleEffect;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.BrokenPaddle;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class TestLevelFactory extends LevelFactory {

    public TestLevelFactory(Game game) {
        super(game, 1000);
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
            case 10:
                currentLevel = getLevelWithPowerUpBrick();
        }
    }

    public Level getOutOfBoundsTest() {
//        targetBlocks = 5;
        ShapeDimension paddleShape = new ShapeDimension("paddle", 180, 250, 100, 4, Color.BLUE);
        ShapeDimension ballShape = new ShapeDimension("ball2", 45, 250, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);

        LevelState initialState = new LevelState(ball, paddle, new ArrayList());
        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;
    }

    public Level getTargetBrickTest() {
        ShapeDimension paddleShape = new ShapeDimension("paddle", 180, 250, 100, 4, Color.BLUE);
        ShapeDimension ballShape = new ShapeDimension("ball", 50, 100, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);

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

        Brick switchBrick = new Brick(new ShapeDimension("switchbrick", 62f, 40f, 20, 10), new Point(1, 2), false, true, false);
        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new ShapeDimension("targetbrick", 10f, 40f, 20, 10), new Point(0, 1)));
        bricks.add(new Brick(new ShapeDimension("regularbrick", 40f, 40f, 20, 10), new Point(1, 1)));
        bricks.add(switchBrick);

        bricks.get(0).setTarget(true);

        List<Brick> switchTaregts = new ArrayList();
        switchTaregts.add(bricks.get(1));

        switchBrick.addEffect(new ToggleEffect(switchTaregts));

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
            shape = new ShapeDimension("ball" + i, 70 + i, 100, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
            balls.add(new Ball(shape));
        }

//        ShapeDimension ballShape = new ShapeDimension("ball", 70, 100, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
//        ShapeDimension ballShape2 = new ShapeDimension("ball2", 70, 150, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
//        
//        balls.add(new Ball(ballShape));
//        balls.add(new Ball(ballShape2));
        LevelState initialState = new LevelState(balls, new ArrayList(), new ArrayList());
        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithOnlyOneLife() {
        ShapeDimension ballShape = new ShapeDimension("ball", 70, 100, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape2 = new ShapeDimension("ball2", 70, 150, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        balls.add(new Ball(ballShape));
        balls.add(new Ball(ballShape2));

        LevelState initialState = new LevelState(balls, new ArrayList(), new ArrayList());
        Level level = new Level(1, game, initialState, 1);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithOneBrick() {
        ShapeDimension ballShape = new ShapeDimension("ball", 70, 125, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        balls.add(new Ball(ballShape));

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

        BrokenPaddle bp = new BrokenPaddle(basePaddle);
        List<Paddle> brokenPaddle = bp.getBrokenPaddle();

        ShapeDimension ballShape = new ShapeDimension("ball", brokenPaddle.get(0).getShape().getPosX(), 200, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape2 = new ShapeDimension("ball2", brokenPaddle.get(0).getShape().getPosX() + brokenPaddle.get(0).getShape().getWidth(), 200, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape3 = new ShapeDimension("ball3", brokenPaddle.get(1).getShape().getPosX(), 200, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        balls.add(new Ball(ballShape));
        balls.add(new Ball(ballShape2));
        balls.add(new Ball(ballShape3));

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

        LevelState initialState = new LevelState(balls, new ArrayList(), new ArrayList());

        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;
    }

    private Level getLevelWithPowerUpBrick() {
        ShapeDimension ballShape = new ShapeDimension("ball", 70, 50, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        List<Ball> balls = new ArrayList();
        balls.add(new Ball(ballShape));
        
        
        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new ShapeDimension("regularbrick", 70, 100, 20, 10), new Point(0, 1)));
        bricks.get(0).setPowerUp(PowerUpType.FLOOR);
        
        
        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);

        Level level = new Level(1, game, initialState, 3);
        level.setRunManual(true);
        return level;
        
    }
}
