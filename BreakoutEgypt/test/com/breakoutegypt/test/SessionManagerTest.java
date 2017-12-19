/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.connectionmanagement.DummyConnection;
import com.breakoutegypt.connectionmanagement.SessionManager;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author kevin
 */
public class SessionManagerTest {

    private SessionManager sm;
    private Player player1, player2;

    @Before
    public void setup() {
        sm = new SessionManager(2);
        player1 = new Player("player1");
        player2 = new Player("player2");
    }
    
    @Test
    public void testRemovePlayerRemovesFromConnectingAndConnected() {

        sm.addConnectingPlayer(player1);
        sm.removePlayer("player1");

        Player playerFromManager = sm.getPlayer("player1");
        Assert.assertNull(playerFromManager);

    }
    
    @Test
    public void addConnectionMovesPlayerFromConnectingToConnected(){
        sm.addConnectingPlayer(player1);
        Assert.assertTrue(sm.isConnecting("player1"));
        sm.addConnectionForPlayer("player1", new DummyConnection());
        
        Assert.assertFalse(sm.isConnecting("player1"));
    }

    @Test
    public void testMaximumConnections() {
        sm.addConnectingPlayer(player1);
        sm.addConnectingPlayer(player2);
        sm.addConnectionForPlayer("player1", new DummyConnection());
        sm.addConnectionForPlayer("player2", new DummyConnection());
        Assert.assertTrue(sm.isFull());

    }

    @Test
    public void connectionPlayerCountsInPlayerCount() {
        sm.addConnectingPlayer(player1);
        sm.addConnectingPlayer(player2);
        Assert.assertEquals(sm.hasNoPlayers(), false);
        Assert.assertEquals(sm.isFull(), true);
    }

    @Test
    public void getPlayersGivesAllPlayers() {
        sm.addConnectingPlayer(player1);
        sm.addConnectingPlayer(player2);
        
        Assert.assertEquals(sm.getPlayers().size(), 2);
    }
    
    @Test
    public void removePlayerTest(){
        sm.addConnectingPlayer(player1);
        sm.addConnectionForPlayer(player1.getUsername(), new DummyConnection());
        sm.removePlayer(player1.getUsername());
        
        Assert.assertEquals(0, sm.getPlayers().size());
    }
}
