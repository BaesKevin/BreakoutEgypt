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
import com.breakoutegypt.domain.messages.BallMessage;
import com.breakoutegypt.domain.messages.BallMessageType;
import com.breakoutegypt.domain.messages.BrickMessage;
import com.breakoutegypt.domain.messages.BrickMessageType;
import com.breakoutegypt.domain.messages.LifeMessage;
import com.breakoutegypt.domain.messages.LifeMessageType;
import com.breakoutegypt.domain.messages.Message;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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
    public void testDummyConnectionGetBallMessages() {
        game.setCurrentLevel(5);
        level = game.getLevel();

        level.startBall();

        stepTimes(level, 120);

        DummyConnection conn = (DummyConnection) player.getConnection();
        List<Message> actualMessages = conn.getBallMessages();
        List<Message> expectedMessages = new ArrayList();

        expectedMessages.add(new BallMessage("ball2", BallMessageType.REMOVE));
        expectedMessages.add(new BallMessage("ball", BallMessageType.REMOVE));
        System.out.println("Expected: " + expectedMessages);
        System.out.println("Actual: " + actualMessages);
        assertEquals(expectedMessages, actualMessages);
    }

    @Test
    public void testDummyConnectionGetLifeMessages() {
        game.setCurrentLevel(6);
        level = game.getLevel();

        level.startBall();

        stepTimes(level, 120);

        DummyConnection conn = (DummyConnection) player.getConnection();
        List<Message> actualMessages = conn.getLifeMessages();
        List<Message> expectedMessages = new ArrayList();
        System.out.println(actualMessages);
        //TODO get name of actual player in session
        expectedMessages.add(new LifeMessage("jef", 1, LifeMessageType.PLAYING));
        expectedMessages.add(new LifeMessage("jef", 0, LifeMessageType.GAMEOVER));
        assertEquals(expectedMessages, actualMessages);
    }

    @Test
    public void testDummyConnectionNotifyPlayers() {
        game.setCurrentLevel(7);
        level = game.getLevel();

        level.startBall();
        level.getLevelState().getBall().setLinearVelocity(0, -100);

        stepTimes(level, 60);

        DummyConnection conn = (DummyConnection) player.getConnection();
        List<Message> actualMessages = conn.getBrickMessages();
        List<Message> expectedMessages = new ArrayList();
        expectedMessages.add(new BrickMessage("regularbrick", BrickMessageType.DESTROY));
        assertEquals(expectedMessages, actualMessages);
    }

    private void stepTimes(Level level, int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
