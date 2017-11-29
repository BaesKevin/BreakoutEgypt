/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.connectionmanagement;

import com.breakoutegypt.domain.messages.Message;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public class WebsocketConnection implements PlayerConnection {

    private final Session session;

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
    public void send(Map<String, List<Message>> msgs) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        if (msgs.containsKey("ballpositions")) {
            job.add("ballpositions", listToJsonArray(msgs.get("ballpositions")));
        }
        if (msgs.containsKey("brickactions")) {
            job.add("brickactions", listToJsonArray(msgs.get("brickactions")));
        }
        job.add("leveldata", job.build());
        send(job.build());
    }

    private JsonArray listToJsonArray(List<Message> msgs) {
        JsonArrayBuilder jab = Json.createArrayBuilder();

        for (Message msg : msgs) {
            jab.add(msg.toJson().build());
        }

        return jab.build();
    }
}
