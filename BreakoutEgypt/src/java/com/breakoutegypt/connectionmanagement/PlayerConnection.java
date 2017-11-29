/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.connectionmanagement;

import com.breakoutegypt.domain.messages.Message;
import java.util.List;
import java.util.Map;
import javax.json.JsonObject;

/**
 *
 * @author kevin
 */
public interface PlayerConnection {
    void send(JsonObject json);
    void send(Message msg);
    void send(Map<String, List<Message>> msgs);
}
