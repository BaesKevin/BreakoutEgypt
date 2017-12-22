/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.connectionmanagement.DummyConnection;
import com.breakoutegypt.data.DummyLevelProgressionRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import com.breakoutegypt.domain.powers.AcidBallPowerUp;
import com.breakoutegypt.domain.powers.BreakoutPowerUpHandler;
import com.breakoutegypt.domain.powers.PowerUp;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.exceptions.BreakoutException;
import java.util.List;
import org.jbox2d.common.Vec2;
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
    private final LevelProgress ALL_LEVELS_UNLOCKED = DummyLevelProgressionRepository.getDefault(GameType.TEST);

    @Before
    public void init() {
        Repositories.isTesting(true);
        GameManager gm = new GameManager();
        int id = gm.createGame(GameType.TEST, Difficulty.MEDIUM, 2);
        game = gm.getGame(id);

        player1 = new Player("player1");
        player2 = new Player("player2");

        game.addConnectingPlayer(player1);
        game.addConnectionForPlayer(player1.getUsername(), new DummyConnection());
        game.addConnectingPlayer(player2);
        game.addConnectionForPlayer(player2.getUsername(), new DummyConnection());

    }

    @Test
    public void testPowerupsPerPlayer() {

        game.initStartingLevel(21, ALL_LEVELS_UNLOCKED);
        level = game.getCurrentLevel();

        AcidBallPowerUp abpu1 = new AcidBallPowerUp("acidball1");
        abpu1.setPlayerId(1);
        AcidBallPowerUp abpu2 = new AcidBallPowerUp("acidball2");
        abpu2.setPlayerId(2);

        BreakoutPowerUpHandler bpuh = level.getPoweruphandler();

        bpuh.addPowerUp(abpu1);
        bpuh.addPowerUp(abpu2);

        PowerUp powerupPlayer1 = bpuh.getPowerupByName("acidball1", 1);

        level.triggerPowerup(powerupPlayer1.getName(), powerupPlayer1.getPlayerId());

        Assert.assertEquals(1, bpuh.getPowerUps().size());
    }
    
    @Test (expected = NullPointerException.class)
    public void testWrongPowerupTrigger() {
        game.initStartingLevel(21, ALL_LEVELS_UNLOCKED);
        level = game.getCurrentLevel();

        AcidBallPowerUp abpu1 = new AcidBallPowerUp("acidball1");
        abpu1.setPlayerId(1);
        AcidBallPowerUp abpu2 = new AcidBallPowerUp("acidball2");
        abpu2.setPlayerId(2);

        BreakoutPowerUpHandler bpuh = level.getPoweruphandler();

        bpuh.addPowerUp(abpu1);
        bpuh.addPowerUp(abpu2);

        PowerUp powerupPlayer1 = bpuh.getPowerupByName("acidball1", 2);

        level.triggerPowerup(powerupPlayer1.getName(), powerupPlayer1.getPlayerId());
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
        int paddle1x = 12;
        int paddle2x = 89;

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
        level.startBall(player1.getIndex());

        stepTimes(200);

        Assert.assertEquals(2, player1.getLives());
        Assert.assertEquals(3, player2.getLives());
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

    @Test
    public void testStartingPlayer2BallDoesntStartPlayer1Ball() {
        game.initStartingLevel(21, ALL_LEVELS_UNLOCKED);

        DummyConnection con = (DummyConnection) player1.getConnection();

        level = game.getCurrentLevel();

        game.movePaddle(player1.getUsername(), 200, 0);
        game.movePaddle(player2.getUsername(), 200, 0);

        level.startBall(player2.getIndex());

        Vec2 player1BallPosition = level.getLevelState().getBalls().get(0).getPosition();
        Vec2 originalPosition = new Vec2(player1BallPosition);

        stepTimes(100);

        Assert.assertEquals(2, player2.getLives());
        Assert.assertEquals(3, player1.getLives());
        Assert.assertEquals(originalPosition, player1BallPosition);
    }

    @Test
    public void testStarting2Balls() {
        game.initStartingLevel(21, ALL_LEVELS_UNLOCKED);

        DummyConnection con1 = (DummyConnection) player1.getConnection();

        level = game.getCurrentLevel();

        game.movePaddle(player1.getUsername(), 200, 0);
        game.movePaddle(player2.getUsername(), 200, 0);

        level.startBall(player1.getIndex());
        level.startBall(player2.getIndex());

        float ball1 = level.getLevelState().getBalls().get(0).getY();
        float ball2 = level.getLevelState().getBalls().get(1).getY();

        stepTimes(10);

        List<Ball> balls = level.getLevelState().getBalls();
        Assert.assertEquals(ball1 + 10, balls.get(0).getPosition().y, 1);
        Assert.assertEquals(ball2 - 10, balls.get(1).getPosition().y, 1);
    }

    @Test
    public void resetBall() {
        game.initStartingLevel(21, ALL_LEVELS_UNLOCKED);

        DummyConnection con1 = (DummyConnection) player1.getConnection();

        level = game.getCurrentLevel();

        game.movePaddle(player1.getUsername(), 200, 0);
        game.movePaddle(player2.getUsername(), 200, 0);

        level.startBall(player1.getIndex());

        stepTimes(100);

        List<Ball> balls = level.getLevelState().getBalls();

        Assert.assertEquals(2, balls.size());
    }

    @Test
    public void levelEndsWhenOnePlayerIsOutOfLives() {
        game.initStartingLevel(21, ALL_LEVELS_UNLOCKED);

        DummyConnection con1 = (DummyConnection) player2.getConnection();

        level = game.getCurrentLevel();

        game.movePaddle(player1.getUsername(), 200, 0);
        game.movePaddle(player2.getUsername(), 200, 0);

        for (int i = 0; i < 3; i++) {
            level.startBall(player2.getIndex());
            stepTimes(100);
        }

        Assert.assertEquals(0, player2.getLives());
        Assert.assertEquals(3, player1.getLives());

    }

    @Test
    public void testRemovePlayer() {
        game.initStartingLevel(21, ALL_LEVELS_UNLOCKED);

        level = game.getCurrentLevel();
        
        game.movePaddle(player1.getUsername(), 200, 0);
        game.movePaddle(player2.getUsername(), 200, 0);
        
        game.removePlayer(player1.getUsername());
        
        game.addConnectingPlayer(player1);
        game.addConnectionForPlayer(player1.getUsername(), new DummyConnection());
        
        game.movePaddle(player1.getUsername(), 200, 0);
        game.movePaddle(player2.getUsername(), 200, 0);

    }
    
    @Test
    public void testPlayer2FloorActivation(){
        game.initStartingLevel(23, ALL_LEVELS_UNLOCKED);

        level = game.getCurrentLevel();
        
        game.movePaddle(player1.getUsername(), 200, 0);
        game.movePaddle(player2.getUsername(), 200, 0);
        
        LevelState state = level.getLevelState();
        
        level.startBall(player1.getIndex());
        level.startBall(player2.getIndex());
        
        stepTimes(30);
        
        BreakoutPowerUpHandler bpuh = level.getPoweruphandler();
        String powerupName = bpuh.getPowerUps().get(0).getName();
        
        game.triggerPowerup(powerupName, 0);
        
        stepTimes(20);
        
        Assert.assertEquals(2, player1.getLives());
        Assert.assertEquals(3, player2.getLives());
    }

    private void stepTimes(int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
