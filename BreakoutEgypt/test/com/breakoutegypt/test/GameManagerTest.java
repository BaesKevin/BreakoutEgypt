/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author kevin
 */
public class GameManagerTest {
    private Game game;
    private GameManager gm;
    private Player player;
    
    @Before
    public void setup(){
        gm = new GameManager();
        int id = gm.createGame(1, 1, GameType.ARCADE);
        game = gm.getGame(id);
        
        player = new Player(new User("player"));
    }
    
    @Test
    public void removeLastPlayerCleansUpGame(){
        
        game.addConnectingPlayer(player);
        
        game.startLevel();
        gm.removePlayer(game.getId(),"player");
        
        Game cleanedUpGame = gm.getGame(game.getId());
        Assert.assertNull(cleanedUpGame);
    }
}