/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

    private int maxPlayers;
    private Map<Session, Player> connectedPlayers;
    private List<Player> connectingPlayers;
    private JsonObject json;

    public SessionManager() {
        this(1);
    }

    public SessionManager(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        connectedPlayers = Collections.synchronizedMap(new HashMap());
        connectingPlayers = Collections.synchronizedList(new ArrayList());
    }

    public Player getPlayer(Session session) {
        return connectedPlayers.get(session);
    }
    
     public  Player getPlayer(Player player) {
         List<Player> players = getPlayers();
         Player toFind = null;
         
         for(Player p : players){
             if(p.equals(player)){
                 toFind = p;
             }
         }
         
         return toFind;
    }

    public void addConnectingPlayer(Player player) {
        connectingPlayers.add(player);
    }

    public boolean isPlayerInSessionManager(Player player){
        boolean isInManager =  connectingPlayers.contains(player) || getPlayers().contains(player);
        System.out.println("SessionManager: Player " + player.getUser().getUsername() + " is in manager: " + isInManager);
        return isInManager;
    }
    
    public boolean isConnecting(Player player){
        boolean isInManager =  connectingPlayers.contains(player);
        System.out.println("Player " + player.getUser().getUsername() + " is connecting: ");
        return isInManager;
    }
    
    public void addSessionForPlayer(Player player, Session session) {
        Player connectingPlayer = null;
        for (Player p : connectingPlayers) {
            if (p.equals(player)) {
                connectingPlayer = p;
            }
        }

        if (connectingPlayer != null) {
            connectingPlayers.remove(connectingPlayer);
            if (connectedPlayers.size() < maxPlayers) {
                System.out.printf("SessionManager: Add session for connecting player %s\n", player.getUser().getUsername());
                connectedPlayers.put(session, connectingPlayer);
            }
        } else {
            System.out.println("SessionManager: trying to addsession for player that doesn't exist");
        }

    }

    public void removePlayer(Session peer) {
        connectedPlayers.remove(peer);
        connectingPlayers.remove(peer);
    }

    public boolean hasNoPlayers() {
        return connectedPlayers.size() == 0;
    }

    public boolean isFull() {
        return connectedPlayers.size() == maxPlayers;
    }

    public List<Player> getPlayers() {
        return new ArrayList(connectedPlayers.values());
    }

    public int getNextAvailablePaddleIndex() {
        return connectedPlayers.values().size() + connectingPlayers.size() - 1;
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
        if (actionsToBeDone) {
            job.add("actions", actionsArrayBuilder.build());
            System.out.println("SessoinManager: sending bodies to destroy");
        }

        simulation.clearBrickMessages();

        return job.build();
    }

    private void sendJsonToPlayers(JsonObject json) {
        try {
            Session session;
            for (Entry<Session, Player> entry : connectedPlayers.entrySet()) {
                session = entry.getKey();
                if (session.isOpen()) {
                    session.getBasicRemote().sendObject(json);
                }
            }
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }

}