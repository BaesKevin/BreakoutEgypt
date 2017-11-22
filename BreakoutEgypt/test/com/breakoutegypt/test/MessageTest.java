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
import java.util.List;
import javax.json.JsonObject;
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
    public MessageTest() {
    }
    
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
    public void testDummyConnectionGetMessages() {
        game.setCurrentLevel(1);
        level = game.getLevel();
        
        level.startBall();
        
        stepTimes(level, 100);
        
        DummyConnection conn = (DummyConnection) player.getConnection();
        List<JsonObject> messages = conn.getJsonMessages();
        
        System.out.println(messages);
    }
    
     private void stepTimes(Level level, int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
