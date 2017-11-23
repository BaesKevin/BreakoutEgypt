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
    public void testDummyConnectionGetMessages() {
        game.setCurrentLevel(5);
        level = game.getLevel();

        level.startBall();

        stepTimes(level, 240);

        DummyConnection conn = (DummyConnection) player.getConnection();
        List<Message> actualMessages = conn.getMessages();
        List<JsonObject> expectedMessages = new ArrayList();

        System.out.println("Messages: " + actualMessages);
        
//        createExpectedMessages(expectedMessages);
        
//        assertEquals(expectedMessages, actualMessages);
    }

    private void createExpectedMessages(List<JsonObject> expectedMessages) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        JsonArrayBuilder actionsArrayBuilder = Json.createArrayBuilder();

        JsonObjectBuilder actionObjectBuilder = new BallMessage("ball2", BallMessageType.REMOVE).toJson();
        actionsArrayBuilder.add(actionObjectBuilder.build());
        job.add("ballactions", actionsArrayBuilder.build());
        expectedMessages.add(job.build());
        job.add("livesLeft", 3);
        job.add("gameOver", false);
        expectedMessages.add(job.build());
        actionsArrayBuilder = Json.createArrayBuilder();
        actionObjectBuilder = new BallMessage("ball", BallMessageType.REMOVE).toJson();
        actionsArrayBuilder.add(actionObjectBuilder.build());
        job.add("ballactions", actionsArrayBuilder.build());
        expectedMessages.add(job.build());
        job.add("livesLeft", 2);
        job.add("gameOver", false);
        expectedMessages.add(job.build());
    }

    private void stepTimes(Level level, int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
