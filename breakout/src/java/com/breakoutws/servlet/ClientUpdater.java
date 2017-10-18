/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.servlet;

import com.breakoutws.domain.Box2dObject;
import java.io.IOException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Session;

/**
 *
 * @author kevin
 */
public class ClientUpdater {

    
    private GameSimulator simulator;
    private JsonObject json;

    public ClientUpdater(GameSimulator simulator) {
        this.simulator = simulator;
    }

    public void notifyClients() {
        json = createJson();
        
        try {
            for (Session peer : BreakoutGameServlet.peers) {

                peer.getBasicRemote().sendObject(json);
            }
        } catch (IOException io) {
            io.printStackTrace();
        } catch (EncodeException ee){
            ee.printStackTrace();
        }

    }
    
    private JsonObject createJson(){
        JsonObjectBuilder job = Json.createObjectBuilder();
        
        for(Box2dObject js : simulator.getBreakoutWorld().getObjects()){
            job.add(js.getJsonKey(), js.toJson());
        }
        
        return job.build();
    }

}
