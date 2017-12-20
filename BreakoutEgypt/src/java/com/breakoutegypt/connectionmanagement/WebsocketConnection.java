/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.connectionmanagement;

import com.breakoutegypt.domain.messages.Message;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
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
        String[] keys = {"ballpositions", "brickactions", "powerupactions", "powerdownactions", "paddlepositions"};
        
        for(String key : keys){
            if(msgs.containsKey(key)){
                job.add(key, msgs.get(key));
            }
        }
        
        job.add("leveldata", job.build());
        send(job.build());
    }

    @Override
    public void send(List<Message> msgs) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (Message m : msgs) {
            jab.add(m.toJson().build());
        }
        job.add("ballaction", jab.build());
        send(job.build());
    }
}
