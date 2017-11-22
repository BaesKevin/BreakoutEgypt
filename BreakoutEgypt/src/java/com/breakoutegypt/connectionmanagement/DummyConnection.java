/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.connectionmanagement;

import com.breakoutegypt.domain.actionmessages.Message;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonObject;

/**
 *
 * @author kevin
 */
public class DummyConnection implements PlayerConnection {

    List<JsonObject> jsonMessages = new ArrayList();
    
    @Override
    public void send(JsonObject json) {
        jsonMessages.add(json);
    }
    
    public List<JsonObject> getJsonMessages() {
        return jsonMessages;
    }
    
}
