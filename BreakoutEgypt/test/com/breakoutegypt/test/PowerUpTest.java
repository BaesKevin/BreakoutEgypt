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

        DummyConnection conn = (DummyConnection) player.getConnection();
        System.out.println(conn.getBallMessages());

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
        level.movePaddle(paddles.get(0), 151, 156);
        System.out.println("Paddle position: ");
        for (Paddle p : paddles) {
            System.out.println(p.getPosition());
        }

    }

    @Test
    public void activateFloorPowerUpTest() {

        game.setCurrentLevel(10);
        level = game.getLevel();

        level.startBall();
        stepTimes(level, 60);
    }

    @Test
    public void activatePowerUpWithExplosiveTest() {

        game.setCurrentLevel(11);
        level = game.getLevel();

        level.startBall();
        stepTimes(level, 60);
    }

    @Test
    public void activateBrokenPaddlePowerup() {

        game.setCurrentLevel(12);
        level = game.getLevel();

        level.startBall();
        level.getLevelState().getBall().setLinearVelocity(0, 100);
        List<Ball> balls = level.getLevelState().getBalls();
        List<Paddle> paddles = level.getLevelState().getPaddles();
        for (Paddle p : paddles ) {
            System.out.println("Before: " + p.getPosition());
        }
        stepTimes(level, 60);
        paddles = level.getLevelState().getPaddles();
        for (Paddle p : paddles ) {
            System.out.println("After: " + p.getPosition());
        }
    }

    private void stepTimes(Level level, int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
