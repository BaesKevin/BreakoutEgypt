/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.connectionmanagement;

import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.actionmessages.BrickMessage;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.ScoreTimer;
import com.breakoutegypt.domain.actionmessages.Message;
import com.breakoutegypt.domain.shapes.Ball;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Keeps track of connected players in a game
 *
 * @author kevin
 */
public class SessionManager {

    private int maxPlayers;
    private Set<Player> connectedPlayers;
    private Set<Player> connectingPlayers;
    private JsonObject json;

    public SessionManager() {
        this(1);
    }

    public SessionManager(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        connectedPlayers = Collections.synchronizedSet(new HashSet());
        connectingPlayers = Collections.synchronizedSet(new HashSet());
    }

    public Player getPlayer(String name) {

        Player toFind = getPlayerInSet(name, connectedPlayers);

        if (toFind == null) {
            toFind = getPlayerInSet(name, connectingPlayers);
        }

        return toFind;
    }

    public Player getPlayer(String name, boolean searchInConnectingPlayers) {
        Set<Player> playerSetToSearch;
        if (searchInConnectingPlayers) {
            playerSetToSearch = connectingPlayers;
        } else {
            playerSetToSearch = connectedPlayers;
        }

        Player player = getPlayerInSet(name, playerSetToSearch);
        return player;
    }

    private Player getPlayerInSet(String name, Set<Player> playerset) {
        Player toFind = null;
        for (Player player : playerset) {
            if (player.getUser().getUsername().equals(name)) {
                toFind = player;
            }
        }

        return toFind;
    }

    public void addConnectingPlayer(Player player) {
        connectingPlayers.add(player);
    }

    public boolean isPlayerInSessionManager(Player player) {
        boolean isInManager = connectingPlayers.contains(player) || getPlayers().contains(player);
        System.out.println("SessionManager: Player " + player.getUser().getUsername() + " is in manager: " + isInManager);
        return isInManager;
    }

    public boolean isConnecting(String name) {
        Player player = getPlayer(name, true);
        return player != null;
    }

    public void addConnectionForPlayer(String name, PlayerConnection conn) {
        Player connectingPlayer = getPlayer(name, true);

        if (connectingPlayer != null) {
            connectingPlayers.remove(connectingPlayer);
            if (connectedPlayers.size() < maxPlayers) {
                System.out.printf("SessionManager: Add session for connecting player %s\n", connectingPlayer.getUser().getUsername());

                connectingPlayer.setConnection(conn);
                connectedPlayers.add(connectingPlayer);
            }
        } else {
            System.out.println("SessionManager: trying to addsession for player that doesn't exist");
        }

    }

    public void removePlayer(String username) {
        Player player = getPlayer(username, true);
        connectedPlayers.remove(player);
        connectingPlayers.remove(player);
    }

    public boolean hasNoPlayers() {
        return connectedPlayers.isEmpty() && connectingPlayers.isEmpty();
    }

    public boolean isFull() {
        return connectedPlayers.size() + connectingPlayers.size() == maxPlayers;
    }

    public Set<Player> getPlayers() {
        Set<Player> allPlayers = new HashSet();
        allPlayers.addAll(connectedPlayers);
        allPlayers.addAll(connectingPlayers);

        return allPlayers;
    }

    public int getNextAvailablePaddleIndex() {
        return connectedPlayers.size() + connectingPlayers.size() - 1;
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

    public void notifyPlayersOfLivesLeft(Level currentLevel) {
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
        
        List<Ball> balls = currentLevel.getLevelState().getBalls();
        JsonArrayBuilder ballArrayBuilder = Json.createArrayBuilder();

        JsonObjectBuilder ballObjectBuilder = Json.createObjectBuilder();
        for (Ball b : balls) {
            ballObjectBuilder.add("name", b.getName());
            ballObjectBuilder.add("x", b.getPosition().x);
            ballObjectBuilder.add("y", b.getPosition().y);
            ballArrayBuilder.add(ballObjectBuilder.build());
        }

        job.add("balls", ballArrayBuilder.build());
        
        List<Message> ballMessages = currentLevel.getLevelState().getMessages();
        
        

        JsonArrayBuilder actionsArrayBuilder = Json.createArrayBuilder();

        System.out.println("ballmessages in sessionmanager: " + ballMessages);
        List<Message> messages = simulation.getMessages();
        messages.addAll(ballMessages);
        for (Message message : messages) {
            JsonObjectBuilder actionObjectBuilder = message.toJson();
            actionsArrayBuilder.add(actionObjectBuilder.build());
        }
        simulation.clearMessages();
        currentLevel.getLevelState().clearMessages();

        //BODIES to HIDE
        //BODIES to SHOW
        if (messages.size() > 0) {
            job.add("actions", actionsArrayBuilder.build());
            System.out.println("SessoinManager: sending bodies to destroy");
        }

        simulation.clearMessages();

        return job.build();
    }

    private void sendJsonToPlayers(JsonObject json) {
        PlayerConnection conn;
        for (Player player : connectedPlayers) {
            conn = player.getConnection();
            conn.send(json);
        }
    }

}
