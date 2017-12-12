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
import com.breakoutegypt.domain.levelprogression.GameDifficulty;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
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
    private final LevelProgress ALL_LEVELS_UNLOCKED = LevelProgressionRepository.getDefault(GameType.TEST);
    
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
    public void testMultiplier() {
        
        // Level with 3 bricks, each 2000 points, multiplier goes up after 2 hits without hitting the paddle or losing a ball/life.
        // Score should be 8000
        createGame(13);
//        game.setCurrentLevel(13, ALL_LEVELS_UNLOCKED);
        level = game.getLevel();
        
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
