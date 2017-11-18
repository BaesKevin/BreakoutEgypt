/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.connectionmanagement;

import com.breakoutegypt.domain.Player;
import java.io.IOException;
import javax.json.JsonObject;
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
}
