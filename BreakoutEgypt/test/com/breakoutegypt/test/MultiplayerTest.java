/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.connectionmanagement.DummyConnection;
import com.breakoutegypt.data.LevelProgressionRepository;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.levelprogression.GameDifficulty;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.exceptions.BreakoutException;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author BenDB
 */
public class MultiplayerTest {

    private Player player1, player2;
    Level level;
    Game game;
    private final LevelProgress ALL_LEVELS_UNLOCKED = LevelProgressionRepository.getDefault(GameType.TEST);

    @Before
    public void init() {
        GameManager gm = new GameManager();
        int id = gm.createGame(GameType.TEST, GameDifficulty.MEDIUM, 2);
        game = gm.getGame(id);

        player1 = new Player("player1");
        player2 = new Player("player2");

        game.addConnectingPlayer(player1);
        game.addConnectionForPlayer(player1.getUsername(), new DummyConnection());
        game.addConnectingPlayer(player2);
        game.addConnectionForPlayer(player2.getUsername(), new DummyConnection());

    }

    @Test
    public void testPlayersHaveRightIndex() {
        game.initStartingLevel(21, ALL_LEVELS_UNLOCKED);

        level = game.getCurrentLevel();

        assertEquals(1, player1.getIndex());
        assertEquals(2, player2.getIndex());
    }

    @Test
    public void testAssigning2Paddles() {
        game.initStartingLevel(21, ALL_LEVELS_UNLOCKED);

        level = game.getCurrentLevel();

        List<Paddle> paddles = level.getLevelState().getPaddles();

        assertEquals("paddle1", paddles.get(0).getName());
        assertEquals("paddle2", paddles.get(1).getName());
    }

    @Test
    public void testMoving2Paddles() {
        game.initStartingLevel(21, ALL_LEVELS_UNLOCKED);

        level = game.getCurrentLevel();


//        game.assignPaddleToPlayer(player1);
//        game.assignPaddleToPlayer(player2);
//        
        int paddle1x = 93;
        int paddle2x = 201;

        game.movePaddle(player1.getUsername(), paddle1x, 0);
        game.movePaddle(player2.getUsername(), paddle2x, 0);

        List<Paddle> paddlesPlayer1 = level.getLevelState().getPaddlesForPlayer(player1.getIndex());
        List<Paddle> paddlesPlayer2 = level.getLevelState().getPaddlesForPlayer(player2.getIndex());

        assertEquals(paddle1x, paddlesPlayer1.get(0).getPosition().x, 0.000001);
        assertEquals(paddle2x, paddlesPlayer2.get(0).getPosition().x, 0.000001);

    }

    @Test
    public void OutOfBoundsLosesLifeForPlayerOne() {
        game.initStartingLevel(21, ALL_LEVELS_UNLOCKED);

        level = game.getCurrentLevel();

        game.movePaddle(player1.getUsername(), 200, 0);
        game.movePaddle(player2.getUsername(), 200, 0);
        level.startBall();
        
        stepTimes(200);
        
        Assert.assertEquals(2, player1.getLives());
        Assert.assertEquals(3, player2.getLives());
    }
    
        @Test
    public void OutOfBoundsLosesLifeForPlayerTwo() {
        game.initStartingLevel(21, ALL_LEVELS_UNLOCKED);

        level = game.getCurrentLevel();

        game.movePaddle(player1.getUsername(), 200, 0);
        game.movePaddle(player2.getUsername(), 200, 0);
        
        Ball ball = level.getLevelState().getBall();
        ball.setPlayerIndex(2);
        ball.setLinearVelocity(0, -100);
        
        stepTimes(200);
        
        Assert.assertEquals(2, player2.getLives());
        Assert.assertEquals(3, player1.getLives());
    }

    @Test(expected = BreakoutException.class)
    public void testConnectToMuchPlayers() {
        game.initStartingLevel(21, ALL_LEVELS_UNLOCKED);
        game.addConnectionForPlayer(player1.getUsername(), new DummyConnection());

        game.addConnectionForPlayer(player2.getUsername(), new DummyConnection());

        Player player3 = new Player("player3");
        game.addConnectingPlayer(player3);
        game.addConnectionForPlayer(player3.getUsername(), new DummyConnection());
    }

    private void stepTimes(int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
