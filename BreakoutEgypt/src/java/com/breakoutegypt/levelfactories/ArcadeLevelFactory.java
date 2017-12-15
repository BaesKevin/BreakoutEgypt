/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.levelfactories;

import com.breakoutegypt.data.BrickTypeRepository;
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
import com.breakoutegypt.domain.shapes.Ball;
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
        ShapeDimension ballShape = new ShapeDimension("ball", 120, 40, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS);
        ShapeDimension brickshape1 = new ShapeDimension("brick1", 120, 80, 20, 20);
        ShapeDimension brickshape2 = new ShapeDimension("brick2", 1, 1, 1, 1);
        ShapeDimension paddleShape = new ShapeDimension("paddle", 45, 250, 100, 4, Color.BLUE);

        List<Paddle> paddles = new ArrayList();
        List<Ball> balls = new ArrayList();
        List<Brick> bricks = new ArrayList();
        Ball b = new Ball(ballShape);
        balls.add(b);
        paddles.add(new Paddle(paddleShape));
        balls.get(0).setStartingBall(true);
        Brick powerdownBrick = new Brick(brickshape1, new Point());
        powerdownBrick.setPowerdown(new FloodPowerDown(b, 50, 15));

        bricks.add(powerdownBrick);
        bricks.add(new Brick(brickshape2, new Point(9, 9), true, false));

        LevelState initialState = new LevelState(balls, paddles, bricks);
        Level level = new Level(1, game, initialState);

        return level;
    }

    public Level getSimpleTestLevel(float timeStep) {
//        targetBlocks = 5;
        ShapeDimension paddleShape = new ShapeDimension("paddle", 45, 250, 100, 4, Color.BLUE);
        ShapeDimension ballShape = new ShapeDimension("ball", 45, 15, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);
        List<Brick> bricks = new ArrayList();
        ball.setStartingBall(true);
        ShapeDimension brickShape;
        Brick brick;

        String name = "brick1";
        int x = 20;
        int y = 20;
        int width = 30;
        int height = 30;
        int gridX = 1;
        int gridY = 1;

        // TODO move to ShapeFactory which gets default bricks from staticbrickrepo
        brickShape = new ShapeDimension(name, x, y, width, height);
        brick = new Brick(brickShape, new Point(gridX, gridY), true, true);

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
        ShapeDimension paddleShape = new ShapeDimension("paddle", 45, 250, 100, 4, Color.BLUE);
        ShapeDimension ballShape = new ShapeDimension("ball", 60, 90, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);
        List<Brick> bricks = new ArrayList();
        ball.setStartingBall(true);
        int row = 1;
        int col = 1;
        int rows = 1;
        int cols = 5;
        int width = 30;
        int height = 30;

        ShapeDimension brickShape;
        Brick brick;

        String id;
        for (int x = 45; x < 45 + ((width + 1) * cols); x += width - 1) {
            for (int y = 45; y < 45 + ((height + 1) * rows); y += height + 1) {
                int colPadding = cols / 10 + 1;
                int rowPadding = rows / 10 + 1;

                id = String.format("brick%0" + rowPadding + "d%0" + colPadding + "d", col, row); //altijd genoeg padding 0en zetten zodat id's uniek zijn

                brickShape = new ShapeDimension(id, x, y, width, height, Color.PINK);
                brick = new Brick(brickShape, new Point(row, col));
                bricks.add(brick);
                col++;
            }
            row++;
            col = 1;
        }

        for (int i = 0; i < 1; i++) {
            bricks.get(i).setTarget(true);
            bricks.get(i).getShape().setColor(Color.BLACK);
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
        ShapeDimension paddleShape = new ShapeDimension("paddle", 45, 250, 100, 4, Color.BLUE);

        Paddle paddle = new Paddle(paddleShape);
        List<Brick> bricks = new ArrayList();

        ShapeDimension shape;
        List<Ball> balls = new ArrayList();
        for (int i = 0; i < 50; i++) {
            shape = new ShapeDimension("ball" + i, 4 + i, 100, 2, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
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
        BrickTypeRepository bricktypeRepo=Repositories.getBrickTypeRepository();
        ShapeDimension paddleShape = new ShapeDimension("paddle", 45, 275, 100, 4, Color.BLUE);
        ShapeDimension ballShape = new ShapeDimension("ball", 60, 150, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        
        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);
        ball.setStartingBall(true);
        int row = 1;
        int col = 1;
        int rows = 3;
        int cols = 9;
        int width = 30;
        int height = 30;

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
        for (int x = 10; x < 10 + ((width + 1) * cols); x += width + 1) {
            for (int y = 5; y < 5 + ((height + 1) * rows); y += height + 1) {
                int colPadding = cols / 10 + 1;
                int rowPadding = rows / 10 + 1;

                id = String.format("brick%0" + rowPadding + "d%0" + colPadding + "d", col, row); //altijd genoeg padding 0en zetten zodat id's uniek zijn

                brickShape = new ShapeDimension(id, x, y, width, height, Color.PINK);

                if (unbreakables.contains(index)) {
                    brick = new Brick(brickShape, new Point(row, col), false, true, false);
                    brick.setType(bricktypeRepo.getBrickTypeByName("UNBREAKABLE"));
                } else {
                    brick = new Brick(brickShape, new Point(row, col));
                    brick.setType(bricktypeRepo.getBrickTypeByName("REGULAR"));
                }

                bricks.add(brick);
                col++;
                index++;
            }
            row++;
            col = 1;
        }

        bricks.get(21).setTarget(true);
        bricks.get(21).setType(bricktypeRepo.getBrickTypeByName("TARGET"));

        bricks.get(4).addEffect(new ExplosiveEffect(bricks.get(4), 1));
        bricks.get(4).setType(bricktypeRepo.getBrickTypeByName("EXPLOSIVE"));
        bricks.get(23).addEffect(new ExplosiveEffect(bricks.get(23), 1));
        bricks.get(23).setType(bricktypeRepo.getBrickTypeByName("EXPLOSIVE"));

        List<Brick> bricksToToggle = new ArrayList();
        for (int i = 0; i < 11; i++) {
            bricksToToggle.add(bricks.get(i));
        }
        bricks.get(11).addEffect(new ToggleEffect(bricksToToggle));
        bricks.get(11).setType(bricktypeRepo.getBrickTypeByName("SWITCH"));
        bricks.get(11).setBreakable(false);

        bricksToToggle = new ArrayList();
        for (int i = 15; i < bricks.size(); i++) {
            bricksToToggle.add(bricks.get(i));
            bricks.get(i).setVisible(false);
        }
        bricksToToggle.add(bricks.get(12));
        bricksToToggle.add(bricks.get(13));
        bricks.get(14).addEffect(new ToggleEffect(bricksToToggle));
        bricks.get(14).setType(bricktypeRepo.getBrickTypeByName("SWITCH"));
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
        ShapeDimension paddleShape = new ShapeDimension("paddle" + currentLevelId, 45, 250, 100, 4, Color.BLUE);
        ShapeDimension ballShape = new ShapeDimension("ball", 60, 200, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);

        Ball ball = new Ball(ballShape);
        List<Brick> bricks = new ArrayList();
        ball.setStartingBall(true);
        int row = 1;
        int col = 1;
        int rows = 1;
        int cols = 5;
        int width = 30;
        int height = 30;

        ShapeDimension brickShape;
        Brick brick;

        String id;
        for (int x = 45; x < 45 + ((width + 1) * cols); x += width + 1) {
            for (int y = 45; y < 45 + ((height + 1) * rows); y += height + 1) {
                int colPadding = cols / 10 + 1;
                int rowPadding = rows / 10 + 1;

                id = String.format("brick%0" + rowPadding + "d%0" + colPadding + "d", col, row); //altijd genoeg padding 0en zetten zodat id's uniek zijn

                brickShape = new ShapeDimension(id, x, y, width, height, Color.PINK);
                brick = new Brick(brickShape, new Point(row, col));
                bricks.add(brick);
                col++;
            }
            row++;
            col = 1;
        }

        for (int i = 0; i < 1; i++) {
            bricks.get(i).setTarget(true);
            bricks.get(i).getShape().setColor(Color.BLACK);
        }

        for (int i = 1; i <= 3; i++) {
            bricks.get(i).getShape().setColor(Color.YELLOW);
            bricks.get(i).setVisible(false);
        }

        bricks.get(3).addEffect(new ExplosiveEffect(bricks.get(3), 1));
        bricks.get(3).getShape().setColor(Color.RED);

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
