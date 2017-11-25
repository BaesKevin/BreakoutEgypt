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
import com.breakoutegypt.domain.effects.Effect;
import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.effects.ToggleEffect;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import com.breakoutegypt.domain.shapes.bricks.BrickType;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author kevin
 */
public class ArcadeLevelFactory extends LevelFactory {

    public ArcadeLevelFactory(Game game) {
        super(game, 4);
    }

    @Override
    public Level getCurrentLevel() {
        return currentLevel;
    }

    @Override
    protected void createCurrentLevel() {
        System.out.println("LevelFactory: creating level " + currentLevelId);
        switch (currentLevelId) {
            case 1:
                currentLevel = getSimpleTestLevel();
//                currentLevel = getLevelWithMultipleBalls();
                break;
            case 2:
                currentLevel = getLevelWithUnbreakableAndExplosive();
                break;
            case 3:
                currentLevel = getLevelWithMultipleBalls();
                break;
            case 4:
                currentLevel = getPossibleRealLevel();
                break;
//            default:
//                currentLevel = getLevelWithSwitch();
//                break;
        }
    }

    public Level getSimpleTestLevel() {
//        targetBlocks = 5;
        ShapeDimension paddleShape = new ShapeDimension("paddle", 45, 250, 100, 4, Color.BLUE);
        ShapeDimension ballShape = new ShapeDimension("ball", 45, 15, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);
        List<Brick> bricks = new ArrayList();

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

        LevelState initialState = new LevelState(ball, paddle, bricks);
        Level level = new Level(currentLevelId, game, initialState, 3);

        return level;
    }

    public Level getLevelWithUnbreakableAndExplosive() {
        ShapeDimension paddleShape = new ShapeDimension("paddle", 45, 250, 100, 4, Color.BLUE);
        ShapeDimension ballShape = new ShapeDimension("ball", 60, 90, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);
        List<Brick> bricks = new ArrayList();

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

        bricks.get(2).addEffect(new ExplosiveEffect(bricks.get(2), 1));

        //bricks.get(1).setType(BrickType.UNBREAKABLE);
        //bricks.get(2).setType(BrickType.EXPLOSIVE);
//        bricks.get(3).getShape().setColor(Color.YELLOW);
//        bricks.get(3).setVisible(false);
//        
//        //bricks.get(4).setVisible(false);
//        bricks.get(4).getShape().setColor(Color.BLUE); 
//        bricks.get(4).setType(BrickType.SWITCH);
//        bricks.get(4).setSwitchBricks(
//                Arrays.asList(new Brick[]{
//                    bricks.get(3)
//                })
//          );
        LevelState initialState = new LevelState(ball, paddle, bricks);
        Level level = new Level(currentLevelId, game, initialState, 3);

        return level;
    }

    public Level getLevelWithMultipleBalls() {
        ShapeDimension paddleShape = new ShapeDimension("paddle", 45, 250, 100, 4, Color.BLUE);
        ShapeDimension ballShape = new ShapeDimension("ball", 60, 70, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape2 = new ShapeDimension("ball2", 80, 90, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape3 = new ShapeDimension("ball3", 100, 110, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape4 = new ShapeDimension("ball4", 120, 130, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
        ShapeDimension ballShape5 = new ShapeDimension("ball5", 140, 150, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);
        Ball ball2 = new Ball(ballShape2);
        Ball ball3 = new Ball(ballShape3);
        Ball ball4 = new Ball(ballShape4);
        Ball ball5 = new Ball(ballShape5);
        List<Ball> balls = new ArrayList<>();
        balls.add(ball);
        balls.add(ball2);
        balls.add(ball3);
        balls.add(ball4);
        balls.add(ball5);
        List<Brick> bricks = new ArrayList();

        List<Paddle> paddles = new ArrayList<Paddle>();
        paddles.add(paddle);
        LevelState initialState = new LevelState(balls, paddles, bricks);
        Level level = new Level(currentLevelId, game, initialState, 3);

        return level;
    }

    public Level getPossibleRealLevel() {
        ShapeDimension paddleShape = new ShapeDimension("paddle", 45, 275, 100, 4, Color.BLUE);
        ShapeDimension ballShape = new ShapeDimension("ball", 60, 150, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);

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
                    brick.setType(BrickType.UNBREAKABLE);
                } else {
                    brick = new Brick(brickShape, new Point(row, col));
                }

                bricks.add(brick);
                col++;
                index++;
            }
            row++;
            col = 1;
        }

        System.out.println("bricks in level: " + bricks.size());
        bricks.get(21).setTarget(true);
        bricks.get(21).getShape().setColor(Color.BLACK);

        bricks.get(4).addEffect(new ExplosiveEffect(bricks.get(4), 1));
        bricks.get(4).setType(BrickType.EXPLOSIVE);
        bricks.get(23).addEffect(new ExplosiveEffect(bricks.get(23), 1));
        bricks.get(23).setType(BrickType.EXPLOSIVE);

        //11 14
        List<Brick> bricksToToggle = new ArrayList();
        for (int i = 0; i < 11; i++) {
            bricksToToggle.add(bricks.get(i));
        }
        bricks.get(11).addEffect(new ToggleEffect(bricksToToggle));
        bricks.get(11).setType(BrickType.SWITCH);
        bricks.get(11).setBreakable(false);

        bricksToToggle = new ArrayList();
        for (int i = 15; i < bricks.size(); i++) {
            bricksToToggle.add(bricks.get(i));
        }
        bricksToToggle.add(bricks.get(12));
        bricksToToggle.add(bricks.get(13));
        bricks.get(14).addEffect(new ToggleEffect(bricksToToggle));
        bricks.get(14).setType(BrickType.SWITCH);
        bricks.get(14).setBreakable(false);

//        for (int i = 0; i <= 2; i++) {
//            bricks.get(i).getShape().setColor(Color.YELLOW);
//            bricks.get(i).setVisible(false);
//        }
//        
//        bricks.get(4).setType(BrickType.SWITCH);
//        bricks.get(4).setSwitchBricks(
//                Arrays.asList(new Brick[]{
//            bricks.get(1), bricks.get(2), bricks.get(3)
//        })
//        );
        LevelState initialState = new LevelState(ball, paddle, bricks);
        Level level = new Level(currentLevelId, game, initialState, 3);

        return level;
    }

//
//    public Level getLevelWithSwitch() {
//        ShapeDimension paddleShape = new ShapeDimension("paddle" + currentLevelId, 45, 250, 100, 4, Color.BLUE);
//        ShapeDimension ballShape = new ShapeDimension("ball", 60, 200, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);
//
//        Paddle paddle = new Paddle(paddleShape);
//
//        Ball ball = new Ball(ballShape);
//        List<Brick> bricks = new ArrayList();
//
//        int row = 1;
//        int col = 1;
//        int rows = 1;
//        int cols = 5;
//        int width = 30;
//        int height = 30;
//
//        ShapeDimension brickShape;
//        Brick brick;
//
//        String id;
//        for (int x = 45; x < 45 + ((width + 1) * cols); x += width + 1) {
//            for (int y = 45; y < 45 + ((height + 1) * rows); y += height + 1) {
//                int colPadding = cols / 10 + 1;
//                int rowPadding = rows / 10 + 1;
//
//                id = String.format("brick%0" + rowPadding + "d%0" + colPadding + "d", col, row); //altijd genoeg padding 0en zetten zodat id's uniek zijn
//
//                brickShape = new ShapeDimension(id, x, y, width, height, Color.PINK);
//                brick = new Brick(brickShape, new Point(row, col));
//                bricks.add(brick);
//                col++;
//            }
//            row++;
//            col = 1;
//        }
//
//        for (int i = 0; i < 1; i++) {
//            bricks.get(i).setTarget(true);
//            bricks.get(i).getShape().setColor(Color.BLACK);
//        }
//
//        for (int i = 1; i <= 2; i++) {
//            bricks.get(i).getShape().setColor(Color.YELLOW);
//            bricks.get(i).setVisible(false);
//        }
//
//        bricks.get(3).setBrickType(BrickType.EXPLOSIVE);
//
//        bricks.get(3).getShape().setColor(Color.RED);
//        bricks.get(4).setType(BrickType.SWITCH);
//        bricks.get(4).setSwitchBricks(
//                Arrays.asList(new Brick[]{
//            bricks.get(1), bricks.get(2), bricks.get(3)
//        })
//        );
//
//        LevelState initialState = new LevelState(ball, paddle, bricks);
//        Level level = new Level(currentLevelId, game, initialState, 3);
//
//        return level;
//    }
}
