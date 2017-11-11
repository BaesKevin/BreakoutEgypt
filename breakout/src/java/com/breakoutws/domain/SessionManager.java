/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import org.jbox2d.common.Vec2;

/**
 * Keeps track of connected players in a game
 *
 * @author kevin
 */
public class SessionManager {

    private Set<MultiplayerPeer> peers;
    private JsonObject json;

    public SessionManager() {
        peers = Collections.synchronizedSet(new HashSet());
    }

    public void addPlayer(MultiplayerPeer peer) {
        peers.add(peer);
    }

    public void removePlayer(Session peer) {
        peers.remove(peer);
    }

    public boolean hasNoPlayers() {
        return peers.size() == 0;
    }
    
    public void notifyLevelComplete(Level currentLevel) {
        json = createLevelCompleteJson(currentLevel.getId(), currentLevel.isLastLevel(), currentLevel.getScoreTimer());        
        System.out.println("SessionManager: notifying of level complete");
        sendJsonToPlayers(json);       
    }
    
    public void notifyPlayers(Level currentLevel, BreakoutWorld simulation) {
       // System.out.println("SessionManager: sending json");
        json = createJson(currentLevel, simulation);

        sendJsonToPlayers(json);
    }
    
    
    void notifyPlayersOfLivesLeft(Level currentLevel) {   
        System.out.println("SessionManager: notifying players of lives lfet");
        boolean noLivesLeft = currentLevel.noLivesLeft();
        json = createLivesLeftJson(currentLevel.getLives(), noLivesLeft);
        sendJsonToPlayers(json);        
    }
    
    private JsonObject createLivesLeftJson(int livesLeft, boolean noLivesLeft) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("livesLeft", livesLeft);
        job.add("gameOver", noLivesLeft);
        return job.build();
    }
    
    private JsonObject createLevelCompleteJson(int nextLevel, boolean isLastLevel, ScoreTimer t) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        
        job.add("levelComplete", true);
        job.add("scoreTimer", t.getDuration());
        job.add("isLastLevel", isLastLevel);
        return job.build();        
    }
    
    

    private JsonObject createJson(Level currentLevel, BreakoutWorld simulation) {
        JsonObjectBuilder job = Json.createObjectBuilder();

        Vec2 position = currentLevel.getBall().getPosition();

        JsonObjectBuilder brickkObjectBuilder = Json.createObjectBuilder();        
        brickkObjectBuilder.add("x", position.x);
        brickkObjectBuilder.add("y", position.y);        
        job.add("ball", brickkObjectBuilder.build());
        
                
        JsonArrayBuilder actionsArrayBuilder = Json.createArrayBuilder();
        boolean actionsToBeDone = false;
        
        List<BrickMessage> messages = simulation.getBrickMessages(); 
        for (BrickMessage message : messages) {
            actionsToBeDone = true;
            JsonObjectBuilder actionObjectBuilder = Json.createObjectBuilder();
            actionObjectBuilder.add("action", message.getMessageType().name().toLowerCase());
            actionObjectBuilder.add("name", message.getName());
            actionsArrayBuilder.add(actionObjectBuilder.build());
        }
        simulation.clearBrickMessages();
        
        //BODIES to HIDE
        
        //BODIES to SHOW
        
        if ( actionsToBeDone ) {
            job.add("actions", actionsArrayBuilder.build());       
            System.out.println("SessoinManager: sending bodies to destroy");
        }
        
        simulation.clearBrickMessages();
        
        return job.build();
    }

     private void sendJsonToPlayers(JsonObject json) {
        try {
            for (MultiplayerPeer peer : peers) {
                if (peer.getSession().isOpen()) {
                    peer.getSession().getBasicRemote().sendObject(json);
                }
            }
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }

    MultiplayerPeer getPeerForSession(Session s) {
        MultiplayerPeer peerToFind = null;
        
        for(MultiplayerPeer peer : peers){
            if(peer.getSession().equals(s)){
                peerToFind = peer;
                break;
            }
        }
        
        if( peerToFind == null ){
            System.out.println("No peer found for session");
        }
        
        return peerToFind;
    }

}
