/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.levelfactories;

import com.breakoutegypt.domain.BodyFactory;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.levelfactories.LevelFactory;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Brick;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.Shape;
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

        Shape ballShape = new Shape("ball", 60, 150, BodyFactory.BALL_RADIUS, BodyFactory.BALL_RADIUS, Color.GREEN);

        Paddle paddle = new Paddle(new Shape("paddle1", 45, 250, 100, 4, Color.BLUE));
        Paddle paddle2 = new Paddle(new Shape("paddle2", 45, 100, 100, 4, Color.BLUE));

        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        paddles.add(paddle2);

        Ball ball = new Ball(ballShape);

        return new Level(currentLevelId, game, ball, paddles, new ArrayList<Brick>(), 3);

    }
}
