/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.connectionmanagement.DummyConnection;
import com.breakoutegypt.data.LevelProgressionRepository;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.levelprogression.GameDifficulty;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import com.breakoutegypt.domain.messages.BrickMessage;
import com.breakoutegypt.domain.messages.BrickMessageType;
import com.breakoutegypt.domain.messages.LifeMessage;
import com.breakoutegypt.domain.messages.LifeMessageType;
import com.breakoutegypt.domain.messages.Message;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author BenDB
 */
public class MessageTest {

    Level level;
    Game game;
    Player player;
    private final LevelProgress ALL_LEVELS_UNLOCKED = LevelProgressionRepository.getDefault(GameType.TEST);
    
    public MessageTest() {
    }

    private void createGame(int startingLevel) {
        GameManager gm = new GameManager();
        int id = gm.createGame(GameType.TEST, GameDifficulty.MEDIUM);
        game = gm.getGame(id);
        game.initStartingLevel(startingLevel, ALL_LEVELS_UNLOCKED);

        level = game.getCurrentLevel();

        for (int i = 0; i < 1000; i++) {
            ALL_LEVELS_UNLOCKED.incrementHighestLevelReached();
        }
        
        player = new Player(new User("Kevin"));
 
        game.addConnectingPlayer(player);
 
        game.addConnectionForPlayer("Kevin", new DummyConnection());
    }
 
    @Test
    public void testDummyConnectionGetBallMessages() {
        createGame(5);
        level = game.getLevel();
 
        level.startBall();
 
        stepTimes(level, 120);
 
        DummyConnection conn = (DummyConnection) player.getConnection();
        List<Message> actualMessages = conn.getBallMessages();
        
        assertEquals(101, actualMessages.size());
    }

    @Test
    public void testDummyConnectionGetLifeMessages() {
        createGame(5);
        level = game.getLevel();

        level.startBall();

        stepTimes(level, 240);

        DummyConnection conn = (DummyConnection) player.getConnection();
        List<Message> actualMessages = conn.getLifeMessages();
        List<Message> expectedMessages = new ArrayList();
        //TODO get name of actual player in session
        
        
        Message msg1 = new LifeMessage("jef", 3, LifeMessageType.PLAYING);
        Message msg2 = new LifeMessage("jef", 0, LifeMessageType.GAMEOVER);
        
        for (int i = 0; i < 99; i++) {
            expectedMessages.add(msg1);
        }
        
        expectedMessages.add(msg2);
        
        assertEquals(expectedMessages.size(), actualMessages.size());
    }

    @Test
    public void testDummyConnectionNotifyPlayers() {
        createGame( 7);
        level = game.getLevel();

        level.getLevelState().getBall().setLinearVelocity(0, -100);

        stepTimes(level, 60);

        DummyConnection conn = (DummyConnection) player.getConnection();
        assertEquals(1, conn.getBrickMessages().size());
    }

    private void stepTimes(Level level, int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
