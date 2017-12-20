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
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.DimensionDefaults;
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

    @Override
    public void createCurrentLevel() {
        currentLevel = makePong();
    }

    public Level makePong() {

        ShapeDimension ballShape = new ShapeDimension("ball", 60, 150, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(new ShapeDimension("paddle1", 45, 250, 100, 4, Color.BLUE));
        Paddle paddle2 = new Paddle(new ShapeDimension("paddle2", 45, 100, 100, 4, Color.BLUE));

        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        paddles.add(paddle2);

        Ball ball = new Ball(ballShape);

        LevelState initialState = new LevelState(ball, paddle, new ArrayList());
        return new Level(currentLevelId, game, initialState);

    }
}
