
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.messages.Message;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

public class ServerClientMessageRepository {

    private List<Message> brickMessages;
    private List<Message> powerupMessages;
    private List<Message> powerdownMessages;

    public ServerClientMessageRepository() {
        this.brickMessages = new ArrayList();
        this.powerupMessages = new ArrayList();
        this.powerdownMessages = new ArrayList();
    }

    public void addBrickMessage(Message m) {
        brickMessages.add(m);
    }

    public JsonArray getBrickMessages() {
        return listToJsonArray(brickMessages);
    }

    private void clearBrickMessages() {
        brickMessages.clear();
    }

    public void addPowerupMessages(Message m) {
        powerupMessages.add(m);
    }

    private void clearPowerupMessages() {
        powerupMessages.clear();
    }

    public JsonArray getPowerupMessages() {
        return listToJsonArray(powerupMessages);
    }

    public void addPowerdownMessages(Message m) {
        powerdownMessages.add(m);
    }

    private void clearPowerdownMessages() {
        powerdownMessages.clear();
    }

    public JsonArray getPowerdownMessages() {
        return listToJsonArray(powerdownMessages);
    }
    
    public void clearAllMessages() {
        clearBrickMessages();
        clearPowerupMessages();
        clearPowerdownMessages();
    }
    
    public JsonArray listToJsonArray(List<Message> msgs) {
        JsonArrayBuilder jab = Json.createArrayBuilder();

        for (Message msg : msgs) {
            jab.add(msg.toJson().build());
        }

        return jab.build();
    }    
    
}
