/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.connectionmanagement.DummyConnection;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author BenDB
 */
public class PowerUpTest {

    Level level;
    Game game;
    Player player;

    @Before
    public void init() {
        GameManager gm = new GameManager();
        int id = gm.createGame(1, 1, GameType.TEST);

        game = gm.getGame(id);

        player = new Player(new User("Kevin"));

        game.addConnectingPlayer(player);

        game.addConnectionForPlayer("Kevin", new DummyConnection());

        game.assignPaddleToPlayer(player);

    }

    @Test
    public void testBrokenPaddle() {

        game.setCurrentLevel(8);
        level = game.getLevel();

        List<Ball> balls = level.getLevelState().getBalls();
        System.out.println(balls);
        for (Ball b : balls) {
            System.out.printf("Ball %s: x = %2.2f\n", b.getName(), b.getPosition().y);
        }

        level.startBall();
        balls = level.getLevelState().getBalls();
        for (Ball b : balls) {
            b.setLinearVelocity(0, 100);
        }

        stepTimes(level, 100);

        for (Ball b : balls) {
            System.out.printf("Ball %s: x = %2.2f\n", b.getName(), b.getPosition().y);
        }

        // check if center ball falls between the gap and the other balls bounce back up
        assertTrue(balls.get(0).getPosition().y < balls.get(1).getPosition().y && balls.get(1).getPosition().y > balls.get(2).getPosition().y);

    }

    @Test
    public void testBrokenPaddleMovement() {
        game.setCurrentLevel(8);
        level = game.getLevel();
        List<Paddle> paddles = level.getLevelState().getPaddles();
        System.out.println("Paddle position: ");
        for (Paddle p : paddles) {
            System.out.println(p.getPosition());
        }

        // left paddle x can't be smaller than the half of its width
        assertTrue(paddles.get(0).getPosition().x >= paddles.get(0).getShape().getWidth() / 2);

        level.movePaddle(paddles.get(0), 120, 156);
        System.out.println("Paddle position 2: ");
        for (Paddle p : paddles) {
            System.out.println(p.getPosition());
        }

        // left paddle x must be between half its width and (level width - the total width of the 2 paddles - half its width) 
        assertTrue(paddles.get(0).getPosition().x > paddles.get(0).getShape().getWidth() / 2 && paddles.get(0).getPosition().x < 300 - paddles.get(0).getShape().getWidth() * 2 - paddles.get(0).getShape().getWidth() / 2);

        level.movePaddle(paddles.get(0), 151, 156);
        System.out.println("Paddle position 3: ");
        for (Paddle p : paddles) {
            System.out.println(p.getPosition());
        }

        // left paddle x must be smaller than (level width - the total width of the 2 paddles - half its width)
        assertTrue(paddles.get(0).getPosition().x <= 300 - paddles.get(0).getShape().getWidth() * 2 - paddles.get(0).getShape().getWidth() / 2);
    }

    @Test
    public void activateFloorPowerUpTest() {

        game.setCurrentLevel(10);
        level = game.getLevel();
        List<Ball> balls = level.getLevelState().getBalls();
        level.startBall();
        balls.get(0).setLinearVelocity(0, -200);
        stepTimes(level, 120);
        
        DummyConnection conn = (DummyConnection) player.getConnection();
        
        assertTrue(conn.getPowerupMessages().size() > 0);
        assertTrue(conn.getLifeMessages().size() == 0);
    }

    @Test
    public void activatePowerUpWithExplosiveTest() {

        game.setCurrentLevel(11);
        level = game.getLevel();

        level.startBall();
        stepTimes(level, 60);
        
        DummyConnection conn = (DummyConnection) player.getConnection();
        
        assertTrue(conn.getPowerupMessages().size() > 0);
    }

    @Test
    public void activateBrokenPaddlePowerup() {

        game.setCurrentLevel(12);
        level = game.getLevel();

        level.startBall();
        level.getLevelState().getBall().setLinearVelocity(0, 100);
        List<Ball> balls = level.getLevelState().getBalls();
        List<Paddle> paddles = level.getLevelState().getPaddles();
        for (Paddle p : paddles) {
            System.out.println("Before: " + p.getPosition());
        }
        assertTrue(paddles.size() == 1);
        stepTimes(level, 60);
        paddles = level.getLevelState().getPaddles();
        for (Paddle p : paddles) {
            System.out.println("After: " + p.getPosition());
        }
        assertTrue(paddles.size() > 1);
        
        DummyConnection conn = (DummyConnection) player.getConnection();

        // ball 3 starts at x = 295, there will be no paddle so it should go out of bounds
        // ball 2 is directly above the left paddle and should bounce back up (no remove message)
        assertTrue(balls.get(1).getPosition().y < paddles.get(0).getPosition().y);
        assertTrue(conn.getPowerupMessages().size() > 0);
    }

    private void stepTimes(Level level, int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
