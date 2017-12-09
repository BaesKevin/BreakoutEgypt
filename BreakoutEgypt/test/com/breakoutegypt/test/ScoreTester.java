/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.connectionmanagement.DummyConnection;
import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameDifficulty;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.domain.levelprogression.LevelProgression;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author BenDB
 */
public class ScoreTester {
    
    Level level;
    Game game;
    Player player;
    private final LevelProgression ALL_LEVELS_UNLOCKED = new LevelProgression();
    
    @Before
    public void init() {
        GameManager gm = new GameManager();
        int id = gm.createGame(1, 1, GameType.TEST, GameDifficulty.MEDIUM);

        game = gm.getGame(id);

        player = new Player(new User("Kevin"));

        game.addConnectingPlayer(player);

        game.addConnectionForPlayer("Kevin", new DummyConnection());

        game.assignPaddleToPlayer(player);
    }
    
    @Test
    public void testMultiplier() {
        
        // Level with 3 bricks, each 2000 points, multiplier goes up after 2 hits without hitting the paddle or losing a ball/life.
        // Score should be 8000
        
        game.setCurrentLevel(13, ALL_LEVELS_UNLOCKED);
        level = game.getLevel();
        
        
        
        level.start();
        level.startBall();
        level.getLevelState().getBalls().get(0).setLinearVelocity(0, 100);
        
        stepTimes(level, 120);

        assertEquals(8000, level.getBrickScore());
    }
    
    private void stepTimes(Level level, int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
    
}
