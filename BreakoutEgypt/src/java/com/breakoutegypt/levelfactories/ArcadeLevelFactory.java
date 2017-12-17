/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.levelfactories;

import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.shapes.BodyConfigurationFactory;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.effects.ToggleEffect;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.powers.FloodPowerDown;
import com.breakoutegypt.domain.powers.FloorPowerUp;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.DimensionDefaults;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.Paddle;
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
public class ArcadeLevelFactory extends LevelFactory {

    private Difficulty difficulty;

    public ArcadeLevelFactory(Game game, Difficulty difficulty) {
        super(game, 5, 5);
        this.difficulty = difficulty;
    }

    @Override
    public Level getCurrentLevel() {
        return currentLevel;
    }

    @Override
    protected void createCurrentLevel() {
        switch (currentLevelId) {
            case 1:
                currentLevel = getSimpleTestLevel();
                break;
            case 2:
                currentLevel = getLevelWithUnbreakableAndExplosive();
                break;
            case 3:
                currentLevel = getSimpleTestLevel();
                currentLevel.setLevelNumber(3);
                break;
            case 4:
                currentLevel = getPossibleRealLevel();
                break;
            case 5:
                currentLevel = getLevelWithFloodPowerDown();
                break;
        }
    }

    public Level getSimpleTestLevel() {
        return getSimpleTestLevel(BreakoutWorld.TIMESTEP_DEFAULT);
    }

    public Level getLevelWithFloodPowerDown() {
        Paddle paddle = shapeRepo.getDefaultPaddle();
        Ball ball = shapeRepo.getDefaultBall(50, 0);
        
        Brick powerdownBrick = shapeRepo.getDefaultBrick("brick1", 40, 20);
        Brick target = new Brick(new ShapeDimension("brick2", 1, 1, 1, 1), true, false);
        
        List<Paddle> paddles = new ArrayList();
        List<Ball> balls = new ArrayList();
        List<Brick> bricks = new ArrayList();
        
        balls.add(ball);
        paddles.add(paddle);
        balls.get(0).setStartingBall(true);
        
        powerdownBrick.setPowerdown(new FloodPowerDown(ball, 50, 15));

        bricks.add(powerdownBrick);
        bricks.add(target);

        LevelState initialState = new LevelState(balls, paddles, bricks);

        Level level = new Level(1, game, initialState);

        return level;
    }

    public Level getSimpleTestLevel(float timeStep) {
        Paddle paddle = shapeRepo.getDefaultPaddle();
        Ball ball = shapeRepo.getDefaultBall();

        List<Brick> bricks = new ArrayList();
        ball.setStartingBall(true);
        ShapeDimension brickShape;
        Brick brick;

        String name = "brick1";
        int x = 5;
        int y = 5;
        int width = 10;
        int height = 10;

        // TODO move to ShapeFactory which gets default bricks from staticbrickrepo
        brickShape = new ShapeDimension(name, x, y, width, height);
        brick = new Brick(brickShape, true, true);

        bricks.add(brick);
        List<Ball> balls = new ArrayList();
        balls.add(ball);
        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        LevelState initialState = new LevelState(balls, paddles, bricks, difficulty, true);
        Level level = new Level(1, game, initialState);

        return level;
    }

    public Level getLevelWithUnbreakableAndExplosive() {
        Paddle paddle = shapeRepo.getDefaultPaddle();
        Ball ball = shapeRepo.getDefaultBall();

        List<Brick> bricks = new ArrayList();
        ball.setStartingBall(true);
        int row = 1;
        int col = 1;
        int rows = 1;
        int cols = 5;
        int width = DimensionDefaults.BRICK_WIDTH;
        int height = DimensionDefaults.BRICK_WIDTH;

        ShapeDimension brickShape;
        Brick brick;

        String id;
        for (int x = 10; x < 10 + ((width) * cols); x += width) {
            for (int y = 10; y < 10 + ((height ) * rows); y += height) {
                int colPadding = cols / 10 + 1;
                int rowPadding = rows / 10 + 1;

                id = String.format("brick%0" + rowPadding + "d%0" + colPadding + "d", col, row); //altijd genoeg padding 0en zetten zodat id's uniek zijn

                brick = shapeRepo.getDefaultBrick(id, x, y);
                
                bricks.add(brick);
                col++;
            }
            row++;
            col = 1;
        }

        for (int i = 0; i < 1; i++) {
            bricks.get(i).setTarget(true);
        }

        bricks.get(2).addEffect(new ExplosiveEffect(bricks.get(2), 1));
        List<Ball> balls = new ArrayList();
        balls.add(ball);
        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        LevelState initialState = new LevelState(balls, paddles, bricks, difficulty, true);
        Level level = new Level(2, game, initialState);

        return level;
    }

    public Level getLevelWithMultipleBalls() {
        Paddle paddle = shapeRepo.getDefaultPaddle();
        List<Brick> bricks = new ArrayList();

        ShapeDimension shape;
        List<Ball> balls = new ArrayList();
        for (int i = 0; i < 50; i++) {
            shape = new ShapeDimension("ball" + i, 4 + i, 100, 2, DimensionDefaults.BALL_RADIUS, Color.GREEN);
            balls.add(new Ball(shape));
        }
        balls.get(0).setStartingBall(true);
        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        LevelState initialState = new LevelState(balls, paddles, bricks, difficulty, true);
        Level level = new Level(currentLevelId, game, initialState);

        return level;
    }

    public Level getPossibleRealLevel() {
        Ball ball = shapeRepo.getDefaultBall();
        Paddle paddle = shapeRepo.getDefaultPaddle();
        ball.setStartingBall(true);
        
        int row = 1;
        int col = 1;
        int rows = 3;
        int cols = 9;
        int width = DimensionDefaults.BRICK_WIDTH;
        int height = DimensionDefaults.BRICK_HEIGHT;

        ShapeDimension brickShape;
        Brick brick;
        List<Brick> bricks = new ArrayList();
        List<Integer> unbreakables = new ArrayList();
        unbreakables.add(25);
        unbreakables.add(24);
        unbreakables.add(22);
        unbreakables.add(19);
        unbreakables.add(18);
        unbreakables.add(11);
        unbreakables.add(14);

        String id;
        int index = 0;
        for (int x = 10; x < 10 + ((width) * cols); x += width) {
            for (int y = 5; y < 5 + ((height) * rows); y += height) {
                int colPadding = cols / 10 + 1;
                int rowPadding = rows / 10 + 1;

                id = String.format("brick%0" + rowPadding + "d%0" + colPadding + "d", col, row); //altijd genoeg padding 0en zetten zodat id's uniek zijn

                brickShape = new ShapeDimension(id, x, y, width, height, Color.PINK);

                if (unbreakables.contains(index)) {
                    brick = new Brick(brickShape, false, true, false);
                } else {
                    brick = new Brick(brickShape);
                }

                bricks.add(brick);
                col++;
                index++;
            }
            row++;
            col = 1;
        }

        bricks.get(21).setTarget(true);

        bricks.get(4).addEffect(new ExplosiveEffect(bricks.get(4), 1));
        bricks.get(23).addEffect(new ExplosiveEffect(bricks.get(23), 1));
        
        List<Brick> bricksToToggle = new ArrayList();
        for (int i = 0; i < 11; i++) {
            bricksToToggle.add(bricks.get(i));
        }
        bricks.get(11).addEffect(new ToggleEffect(bricksToToggle));
        bricks.get(11).setBreakable(false);

        bricksToToggle = new ArrayList();
        for (int i = 15; i < bricks.size(); i++) {
            bricksToToggle.add(bricks.get(i));
            bricks.get(i).setVisible(false);
        }
        bricksToToggle.add(bricks.get(12));
        bricksToToggle.add(bricks.get(13));
        bricks.get(14).addEffect(new ToggleEffect(bricksToToggle));
        bricks.get(14).setBreakable(false);

        List<Ball> balls = new ArrayList();
        balls.add(ball);
        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
//        int noOfPowerups = 5;
//        int noOfPowerdowns = 5;

        LevelState initialState = new LevelState(balls, paddles, bricks, difficulty, true);
        Level level = new Level(currentLevelId, game, initialState);

        return level;
    }

    public Level getLevelWithSwitch() {
        Ball ball = shapeRepo.getDefaultBall();
        Paddle paddle = shapeRepo.getDefaultPaddle();

        List<Brick> bricks = new ArrayList();
        ball.setStartingBall(true);
        int row = 1;
        int col = 1;
        int rows = 1;
        int cols = 5;
        int width = DimensionDefaults.BRICK_WIDTH;
        int height = DimensionDefaults.BRICK_HEIGHT;

        ShapeDimension brickShape;
        Brick brick;

        String id;
        for (int x = 10; x < 10 + ((width) * cols); x += width) {
            for (int y = 10; y < 10 + ((height) * rows); y += height) {
                int colPadding = cols / 10 + 1;
                int rowPadding = rows / 10 + 1;

                id = String.format("brick%0" + rowPadding + "d%0" + colPadding + "d", col, row); //altijd genoeg padding 0en zetten zodat id's uniek zijn

                brickShape = new ShapeDimension(id, x, y, width, height, Color.PINK);
                brick = new Brick(brickShape);
                bricks.add(brick);
                col++;
            }
            row++;
            col = 1;
        }

        for (int i = 0; i < 1; i++) {
            bricks.get(i).setTarget(true);
        }

        for (int i = 1; i <= 3; i++) {
            bricks.get(i).setVisible(false);
        }

        bricks.get(3).addEffect(new ExplosiveEffect(bricks.get(3), 1));

        List<Brick> toggles = new ArrayList();
        toggles.add(bricks.get(1));
        toggles.add(bricks.get(2));
        toggles.add(bricks.get(3));
        bricks.get(4).addEffect(new ToggleEffect(toggles));

        List<Ball> balls = new ArrayList();
        balls.add(ball);
        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        LevelState initialState = new LevelState(balls, paddles, bricks, difficulty, true);
        Level level = new Level(currentLevelId, game, initialState);

        return level;
    }
}
