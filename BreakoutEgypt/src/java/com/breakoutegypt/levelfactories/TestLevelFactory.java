/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.levelfactories;

import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelPack;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.powers.AcidBallPowerUp;
import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.effects.ToggleEffect;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.powers.BrokenPaddlePowerUp;
import com.breakoutegypt.domain.powers.FloodPowerDown;
import com.breakoutegypt.domain.powers.FloorPowerUp;
import com.breakoutegypt.domain.powers.ProjectilePowerDown;
import com.breakoutegypt.domain.shapes.DimensionDefaults;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class TestLevelFactory extends LevelFactory {

    public static final int DEFAULT_OPEN_LEVELS = 500;
    public final int totalLevels = 1000;
    private Difficulty difficulty = Repositories.getDifficultyRepository().findByName(Difficulty.EASY);

    public TestLevelFactory(Game game) {
        super(game, 1000, DEFAULT_OPEN_LEVELS, "test");
    }

    @Override
    public Level getCurrentLevel() {
        return currentLevel;
    }

//    @Override
//    public LevelPack createLevelPack() {
//        List<Level> levels = new ArrayList();
//        levels.add(getOutOfBoundsTest());
//        levels.add(getTargetBrickTest());
//        levels.add(getSiwtchBrickTest());
//        levels.add(getExplosiveBrickTest());
//        levels.add(getLevelWithMultipleBalls());
//        levels.add(getLevelWithOnlyOneLife());
//        levels.add(getLevelWithOneBrick());
//        levels.add(getLevelWithBrokenPaddle());
//        levels.add(getLevelWithFloor());
//        levels.add(getLevelWithPowerUpBrick());
//        levels.add(getLevelWithExplosiveAndPowerUpBrick());
//        levels.add(getLevelWithBrokenPaddlePowerup());
//        levels.add(getSimpleScoreTestLevel());
//        levels.add(getLevelWith2AcidBalls());
//        levels.add(getLevelWith2Floors());
//        levels.add(getLevelWith2BrokenPaddles());
//        levels.add(getOneTargetBrickTest());
//        levels.add(getLevelWithFloodPowerDown());
//        levels.add(getLevelWithProjectile());
//        levels.add(getLevelWithPowerDownAndExplosive());
//
//        return new LevelPack("test", "Testlevels", levels, 500, 1000);
//
//    }
//    @Override
//    public void initializeLevels() {
//        if (pack == null) {
//            pack = levelPackRepo.getByName("test", game);
//
//            if (pack == null) {
//                List<Level> levels = new ArrayList();
//                levels.add(getOutOfBoundsTest());
//                levels.add(getTargetBrickTest());
//                levels.add(getSiwtchBrickTest());
//                levels.add(getExplosiveBrickTest());
//                levels.add(getLevelWithMultipleBalls());
//                levels.add(getLevelWithOnlyOneLife());
//                levels.add(getLevelWithOneBrick());
//                levels.add(getLevelWithBrokenPaddle());
//                levels.add(getLevelWithFloor());
//                levels.add(getLevelWithPowerUpBrick());
//                levels.add(getLevelWithExplosiveAndPowerUpBrick());
//                levels.add(getLevelWithBrokenPaddlePowerup());
//                levels.add(getSimpleScoreTestLevel());
//                levels.add(getLevelWith2AcidBalls());
//                levels.add(getLevelWith2Floors());
//                levels.add(getLevelWith2BrokenPaddles());
//                levels.add(getOneTargetBrickTest());
//                levels.add(getLevelWithFloodPowerDown());
//                levels.add(getLevelWithProjectile());
//                levels.add(getLevelWithPowerDownAndExplosive());
//
//                pack = new LevelPack("test", "Testlevels", levels, 500, 1000);
//                levelPackRepo.add(pack);
//            }
//        }
//    }
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
                    break;
                case 21:
                    currentLevel = getLevelWith2Paddles();
                    break;
                default:
                    // if there is a last level in this factory the liferegeneration can't be tested
                    currentLevel = getOneTargetBrickTest();
            }
        
        

    }

    public Level getLevelWith2Paddles() {

//        ShapeDimension ballShape = new ShapeDimension("ball", 60, 200, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
//        ShapeDimension ballShape2 = new ShapeDimension("ball", 60, 150, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
        Ball ball = shapeRepo.getDefaultBall();
        Ball ball2 = shapeRepo.getDefaultBall("ball2", 50, 30);

//        Paddle paddle = new Paddle(new ShapeDimension("paddle1", 45, 250, 100, 4, Color.BLUE));
//        Paddle paddle2 = new Paddle(new ShapeDimension("paddle2", 45, 100, 100, 4, Color.BLUE));
        Paddle paddle = shapeRepo.getDefaultPaddle("paddle1", 50, 80);
        Paddle paddle2 = shapeRepo.getDefaultPaddle("paddle2", 50, 10);

        paddle2.setPlayerIndex(2);
        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        paddles.add(paddle2);

        List<Ball> balls = new ArrayList();
//        Ball ball = new Ball(ballShape);
//        Ball ball2 = new Ball(ballShape2);
        ball2.setPlayerIndex(2);
        ball.setStartingBall(true);
        ball2.setStartingBall(true);

//        ball.setStartingBall(true);
        balls.add(ball);
        balls.add(ball2);

        LevelState initialState = new LevelState(balls, paddles, new ArrayList(), Repositories.getDifficultyRepository().findByName(Difficulty.EASY), false, true);
        Level level = new Level(currentLevelId, game, initialState);
        level.setLevelNumber(21);
        return level;
    }

    public Level getLevelWithPowerDownAndExplosive() {

        Ball b = shapeRepo.getDefaultBall(50, 40);
        Brick powerdownBrick = shapeRepo.getDefaultBrick("brick1", 40, 20);
        Brick targetBrick = shapeRepo.getDefaultBrick("brick2", 1, 1, true, false);
        Brick explosiveBrick = shapeRepo.getDefaultBrick("brick3", 50, 20);

        Paddle paddle = shapeRepo.getDefaultPaddle();
        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);

        List<Ball> balls = new ArrayList();
        List<Brick> bricks = new ArrayList();

        b.setStartingBall(true);
        balls.add(b);

        powerdownBrick.setPowerdown(new ProjectilePowerDown(shapeRepo.getProjectile(40, 20)));
        explosiveBrick.addEffect(new ExplosiveEffect(explosiveBrick, 1));

        bricks.add(powerdownBrick);
        bricks.add(explosiveBrick);
        bricks.add(targetBrick);

        LevelState initialState = new LevelState(balls, paddles, bricks);
        Level level = new Level(1, game, initialState);
        level.setLevelNumber(20);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithProjectile() {

        Ball b = shapeRepo.getDefaultBall(30, 50);
        Paddle paddle = shapeRepo.getDefaultPaddle();

        Brick powerdownBrick = shapeRepo.getDefaultBrick("brick1", 30, 20);
        Brick targetBrick = shapeRepo.getDefaultBrick("brick2", 1, 1, true);

        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);

        List<Ball> balls = new ArrayList();
        List<Brick> bricks = new ArrayList();

        b.setStartingBall(true);
        balls.add(b);

        powerdownBrick.setPowerdown(new ProjectilePowerDown(shapeRepo.getProjectile(50, 20)));

        bricks.add(powerdownBrick);
        bricks.add(targetBrick);

        LevelState initialState = new LevelState(balls, paddles, bricks);
        Level level = new Level(1, game, initialState);
        level.setLevelNumber(19);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithFloodPowerDown() {

        Ball b = shapeRepo.getDefaultBall(40, 10);

        Brick powerdownBrick = shapeRepo.getDefaultBrick("floodBrick", 40, 20);
        Brick targetBrick = shapeRepo.getDefaultBrick("target", 1, 1);

        List<Ball> balls = new ArrayList();
        List<Brick> bricks = new ArrayList();

        b.setStartingBall(true);
        balls.add(b);

        powerdownBrick.setPowerdown(new FloodPowerDown(b, 5));

        bricks.add(powerdownBrick);
        bricks.add(targetBrick);

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);
        Level level = new Level(1, game, initialState);
        level.setLevelNumber(18);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWith2BrokenPaddles() {
        Ball b = shapeRepo.getDefaultBall(35, 10);
        Paddle paddle = shapeRepo.getDefaultPaddle();

        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        List<Ball> balls = new ArrayList();

        b.setStartingBall(true);
        balls.add(b);

        List<Brick> bricks = new ArrayList();
        Brick brick1 = shapeRepo.getDefaultBrick("brick1", 20, 20);
        Brick brick2 = shapeRepo.getDefaultBrick("brick2", 30, 20);
        Brick brick3 = shapeRepo.getDefaultBrick("brick3", 40, 20);
        Brick brick4 = shapeRepo.getDefaultBrick("brick4", 60, 20, true, false);

        bricks.add(brick1);
        bricks.add(brick2);
        bricks.add(brick3);
        bricks.add(brick4);

        bricks.get(0).setPowerUp(new BrokenPaddlePowerUp(paddle, 1, difficulty.getPowerupTime()));
        bricks.get(1).addEffect(new ExplosiveEffect(bricks.get(1), 1));
        bricks.get(2).setPowerUp(new BrokenPaddlePowerUp(paddle, 2, difficulty.getPowerupTime()));

        LevelState initialState = new LevelState(balls, paddles, bricks);
        Level level = new Level(1, game, initialState);
        level.setLevelNumber(16);
        level.setRunManual(true);
        return level;

    }

    public Level getLevelWith2Floors() {

        Ball b = shapeRepo.getDefaultBall(55, 10);

        List<Ball> balls = new ArrayList();

        b.setStartingBall(true);
        balls.add(b);

        List<Brick> bricks = new ArrayList();

        Brick brick1 = shapeRepo.getDefaultBrick("brick1", 40, 20);
        Brick brick2 = shapeRepo.getDefaultBrick("brick2", 50, 20);
        Brick brick3 = shapeRepo.getDefaultBrick("brick3", 60, 20);
        Brick brick4 = shapeRepo.getDefaultBrick("brick4", 70, 20, true, false);

        bricks.add(brick1);
        bricks.add(brick2);
        bricks.add(brick3);
        bricks.add(brick4);

        bricks.get(0).setPowerUp(new FloorPowerUp(shapeRepo.getDefaultFloor("floor1"), difficulty.getPowerupTime()));
        bricks.get(1).addEffect(new ExplosiveEffect(bricks.get(1), 1));
        bricks.get(2).setPowerUp(new FloorPowerUp(shapeRepo.getDefaultFloor("floor2"), difficulty.getPowerupTime()));

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);
        Level level = new Level(2, game, initialState);
        level.setLevelNumber(15);
        level.setRunManual(true);
        return level;

    }

    public Level getLevelWith2AcidBalls() {

        Ball b = shapeRepo.getDefaultBall(35, 40);
        List<Ball> balls = new ArrayList();

        b.setStartingBall(true);
        balls.add(b);

        List<Brick> bricks = new ArrayList();
        Brick brick1 = shapeRepo.getDefaultBrick("brick1", 20, 20);
        Brick brick2 = shapeRepo.getDefaultBrick("brick2", 30, 20);
        Brick brick3 = shapeRepo.getDefaultBrick("brick3", 40, 20);
        Brick brick4 = shapeRepo.getDefaultBrick("brick4", 50, 20, true, false);

        bricks.add(brick1);
        bricks.add(brick2);
        bricks.add(brick3);
        bricks.add(brick4);

        bricks.get(0).setPowerUp(new AcidBallPowerUp("acidball1"));
        bricks.get(1).addEffect(new ExplosiveEffect(bricks.get(1), 1));
        bricks.get(2).setPowerUp(new AcidBallPowerUp("acidball2"));

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);
        Level level = new Level(3, game, initialState);
        level.setLevelNumber(14);
        level.setRunManual(true);
        return level;

    }

    // complicated tests rely on this level, change with care
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
        level.setLevelNumber(13);
        level.setRunManual(true);
        return level;
    }

    public Level getOutOfBoundsTest() {
        Ball b = shapeRepo.getDefaultBall(10, 90);

        Paddle paddle = shapeRepo.getDefaultPaddle(70);
        b.setStartingBall(true);

        LevelState initialState = new LevelState(b, paddle, new ArrayList());
        Level level = new Level(5, game, initialState);
        level.setLevelNumber(1);
        level.setRunManual(true);
        return level;
    }

    public Level getOneTargetBrickTest() {
        Ball ball = shapeRepo.getDefaultBall();
        Paddle paddle = shapeRepo.getDefaultPaddle(75);;
        ball.setStartingBall(true);

        List<Brick> bricks = new ArrayList();
        bricks.add(shapeRepo.getDefaultBrick("targetBrick1", 45, 40, true));

        LevelState initialState = new LevelState(ball, paddle, bricks);
        Level level = new Level(6, game, initialState);
        level.setLevelNumber(17);
        level.setRunManual(true);
        return level;
    }

    public Level getTargetBrickTest() {
        Ball ball = shapeRepo.getDefaultBall();
        Paddle paddle = shapeRepo.getDefaultPaddle();
        ball.setStartingBall(true);

        List<Brick> bricks = new ArrayList();
        bricks.add(shapeRepo.getDefaultBrick("targetbrick1", 20, 20, true));
        bricks.add(shapeRepo.getDefaultBrick("targetbrick1", 45, 20, true));

        LevelState initialState = new LevelState(ball, paddle, bricks);
        Level level = new Level(7, game, initialState);
        level.setLevelNumber(2);
        level.setRunManual(true);
        return level;
    }

    public Level getSiwtchBrickTest() {
        Ball ball = shapeRepo.getDefaultBall(70, 50);
        Paddle paddle = shapeRepo.getDefaultPaddle(65);

        ball.setStartingBall(true);
        Brick switchBrick = shapeRepo.getDefaultBrick("switchbrick", 62, 40, false, true, false);
        List<Brick> bricks = new ArrayList();
        bricks.add(shapeRepo.getDefaultBrick("targetbrick", 10, 40, true));
        bricks.add(shapeRepo.getDefaultBrick("regularbrick", 40, 40));
        bricks.add(switchBrick);

        List<Brick> switchTargets = new ArrayList();
        switchTargets.add(bricks.get(1));

        switchBrick.addEffect(new ToggleEffect(switchTargets));

        LevelState initialState = new LevelState(ball, paddle, bricks);
        Level level = new Level(8, game, initialState);
        level.setLevelNumber(3);
        level.setRunManual(true);
        return level;
    }

    public Level getExplosiveBrickTest() {
        Ball ball = shapeRepo.getDefaultBall(65, 50);
        Paddle paddle = shapeRepo.getDefaultPaddle();

        ball.setStartingBall(true);

        List<Brick> bricks = new ArrayList();
        bricks.add(shapeRepo.getDefaultBrick("targetbrick", 40, 40, true));
        bricks.add(shapeRepo.getDefaultBrick("regularbrick", 50, 40));

        Brick explosive = shapeRepo.getDefaultBrick("explosivebrick", 60, 40);
        bricks.add(explosive);

        bricks.add(shapeRepo.getDefaultBrick("regularbrick", 70, 40));

        explosive.addEffect(new ExplosiveEffect(explosive, 1));

        LevelState initialState = new LevelState(ball, paddle, bricks);
        Level level = new Level(9, game, initialState);
        level.setLevelNumber(4);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithMultipleBalls() {
        ShapeDimension shape;
        List<Ball> balls = new ArrayList();
        for (int i = 0; i < 100; i++) {
            balls.add(shapeRepo.getDefaultBall("ball" + i, 0 + i, 50));
        }
        balls.get(0).setStartingBall(true);

        LevelState initialState = new LevelState(balls, new ArrayList(), new ArrayList());
        Level level = new Level(10, game, initialState);
        level.setLevelNumber(5);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithOnlyOneLife() {
        Ball ball1 = shapeRepo.getDefaultBall(50, 50);
        Ball ball2 = shapeRepo.getDefaultBall(50, 70);
        ball1.setStartingBall(true);

        List<Ball> balls = new ArrayList();
        balls.add(ball1);
        balls.add(ball1);

        LevelState initialState = new LevelState(balls, new ArrayList(), new ArrayList());
        Level level = new Level(11, game, initialState);
        level.setLevelNumber(6);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithOneBrick() {
        Ball ball = shapeRepo.getDefaultBall(50, 10);
        List<Ball> balls = new ArrayList();
        balls.add(ball);
        balls.get(0).setStartingBall(true);
        List<Brick> bricks = new ArrayList();
        bricks.add(shapeRepo.getDefaultBrick("regularbrick", 50, 20));

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);
        Level level = new Level(12, game, initialState);
        level.setLevelNumber(7);
        level.setRunManual(true);
        return level;
    }

    public Level getLevelWithBrokenPaddle() {

        Paddle basePaddle = shapeRepo.getDefaultPaddle();;
        BrokenPaddlePowerUp bp = new BrokenPaddlePowerUp(basePaddle, 0, difficulty.getPowerupTime());

        List<Paddle> brokenPaddle = bp.getBrokenPaddle();

        Ball ball1 = shapeRepo.getDefaultBall("ball1", brokenPaddle.get(0).getX(), 60);
        Ball ball2 = shapeRepo.getDefaultBall("ball2", brokenPaddle.get(0).getX() + brokenPaddle.get(0).getWidth(), 60);
        Ball ball3 = shapeRepo.getDefaultBall("ball3", brokenPaddle.get(1).getX(), 60);

        List<Ball> balls = new ArrayList();
        balls.add(ball1);
        balls.add(ball2);
        balls.add(ball3);
        balls.get(0).setStartingBall(true);
        LevelState initialState = new LevelState(balls, brokenPaddle, new ArrayList());
        Level level = new Level(13, game, initialState);
        level.setLevelNumber(8);
        level.setRunManual(true);
        return level;
    }

    private Level getLevelWithFloor() {
        Ball ball1 = shapeRepo.getDefaultBall("ball1", 50, 80);
        Ball ball2 = shapeRepo.getDefaultBall("ball1", 50, 70);
        List<Ball> balls = new ArrayList();
        balls.add(ball1);
        balls.add(ball2);
        balls.get(0).setStartingBall(true);
        LevelState initialState = new LevelState(balls, new ArrayList(), new ArrayList());

        Level level = new Level(14, game, initialState);
        level.setLevelNumber(9);
        level.setRunManual(true);
        return level;
    }

    private Level getLevelWithPowerUpBrick() {
        Ball ball = shapeRepo.getDefaultBall(50, 10);
        List<Ball> balls = new ArrayList();
        balls.add(ball);

        balls.get(0).setStartingBall(true);
        List<Brick> bricks = new ArrayList();
        bricks.add(shapeRepo.getDefaultBrick("regularbrick", 50, 20));
        bricks.add(shapeRepo.getDefaultBrick("targetbrick", 80, 20, true));

        bricks.get(0).setPowerUp(new FloorPowerUp(shapeRepo.getDefaultFloor(), difficulty.getPowerupTime()));

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);

        Level level = new Level(15, game, initialState);
        level.setLevelNumber(10);
        level.setRunManual(true);
        return level;

    }

    private Level getLevelWithExplosiveAndPowerUpBrick() {
        Ball ball = shapeRepo.getDefaultBall(49, 30);
        Paddle paddle = shapeRepo.getDefaultPaddle();;

        List<Ball> balls = new ArrayList();
        List<Paddle> paddles = new ArrayList();

        balls.add(ball);
        paddles.add(paddle);

        ShapeDimension floorShape = shapeRepo.getDefaultFloor();
        balls.get(0).setStartingBall(true);
        List<Brick> bricks = new ArrayList();

        bricks.add(shapeRepo.getDefaultBrick("regularBrick", 35, 40));
        bricks.add(shapeRepo.getDefaultBrick("explosiveBrick", 45, 40));

        bricks.get(0).setPowerUp(new FloorPowerUp(floorShape, difficulty.getPowerupTime()));
        bricks.get(1).addEffect(new ExplosiveEffect(bricks.get(1), 1));

        LevelState initialState = new LevelState(balls, paddles, bricks);

        Level level = new Level(16, game, initialState);
        level.setLevelNumber(11);
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
        bricks.get(0).setPowerUp(new BrokenPaddlePowerUp(basePaddle, 1, difficulty.getPowerupTime()));
        bricks.get(1).setTarget(true);

        List<Paddle> paddles = new ArrayList();
        paddles.add(basePaddle);

        LevelState initialState = new LevelState(balls, paddles, bricks);

        Level level = new Level(17, game, initialState);
        level.setLevelNumber(12);
        level.setRunManual(true);
        return level;

    }
}
