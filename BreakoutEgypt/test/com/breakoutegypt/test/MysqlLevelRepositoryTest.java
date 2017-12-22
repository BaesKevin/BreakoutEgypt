/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.data.DefaultShapeRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.data.mysql.MysqlLevelRepository;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.effects.ToggleEffect;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.powers.FloodPowerDown;
import com.breakoutegypt.domain.powers.generic.BallPowerup;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.DimensionDefaults;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Bjarne Deketelaere
 */
public class MysqlLevelRepositoryTest {
    private Level level;
    public MysqlLevelRepositoryTest() {
        this.level=this.getLevelWithPowerup();
    }
    @Test
    public void levelOperations(){
        try{
            MysqlLevelRepository repository=new MysqlLevelRepository();
            repository.addLevel(level);
            repository.removeLevel(level);
        } catch(Exception ex){
            System.out.println(ex);
            fail("Should not throw an exception");
        }
        
    }
    private Level genSwitchLevel(){
        DefaultShapeRepository shapeRepo = Repositories.getDefaultShapeRepository();
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
        LevelState initialState = new LevelState(balls, paddles, bricks, new Difficulty(1, "easy", 50, Difficulty.INFINITE_LIVES, true, 8000, 80, 15), true);
        Level level = new Level(1, new Game(GameType.ARCADE, Difficulty.EASY, "abc"), initialState);
        level.setLevelPackId(1);
        return level;
    }
    public Level getLevelWithPowerup() {
        DefaultShapeRepository shapeRepo = Repositories.getDefaultShapeRepository();
        Ball b = shapeRepo.getDefaultBall();        
        

        Brick powerupBrick = shapeRepo.getDefaultBrick("floodBrick", 40, 20);
        Brick targetBrick = shapeRepo.getDefaultBrick("target", 1, 1);

        List<Ball> balls = new ArrayList();
        List<Brick> bricks = new ArrayList();

        b.setStartingBall(true);
        balls.add(b);

        powerupBrick.setPowerUp(new BallPowerup(b, 20, 20, 500));

        bricks.add(powerupBrick);
        bricks.add(targetBrick);

        LevelState initialState = new LevelState(balls, new ArrayList(), bricks);
        Level level = new Level(1, new Game(GameType.ARCADE, Difficulty.EASY, "abc"), initialState);
        level.setLevelPackId(1);
        level.setRunManual(true);
        return level;
    }
    public Level getLevelWithFloodPowerDown() {
        DefaultShapeRepository shapeRepo = Repositories.getDefaultShapeRepository();
        Ball b = shapeRepo.getDefaultBall();        
        

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
        Level level = new Level(1, new Game(GameType.ARCADE, Difficulty.EASY, "abc"), initialState);
        level.setLevelPackId(1);
        level.setRunManual(true);
        return level;
    }
}
