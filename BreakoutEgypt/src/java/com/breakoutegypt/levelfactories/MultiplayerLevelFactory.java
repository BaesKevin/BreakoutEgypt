/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.levelfactories;

import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.DimensionDefaults;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class MultiplayerLevelFactory extends LevelFactory {

    public MultiplayerLevelFactory(Game game) {
        super(game, 2);
    }

    @Override
    public void createCurrentLevel() {
        switch(this.currentLevelId){
            case 1:
                currentLevel = makePong();
                break;
            case 2:
                currentLevel = levelWithTarget();
                break;
        }
        
    }

    public Level makePong() {

                Ball ball = shapeRepo.getDefaultBall("ball", 50,70);
        Ball ball2 = shapeRepo.getDefaultBall("ball2", 50, 30);
        
        Paddle paddle = shapeRepo.getDefaultPaddle("paddle1", 50, 10);
        Paddle paddle2 = shapeRepo.getDefaultPaddle("paddle2", 50, 90);
        
        Brick target = shapeRepo.getDefaultBrick("target", 45, 45, true);
        
        List<Brick> bricks = new ArrayList();
        for (int i = 0; i < 3; i++) {
            Brick brick = new Brick(
                    new ShapeDimension("upperBrick"+i, 35 + i * DimensionDefaults.BRICK_WIDTH, 40,DimensionDefaults.BRICK_WIDTH,  DimensionDefaults.BRICK_HEIGHT_MULTIPLAYER)
                    , false, true, true ,false);
            bricks.add(brick);
        }
        
        for (int i = 0; i < 3; i++) {
            Brick brick = new Brick(
                    new ShapeDimension("lowerBrick"+i,  35 + i * DimensionDefaults.BRICK_WIDTH, 55, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT_MULTIPLAYER)
                    , false, true, true ,true);
            bricks.add(brick);
        }
        
        
        
        paddle.setPlayerIndex(2);
        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        paddles.add(paddle2);

        List<Ball> balls = new ArrayList();
        
        bricks.add(target);
        
        ball2.setPlayerIndex(2);
        ball.setStartingBall(true);
        ball2.setStartingBall(true);
        
        balls.add(ball);
        balls.add(ball2);
        
        LevelState initialState = new LevelState(balls, paddles, bricks, Repositories.getDifficultyRepository().findByName(Difficulty.EASY), true, true);
        return new Level(currentLevelId, game, initialState);

    }
    
    public Level levelWithTarget(){
        
                Ball ball = shapeRepo.getDefaultBall("ball", 50,70);
        Ball ball2 = shapeRepo.getDefaultBall("ball2", 50, 30);
        
        Paddle paddle = shapeRepo.getDefaultPaddle("paddle1", 50, 10);
        Paddle paddle2 = shapeRepo.getDefaultPaddle("paddle2", 50, 90);
        
        Brick target = shapeRepo.getDefaultBrick("target", 45, 45, true);
        paddle.setPlayerIndex(2);
        List<Paddle> paddles = new ArrayList();
        paddles.add(paddle);
        paddles.add(paddle2);

        List<Ball> balls = new ArrayList();
        List<Brick> bricks = new ArrayList();
        bricks.add(target);

        ball2.setPlayerIndex(2);
        ball.setStartingBall(true);
        ball2.setStartingBall(true);
        
        balls.add(ball);
        balls.add(ball2);
        
        LevelState initialState = new LevelState(balls, paddles, bricks, Repositories.getDifficultyRepository().findByName(Difficulty.EASY), false, true);
        return new Level(currentLevelId, game, initialState);

    }
}
