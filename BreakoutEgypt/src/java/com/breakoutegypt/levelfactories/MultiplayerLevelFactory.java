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
import com.breakoutegypt.levelfactories.LevelFactory;
import com.breakoutegypt.domain.shapes.Ball;
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
public class MultiplayerLevelFactory extends LevelFactory {

    public MultiplayerLevelFactory(Game game) {
        super(game, 1);
    }


    public void createCurrentLevel() {
        System.out.printf("LevelFactory: Get level %d of %d", currentLevelId, totalLevels);

        currentLevel = makePong();
    }
    
    public Level makePong() {

        ShapeDimension ballShape = new ShapeDimension("ball", 60, 150, BodyConfigurationFactory.BALL_RADIUS, BodyConfigurationFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(new ShapeDimension("paddle1", 45, 250, 100, 4, Color.BLUE));
        Paddle paddle2 = new Paddle(new ShapeDimension("paddle2", 45, 100, 100, 4, Color.BLUE));

        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        paddles.add(paddle2);

        Ball ball = new Ball(ballShape);

        LevelState initialState = new LevelState(ball, paddle, new ArrayList());
        return new Level(currentLevelId, game, initialState, 3);

    }
}