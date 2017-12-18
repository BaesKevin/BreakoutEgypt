/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.connectionmanagement.DummyConnection;
import com.breakoutegypt.data.LevelProgressionRepository;
import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.domain.powers.AcidBallPowerUp;
import com.breakoutegypt.domain.powers.BreakoutPowerUpHandler;
import com.breakoutegypt.domain.powers.BrokenPaddlePowerUp;
import com.breakoutegypt.domain.powers.PowerUp;
import com.breakoutegypt.domain.messages.Message;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;
import java.util.List;
import static org.junit.Assert.assertEquals;
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
    private final LevelProgress ALL_LEVELS_UNLOCKED = LevelProgressionRepository.getDefault(GameType.TEST);

    private void createGame(int startingLevel, boolean levelHasPaddle) {
        GameManager gm = new GameManager();
        int id = gm.createGame(GameType.TEST, "medium");
        game = gm.getGame(id);
        game.initStartingLevel(startingLevel, ALL_LEVELS_UNLOCKED);

        level = game.getCurrentLevel();

        for (int i = 0; i < 1000; i++) {
            ALL_LEVELS_UNLOCKED.incrementHighestLevelReached();
        }

        player = new Player(new User("Kevin"));

        game.addConnectingPlayer(player);

        game.addConnectionForPlayer("Kevin", new DummyConnection());
        
        if(levelHasPaddle){
            game.assignPaddleToPlayer(player);
        }
        
    }
    
    @Test
    public void twoBallsBouncOnBrokenPaddle1GoesOutOfBounds() {
        createGame(8, true);
        level = game.getLevel();

        level.startBall();
        List<Ball> balls = level.getLevelState().getBalls();
        for (Ball b : balls) {
            b.setLinearVelocity(0, 100);
        }

        stepTimes(level, 100);
        
        assertTrue(balls.size() == 2);

    }

    @Test
    public void testBrokenPaddleMovement() {
        
        createGame(8, true);
        level = game.getLevel();
        List<Paddle> paddles = level.getLevelState().getPaddles();
        
        int paddleWidth = paddles.get(0).getWidth() ;
        int minimum = paddleWidth / 2;
        int maximum =  BreakoutWorld.DIMENSION - paddleWidth * 2 - BrokenPaddlePowerUp.GAP + paddleWidth / 2 ;
        
        float paddleY = paddles.get(0).getY();

        level.movePaddle(paddles.get(0), 0, 156);
        assertTrue(paddles.get(0).getX() == minimum);
        
        level.movePaddle(paddles.get(0), 120, 156);

        float paddlePositionX = paddles.get(0).getX();
        
        assertTrue(paddlePositionX == maximum);
    }

    @Test
    public void activateFloorPowerUpTest() {
        createGame(10, false);

        level = game.getLevel();
        List<Ball> balls = level.getLevelState().getBalls();
        level.startBall();
        stepTimes(level, 40);

        game.triggerPowerup("floor");
        stepTimes(level, 120);

        DummyConnection conn = (DummyConnection) player.getConnection();

        assertTrue(conn.getPowerupMessages().size() > 0);
        assertTrue(conn.getLifeMessages().isEmpty());
    }

    @Test
    public void activatePowerUpWithExplosiveTest() {
        createGame(11, false);

        level = game.getLevel();

        level.startBall();
        stepTimes(level, 30);

        DummyConnection conn = (DummyConnection) player.getConnection();

        assertTrue(conn.getPowerupMessages().size() > 0);
    }

    @Test
    public void activateBrokenPaddlePowerup() {
        createGame(12, true);
        
        level = game.getLevel();

        level.getLevelState().getBall().setLinearVelocity(0, 100);
        List<Ball> balls = level.getLevelState().getBalls();
        List<Paddle> paddles = level.getLevelState().getPaddles();
        assertTrue(paddles.size() == 1);

        stepTimes(level, 60);
        game.triggerPowerup("brokenpaddle1");

        stepTimes(level, 10);
        paddles = level.getLevelState().getPaddles();
        assertTrue(paddles.size() > 1);

        DummyConnection conn = (DummyConnection) player.getConnection();

        // ball 3 starts at x = 295, there will be no paddle so it should go out of bounds
        // ball 2 is directly above the left paddle and should bounce back up (no remove message)
        assertTrue(balls.get(1).getY() < paddles.get(0).getY());
        assertTrue(conn.getPowerupMessages().size() > 0);
    }

    @Test
    public void testDoubleAcidBallActivation() {
        createGame(14, false);

        level = game.getCurrentLevel();

        BreakoutPowerUpHandler bpuh = level.getPoweruphandler();

        level.getLevelState().getBalls().get(0).setLinearVelocity(0, -100);
        stepTimes(level, 10);

        DummyConnection conn = (DummyConnection) player.getConnection();

        List<PowerUp> powerups = bpuh.getPowerUps();

        level.triggerPowerup(powerups.get(0).getName());
        stepTimes(level, 1);
        level.triggerPowerup(powerups.get(0).getName());

        assertEquals(2, level.getLevelState().getBall().getAcidBall().getRange());
    }

    @Test
    public void testDoubleFloorActivation() {
        createGame(15, false);

        level = game.getCurrentLevel();

        BreakoutPowerUpHandler bpuh = level.getPoweruphandler();

        level.startBall();
        stepTimes(level, 60);

        List<PowerUp> powerups = bpuh.getPowerUps();

        level.triggerPowerup(powerups.get(0).getName());
        int initialTime = level.getLevelState().getFloor().getTimeVisible();
        level.triggerPowerup(powerups.get(0).getName());
        
        assertEquals(initialTime*2, level.getLevelState().getFloor().getTimeVisible());
    }

    @Test
    public void testDoubleBrokenPaddleActivation() {
        createGame(16, true);

        level = game.getCurrentLevel();

        BreakoutPowerUpHandler bpuh = level.getPoweruphandler();

        level.startBall();
        stepTimes(level, 10);

        List<PowerUp> powerups = bpuh.getPowerUps();

        level.triggerPowerup(powerups.get(0).getName());
        int initialTime = level.getPoweruphandler().getPaddlePowerup().getTimeVisible();
        level.triggerPowerup(powerups.get(0).getName());

        assertEquals(initialTime * 2, level.getPoweruphandler().getPaddlePowerup().getTimeVisible());
    }
    
    @Test
    public void testAcidBallNotOnDecoyBall() {
        
    }

    @Test
    public void AcidBallMessageTest() {
        PowerUp p = new AcidBallPowerUp("acidball");

        p.toJson().toString();
    }

    private void stepTimes(Level level, int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
