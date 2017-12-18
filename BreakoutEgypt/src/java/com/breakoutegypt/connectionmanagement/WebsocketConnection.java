/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.connectionmanagement;

import com.breakoutegypt.domain.messages.Message;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Session;

/**
 *
 * @author kevin
 */
public class WebsocketConnection implements PlayerConnection, Serializable{

    private final transient Session session;

    public WebsocketConnection(Session session) {
        this.session = session;
    }
    
    @Override
    public void send(JsonObject json) {
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendObject(json);
            }
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(Message msg) {
        send(msg.toJson().build());
    }

    @Override
    public void send(Map<String, JsonArray> msgs) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        if (msgs.containsKey("ballpositions")) {
            job.add("ballpositions", msgs.get("ballpositions"));
        }
        if (msgs.containsKey("brickactions")) {
            job.add("brickactions", msgs.get("brickactions"));
        }
        if (msgs.containsKey("powerupactions")) {
            job.add("powerupactions", msgs.get("powerupactions"));
        } 
        if (msgs.containsKey("powerdownactions")) {
            job.add("powerdownactions", msgs.get("powerdownactions"));
        }
        job.add("leveldata", job.build());
        send(job.build());
    }
}
