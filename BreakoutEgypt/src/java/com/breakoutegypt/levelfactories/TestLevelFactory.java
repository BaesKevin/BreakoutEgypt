/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.levelfactories;

import com.breakoutegypt.domain.BodyFactory;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Brick;
import com.breakoutegypt.domain.shapes.BrickType;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.Shape;
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
        System.out.println("LevelFactory: creating level " + currentLevelId);
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
        }
    }

    public Level getOutOfBoundsTest() {
//        targetBlocks = 5;
        Shape paddleShape = new Shape("paddle", 180, 250, 100, 4, Color.BLUE);
        Shape ballShape = new Shape("ball", 45, 250, BodyFactory.BALL_RADIUS, BodyFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);

        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        Level level = new Level(1, game, ball, paddles, new ArrayList(), 3);
        level.setRunManual(true);
        return level;
    }

    public Level getTargetBrickTest() {
        Shape paddleShape = new Shape("paddle", 180, 250, 100, 4, Color.BLUE);
        Shape ballShape = new Shape("ball", 50, 100, BodyFactory.BALL_RADIUS, BodyFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);

        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new Shape("targetbrick1", 40f, 40f, 20, 10), BrickType.REGULAR, new Point(1, 1)));
        bricks.add(new Brick(new Shape("targetbrick2", 62f, 40f, 20, 10), BrickType.REGULAR, new Point(1, 2)));

        bricks.get(0).setIsTarget(true);
        bricks.get(1).setIsTarget(true);

        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        Level level = new Level(1, game, ball, paddles, bricks, 3);
        level.setRunManual(true);
        return level;
    }

    public Level getSiwtchBrickTest() {
        Shape paddleShape = new Shape("paddle", 180, 250, 100, 4, Color.BLUE);
        Shape ballShape = new Shape("ball", 70, 100, BodyFactory.BALL_RADIUS, BodyFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);

        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new Shape("targetbrick", 10f, 40f, 20, 10), BrickType.REGULAR, new Point(0, 1)));
        bricks.add(new Brick(new Shape("regularbrick", 40f, 40f, 20, 10), BrickType.REGULAR, new Point(1, 1)));
        bricks.add(new Brick(new Shape("switchbrick", 62f, 40f, 20, 10), BrickType.SWITCH, new Point(1, 2)));

        List<Brick> switchTaregts = new ArrayList();
        switchTaregts.add(bricks.get(1));
        bricks.get(2).setSwitchBricks(switchTaregts);

        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        Level level = new Level(1, game, ball, paddles, bricks, 3);
        level.setRunManual(true);
        return level;
    }

    public Level getExplosiveBrickTest() {
         Shape paddleShape = new Shape("paddle", 180, 250, 100, 4, Color.BLUE);
        Shape ballShape = new Shape("ball", 70, 100, BodyFactory.BALL_RADIUS, BodyFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);

        List<Brick> bricks = new ArrayList();
        bricks.add(new Brick(new Shape("targetbrick", 10f, 40f, 20, 10), BrickType.REGULAR, new Point(0, 1)));
        bricks.add(new Brick(new Shape("regularbrick", 40f, 40f, 20, 10), BrickType.REGULAR, new Point(1, 1)));
        bricks.add(new Brick(new Shape("explosivebrick", 62f, 40f, 20, 10), BrickType.EXPLOSIVE, new Point(2, 1)));
        bricks.add(new Brick(new Shape("regularbrick", 84f, 40f, 20, 10), BrickType.REGULAR, new Point(3, 1)));

        bricks.get(2).setExplosionRadius(1);

        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        Level level = new Level(1, game, ball, paddles, bricks, 3);
        level.setRunManual(true);
        return level;
    }
}
