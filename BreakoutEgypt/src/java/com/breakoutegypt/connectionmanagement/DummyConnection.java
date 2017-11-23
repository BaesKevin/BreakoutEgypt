/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.connectionmanagement;

import com.breakoutegypt.domain.messages.Message;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonObject;

/**
 *
 * @author kevin
 */
public class DummyConnection implements PlayerConnection {

    List<JsonObject> jsonMessages = new ArrayList();
    List<Message> messages = new ArrayList();
    
    @Override
    public void send(JsonObject json) {
        jsonMessages.add(json);
    }
    
    public List<JsonObject> getJsonMessages() {
        return jsonMessages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void send(Message msg) {
        messages.add(msg);
    }
    
}
