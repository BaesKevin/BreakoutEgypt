/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.connectionmanagement;

import com.breakoutegypt.domain.messages.BallMessageType;
import com.breakoutegypt.domain.messages.LifeMessageType;
import com.breakoutegypt.domain.messages.Message;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author kevin
 */
public class DummyConnection implements PlayerConnection {

    private List<JsonObject> jsonMessages = new ArrayList();
    private List<Message> ballMessages = new ArrayList();
    private List<Message> lifeMessages = new ArrayList();
    private JsonArray ballPositionMessages = Json.createArrayBuilder().build();
    private JsonArray brickMessages = Json.createArrayBuilder().build();
    private JsonArray powerupMessages = Json.createArrayBuilder().build();
    private JsonArray powerdownMessages = Json.createArrayBuilder().build();

    public JsonArray getBallPositionMessages() {
        return ballPositionMessages;
    }

    public JsonArray getBrickMessages() {
        return brickMessages;
    }

    public JsonArray getPowerupMessages() {
        return powerupMessages;
    }

    public JsonArray getPowerdownMessages() {
        return powerdownMessages;
    }

    @Override
    public void send(JsonObject json) {
        jsonMessages.add(json);
    }

    public List<JsonObject> getJsonMessages() {
        return jsonMessages;
    }

    public List<Message> getBallMessages() {
        return ballMessages;
    }

    public void clearBallMessages() {
        ballMessages.clear();
    }

    public List<Message> getLifeMessages() {
        return lifeMessages;
    }

    @Override
    public void send(Message msg) {
        if (msg.getMessageType().equals(LifeMessageType.PLAYING) || msg.getMessageType().equals(LifeMessageType.GAMEOVER)) {
            lifeMessages.add(msg);
        }
        this.send(msg.toJson().build());
    }

    @Override
    public void send(Map<String, JsonArray> msgs) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        if (msgs.containsKey("ballpositions")) {
            ballPositionMessages = addAll(ballPositionMessages, msgs.get("ballpositions"));
            job.add("ballpositions", msgs.get("ballpositions"));
        }
        if (msgs.containsKey("brickactions")) {
            brickMessages = addAll(brickMessages, msgs.get("brickactions"));
            job.add("brickactions", msgs.get("brickactions"));
        }
        if (msgs.containsKey("powerupactions")) {
            powerupMessages = addAll(powerupMessages, msgs.get("powerupactions"));
            job.add("powerupactions", msgs.get("powerupactions"));
        }
        if (msgs.containsKey("powerdownactions")) {
            powerdownMessages = addAll(powerdownMessages, msgs.get("powerdownactions"));
            job.add("powerdownactions", msgs.get("powerdownactions"));
        }
        jsonMessages.add(job.build());
    }

    private JsonArray addAll(JsonArray original, JsonArray arrayToAdd) {
        JsonArrayBuilder result = Json.createArrayBuilder();
        for (int i = 0; i < original.size(); i++) {
            result.add(original.get(i));
        }
        for (int i = 0; i < arrayToAdd.size(); i++) {
            result.add(arrayToAdd.get(i));
        }
        return result.build();
    }

    @Override
    public void send(List<Message> msgs) {
        ballMessages.addAll(msgs);
    }
}
