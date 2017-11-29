/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.connectionmanagement;

import com.breakoutegypt.domain.messages.BallMessageType;
import com.breakoutegypt.domain.messages.LifeMessageType;
import com.breakoutegypt.domain.messages.Message;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    private List<Message> ballPositionMessages = new ArrayList();
    private List<Message> brickMessages = new ArrayList();

    public List<Message> getBallPositionMessages() {
        return ballPositionMessages;
    }

    public List<Message> getBrickMessages() {
        return brickMessages;
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

    public List<Message> getLifeMessages() {
        return lifeMessages;
    }

    @Override
    public void send(Message msg) {
        if (msg.getMessageType().equals(BallMessageType.REMOVE) || msg.getMessageType().equals(BallMessageType.REMOVE)) {
            ballMessages.add(msg);
        } else if (msg.getMessageType().equals(LifeMessageType.PLAYING) || msg.getMessageType().equals(LifeMessageType.GAMEOVER)) {
            lifeMessages.add(msg);
        }
        this.send(msg.toJson().build());
    }

    @Override
    public void send(Map<String, List<Message>> msgs) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        if (msgs.containsKey("ballpositions")) {
            ballPositionMessages = msgs.get("ballpositions");
            job.add("ballpositions", listToJsonArray(ballPositionMessages));
        }
        if (msgs.containsKey("brickactions")) {
            brickMessages.addAll(msgs.get("brickactions"));
            job.add("brickactions", listToJsonArray(brickMessages));
        }
        jsonMessages.add(job.build());
    }

    private JsonArray listToJsonArray(List<Message> msgs) {
        JsonArrayBuilder jab = Json.createArrayBuilder();

        for (Message msg : msgs) {
            jab.add(msg.toJson().build());
        }

        return jab.build();
    }

}
