/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.levelfactories;

import com.breakoutegypt.data.LevelRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelPack;
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
        super(game, 2, 2, "multiplayer");
    }

    @Override
    public void createCurrentLevel() {
        LevelPack pack = Repositories.getLevelPackRepo().getByName(LEVELPACK_NAME);
        if (pack == null) {
            Repositories.getLevelPackRepo().add(new LevelPack(LEVELPACK_NAME, "multiplayer", defaultOpenLevels, totalLevels));
            pack = Repositories.getLevelPackRepo().getByName(LEVELPACK_NAME);
        }

        LevelRepository levelRepo = Repositories.getLevelRepository();
        Level levelFromDatabase = levelRepo.getLevelByNumber(currentLevelId, pack.getId(), game);
        
        if (levelFromDatabase == null) {
            switch (this.currentLevelId) {
                case 1:
                    currentLevel = makePong();
                    break;
                case 2:
                    currentLevel = levelWithTarget();
                    break;
            }

            currentLevel.setLevelPackId(pack.getId());
            Repositories.getLevelRepository().addLevel(currentLevel);
            currentLevel = Repositories.getLevelRepository().getLevelByNumber(currentLevelId, pack.getId(), game);
        } else {
            currentLevel = levelFromDatabase;
        }

    }

    public Level makePong() {

        Ball ball = shapeRepo.getDefaultBall();
        Ball ball2 = shapeRepo.getDefaultBall("ball2", 50, 20);

        Paddle paddle = shapeRepo.getDefaultPaddle("paddle1", 50, 10);
        Paddle paddle2 = shapeRepo.getDefaultPaddle("paddle2", 50, 95);

        Brick target = shapeRepo.getDefaultBrick("target", 45, 45, true);
        target.setIsSquare(true);
        
        List<Brick> bricks = new ArrayList();

        drawTopPyramid(bricks);
        drawBottomPyramid(bricks);
        

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
        Level level = new Level(currentLevelId, game, initialState);
        level.setLevelNumber(1);
        return level;
    }
    
     private void drawTopPyramid(List<Brick> bricks) {
        int rows = 4;
        int x= 45, y = 50 - rows * DimensionDefaults.BRICK_HEIGHT_MULTIPLAYER;
        int bricksPerRow = 1;
        for (int row = 0; row < rows; row++) {
            
            int startingx = 45 - (int)Math.floor(bricksPerRow / 2) * (DimensionDefaults.BRICK_WIDTH / 2);
            boolean invertedBrick = false;
            
            for(int col = 0; col < bricksPerRow; col++){
                int brickx = startingx + col * DimensionDefaults.BRICK_WIDTH / 2;
                int bricky = y;
                Brick brick = new Brick(
                        new ShapeDimension("lowerBrick", brickx, bricky, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT_MULTIPLAYER),
                        false, true, true, invertedBrick);
                bricks.add(brick);
                invertedBrick = !invertedBrick;
                System.out.printf("x: %d y: %d\n", startingx + col * DimensionDefaults.BRICK_WIDTH, y);
            }
            
            bricksPerRow += 4;
            y += DimensionDefaults.BRICK_HEIGHT_MULTIPLAYER;
        }
        
        bricks.get(0).setBreakable(false);
    }

    private void drawBottomPyramid(List<Brick> bricks) {
        int rows = 4;
        int x= 45, y = 50;
        int bricksPerRow = ((rows - 1) * 4) + 1;
        for (int row = 0; row < rows; row++) {
            
            int startingx = 45 - (int)Math.floor(bricksPerRow / 2) * (DimensionDefaults.BRICK_WIDTH / 2);
            boolean invertedBrick = true;
            
            for(int col = 0; col < bricksPerRow; col++){
                int brickx = startingx + col * DimensionDefaults.BRICK_WIDTH / 2;
                int bricky = y;
                Brick brick = new Brick(
                        new ShapeDimension("lowerBrick", brickx, bricky, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT_MULTIPLAYER),
                        false, true, true, invertedBrick);
                bricks.add(brick);
                invertedBrick = !invertedBrick;
                System.out.printf("x: %d y: %d\n", startingx + col * DimensionDefaults.BRICK_WIDTH, y);
            }
            
            bricksPerRow -= 4;
            y += DimensionDefaults.BRICK_HEIGHT_MULTIPLAYER;
        }
        
        bricks.get(bricks.size() - 1).setBreakable(false);
    }

    public Level levelWithTarget() {

        Ball ball = shapeRepo.getDefaultBall("ball", 50, 70);
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
        Level level = new Level(currentLevelId, game, initialState);
        level.setLevelNumber(2);
        return level;

    }
}
