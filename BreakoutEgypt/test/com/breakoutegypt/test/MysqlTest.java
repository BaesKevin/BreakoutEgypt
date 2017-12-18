/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.data.mysql.MysqlBallRepository;
import com.breakoutegypt.data.mysql.MysqlBrickRepository;
import com.breakoutegypt.data.mysql.MysqlBrickTypeRepository;
import com.breakoutegypt.data.mysql.MysqlDifficultyRepository;
import com.breakoutegypt.data.mysql.MysqlPaddleRepository;
import com.breakoutegypt.data.mysql.MysqlUserRepository;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.bricks.BrickType;
import java.awt.Color;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Bjarne Deketelaere
 */
public class MysqlTest {
    
    public MysqlTest() {
    }
    @Test
    public void DifficultyRepoFindAllTest(){
        MysqlDifficultyRepository repository=new MysqlDifficultyRepository();
        List<Difficulty> difficulties=repository.findAll();
        assertNotNull(difficulties);
    }
    @Test
    public void DifficultyRepoFindByNameTest(){
        MysqlDifficultyRepository repository=new MysqlDifficultyRepository();
        Difficulty difficulty=repository.findByName("easy");
        assertNotNull(difficulty);
    }
    @Test
    public void brickTypeRepoFindByNameTest(){
        MysqlBrickTypeRepository repository=new MysqlBrickTypeRepository();
        BrickType bricktype=repository.getBrickTypeByName("EXPLOSIVE");
        assertNotNull(bricktype);
        assertEquals(bricktype.getName(), "EXPLOSIVE");
    }
    
    @Test
    public void addPaddle(){
        ShapeDimension paddleShape = new ShapeDimension("paddle", 45, 275, 100, 4, Color.BLUE);
        Paddle paddle = new Paddle(paddleShape);
        MysqlPaddleRepository repository=new MysqlPaddleRepository();
        repository.addPaddle(paddle);
        List<Paddle> paddles=repository.getPaddles();
        assertEquals(paddles.size(), 1);
        repository.removePaddle(paddle);
    }
    @Test
    public void getBricks(){
        MysqlBrickRepository repository=new MysqlBrickRepository();
        Brick brick=new Brick(new ShapeDimension(45,275,100,4), true, true, true);
        repository.addBrick(brick);
        List<Brick> bricks=repository.getBricks();
        assertEquals(bricks.size(), 1);
        repository.removeBrick(brick);
        bricks=repository.getBricks();
        assertEquals(bricks.size(), 0);
    }
    @Test
    public void getBalls(){
        MysqlBallRepository repository=new MysqlBallRepository();
        Ball ball=new Ball(new ShapeDimension(45,275,100,4),10,10);
        repository.addBall(ball);
        List<Ball> balls=repository.getBalls();
        assertEquals(balls.size(), 1);
        repository.removeBall(ball);
        balls=repository.getBalls();
        assertEquals(balls.size(), 0);
        
    }
}
