/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import org.jbox2d.common.Vec2;

/**
 * Keeps track of connected players in a game
 * @author kevin
 */
public class SessionManager {
    private Set<Session> gameSessions;
    private JsonObject json;
    
    public SessionManager(){
        gameSessions  = Collections.synchronizedSet(new HashSet());
    }
    
    public void addPlayer(Session peer) {
        gameSessions.add(peer);
    }

    public void removePlayer(Session peer) {
        gameSessions.remove(peer);
    }
    
    public boolean hasNoPlayers() {
        return gameSessions.size() == 0;
    }
    
    public void notifyPlayers(Level currentLevel, BreakoutWorld simulation) {
        json = createJson(currentLevel, simulation);

        try {
            for (Session peer : gameSessions) {
                if( peer.isOpen())
                    peer.getBasicRemote().sendObject(json);
            }
        } catch (IOException io) {
            io.printStackTrace();
        } catch (EncodeException ee) {
            ee.printStackTrace();
        }
    }

    private JsonObject createJson(Level currentLevel, BreakoutWorld simulation) {
        JsonObjectBuilder job = Json.createObjectBuilder();

        Vec2 position = currentLevel.getBall().getPosition();

        JsonObjectBuilder brickkObjectBuilder = Json.createObjectBuilder();
        brickkObjectBuilder.add("x", position.x);
        brickkObjectBuilder.add("y", position.y);

        job.add("ball", brickkObjectBuilder.build());

        List<String> bodiesToDestroy = simulation.getKeysOfBodiesToDestroy();
        if (0 < bodiesToDestroy.size()) {
            JsonArrayBuilder jab = Json.createArrayBuilder();

            for (String key : bodiesToDestroy) {
                jab.add(key);
            }

            job.add("destroy", jab.build());
        }

        simulation.clearKeysOfBodiesToDestroy();

        return job.build();
    }

   
}
