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
import com.breakoutegypt.domain.messages.Message;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.effects.FloorPowerUp;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
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

        stepTimes(level, 60);

        for (Ball b : balls) {
            System.out.printf("Ball %s: x = %2.2f\n", b.getName(), b.getPosition().y);
        }

        DummyConnection conn = (DummyConnection) player.getConnection();
        System.out.println(conn.getBallMessages());

    }

    @Test
    public void testFloorInLevel() {
        game.setCurrentLevel(9);
        List<Message> messagesWithFloor = runLevelWithFloor(true);
        System.out.println(messagesWithFloor);
        assertEquals(1, messagesWithFloor.size());
    }

    @Test
    public void testFloorInLevel2() {
        game.setCurrentLevel(9);
        List<Message> messagesWithoutFloor = runLevelWithFloor(false);
        System.out.println(messagesWithoutFloor);
        assertEquals(2, messagesWithoutFloor.size());
    }

    private List<Message> runLevelWithFloor(boolean hasFloor) {

        level = game.getLevel();

        level.startBall();
        stepTimes(level, 30);

        if (hasFloor) {
            ShapeDimension s = new ShapeDimension("floor", 0, 290, 300, 1);
            FloorPowerUp floor = new FloorPowerUp(s);
            level.addFloor(floor);
        }

        stepTimes(level, 80);

        DummyConnection dc = (DummyConnection) player.getConnection();
        List<Message> ballmessages = dc.getBallMessages();

        level.getLevelState().getBalls();

        return ballmessages;
    }

    @Test
    public void activatePowerUpTest() {

        game.setCurrentLevel(10);
        level = game.getLevel();

        level.startBall();
        stepTimes(level, 60);
    }

    private void stepTimes(Level level, int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
