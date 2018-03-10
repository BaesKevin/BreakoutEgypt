/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.connectionmanagement.DummyConnection;
import com.breakoutegypt.data.DummyLevelProgressionRepository;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author kevin
 */
public class ThreadingTest {
    private Game game;
    private Level level;
    private Player player;
    private final LevelProgress ALL_LEVELS_UNLOCKED = DummyLevelProgressionRepository.getDefault(GameType.TEST);
    
    @Before
    public void setup() {
        createGame(GameType.TEST, "medium", 1);
    }
    
    private void createGame(GameType type, String diff, int startingLevel){
        player = new Player("player");
        GameManager gm = new GameManager();
        String id = gm.createGame(GameType.TEST, Difficulty.MEDIUM);
        game = gm.getGame(id);
        game.addConnectingPlayer(player);
        game.addConnectionForPlayer(player.getUsername(), new DummyConnection());
        
        game.initStartingLevel(startingLevel, ALL_LEVELS_UNLOCKED);
        
        level = game.getCurrentLevel();

        for (int i = 0; i < 1000; i++) {
            ALL_LEVELS_UNLOCKED.incrementHighestLevelReached();
        }
    }
    
    @Test
    public void testMovingALot(){
        level.start();
        int x = 50;
        int y = 50;
        int amount = 10000000;
        
        
        for (int i = 0; i < amount; i++) {
            game.movePaddle("player", x, y);
            
            x = (int)Math.floor( Math.random() * 100 ) + 1;
            y = (int)Math.floor( Math.random() * 100 ) + 1;
        }
        
    }

}
