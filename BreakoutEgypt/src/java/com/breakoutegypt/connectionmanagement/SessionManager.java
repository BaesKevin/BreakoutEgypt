/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.connectionmanagement;

import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.ServerClientMessageRepository;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.messages.BallPositionMessage;
import com.breakoutegypt.domain.messages.LevelMessage;
import com.breakoutegypt.domain.messages.LevelMessageType;
import com.breakoutegypt.domain.messages.LifeMessage;
import com.breakoutegypt.domain.messages.LifeMessageType;
import com.breakoutegypt.domain.messages.Message;
import com.breakoutegypt.domain.messages.PaddlePositionMessage;
import com.breakoutegypt.domain.messages.ProjectilePositionMessage;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.Projectile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.json.JsonArray;

/**
 * Keeps track of connected players in a game
 *
 * @author kevin
 */
public class SessionManager {

    private int maxPlayers;
    private Set<Player> connectedPlayers;
    private Set<Player> connectingPlayers;

    private Map<Integer, Player> indexToPlayerMap;

    public SessionManager() {
        this(1);
    }

    public SessionManager(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        connectedPlayers = Collections.synchronizedSet(new HashSet());
        connectingPlayers = Collections.synchronizedSet(new HashSet());
        indexToPlayerMap = Collections.synchronizedMap(new HashMap());
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
    
     public Player getPlayer(int playerIndex) {
        Player toFind = null;
        for (Player player : connectedPlayers) {
            if (player.getIndex() == playerIndex) {
                toFind = player;
            }
        }

        return toFind;
    }

    private Player getPlayerInSet(String name, Set<Player> playerset) {
        Player toFind = null;
        for (Player player : playerset) {
            if (player.getUsername().equals(name)) {
                toFind = player;
            }
        }

        return toFind;
    }

    public void addConnectingPlayer(Player player) {
        connectingPlayers.add(player);

        int highestIndex = -1;
        
        Set<Integer> keysInMap = indexToPlayerMap.keySet();
        
        for (int index = 1; index <= maxPlayers; index++) {
            if (!keysInMap.contains(index)) {
                highestIndex = index;
                break;
            }
        }
        
        int playerIndex = highestIndex;

        indexToPlayerMap.put(playerIndex, player);
        player.setIndex(playerIndex);
    }

    public boolean isPlayerInSessionManager(Player player) {
        boolean isInManager = connectingPlayers.contains(player) || getPlayers().contains(player);
        return isInManager;
    }

    public boolean isConnecting(String name) {
        Player player = getPlayer(name, true);
        return player != null;
    }

    public void addConnectionForPlayer(String name, PlayerConnection conn) {
        Player connectingPlayer = getPlayer(name, true);
        if (connectingPlayer != null) {

            if (connectedPlayers.size() < maxPlayers) {

                connectingPlayer.setConnection(conn);
                connectedPlayers.add(connectingPlayer);

            }
            connectingPlayers.remove(connectingPlayer);
        } else {
        }

    }

    public void removePlayer(String username) {
        Player player = getPlayer(username);
        connectedPlayers.remove(player);
        connectingPlayers.remove(player);
        indexToPlayerMap.remove(player.getIndex(), player);
    }

    public boolean hasNoPlayers() {
        return connectedPlayers.isEmpty() && connectingPlayers.isEmpty();
    }

    public boolean isFull() {
        return connectedPlayers.size() + connectingPlayers.size() == maxPlayers;
    }
    
    public boolean hasMaxPlayers(){
        return connectedPlayers.size()==this.maxPlayers;
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

    public void incrementLevelReachedForAllPlayers(GameType gameType, Difficulty difficulty) {
        for (Player p : getPlayers()) {
            p.getProgressions().incrementHighestLevel(gameType, difficulty.getName());
        }
    }

    public void notifyLevelComplete(Level currentLevel, int winnerIndex) {
        Player winner = getPlayer(winnerIndex);
        
        long timeScore = currentLevel.getScoreTimer().getDuration();
        int brickScore = currentLevel.getBrickScore() - (int) timeScore;

        LevelMessage lm = new LevelMessage(winner.getUsername(), currentLevel.isLastLevel(), timeScore, brickScore, LevelMessageType.COMPLETE);
        sendJsonToPlayers(lm);
    }

    public void notifyPlayers(Level currentLevel, ServerClientMessageRepository messageRepo) {

        Map<String, JsonArray> messages = createMessageMap(currentLevel, messageRepo);

        sendJsonToPlayers(messages);
        messageRepo.clearAllMessages();
        currentLevel.getLevelState().clearMessages();
    }

    public void notifyPlayersOfLivesLeft(Player player) {
//        boolean noLivesLeft = currentLevel.noLivesLeft();
        boolean noLivesLeft = player.noLivesLeft();
        //TODO User uit session halen en meegeven ipv 'jef'
        Message lifeMessage;
        if (noLivesLeft) {
            lifeMessage = new LifeMessage(player.getUsername(), 0, LifeMessageType.GAMEOVER, player.getIndex());
        } else {
            lifeMessage = new LifeMessage(player.getUsername(), player.getLives(), LifeMessageType.PLAYING, player.getIndex());
        }
        sendJsonToPlayers(lifeMessage);
    }

    public void notifyPlayersOfBallAction(Level currentLevel) {
        List<Message> ballMessages = currentLevel.getLevelState().getMessages();
        sendJsonToPlayers(ballMessages);
        currentLevel.getLevelState().clearMessages();
    }

    private Map<String, JsonArray> createMessageMap(Level currentLevel, ServerClientMessageRepository messageRepo) {

        Map<String, JsonArray> messages = new HashMap<>();
        List<Ball> balls = currentLevel.getLevelState().getBalls();
        List<Paddle> paddles = currentLevel.getLevelState().getPaddles();
        
        List<Message> ballPositionMessages = new ArrayList();
        List<Message> paddlePositionMessages = new ArrayList();
        
        List<Projectile> projectiles = currentLevel.getLevelState().getProjectiles();

        for (Projectile p : projectiles) {
            Message m = new ProjectilePositionMessage(p);
            messageRepo.addPowerdownMessages(m);
        }

        for (Ball b : balls) {
            BallPositionMessage bpm = new BallPositionMessage(b);
            ballPositionMessages.add(bpm);
        }
        
        for(Paddle p : paddles){
            paddlePositionMessages.add(new PaddlePositionMessage(p));
        }

        JsonArray powerupmessages = messageRepo.getPowerupMessages();
        if (powerupmessages.size() > 0) {
            messages.put("powerupactions", powerupmessages);
        }

        JsonArray powerdownmessages = messageRepo.getPowerdownMessages();
        if (powerdownmessages.size() > 0) {
            messages.put("powerdownactions", powerdownmessages);
        }

        JsonArray brickmessages = messageRepo.getBrickMessages();
        if (brickmessages.size() > 0) {
            messages.put("brickactions", brickmessages);
        }

        JsonArray ballpositions = messageRepo.listToJsonArray(ballPositionMessages);
        if (ballpositions.size() > 0) {
            messages.put("ballpositions", ballpositions);
        }
        
        JsonArray paddlepositions = messageRepo.listToJsonArray(paddlePositionMessages);
        if (paddlepositions.size() > 0) {
            messages.put("paddlepositions", paddlepositions);
        }

        return messages;
    }

    private void sendJsonToPlayers(Message msg) {

        PlayerConnection conn;
        for (Player player : connectedPlayers) {
            conn = player.getConnection();
            conn.send(msg);
        }
    }

    private void sendJsonToPlayers(Map<String, JsonArray> messages) {
        PlayerConnection conn;
        for (Player player : connectedPlayers) {
            conn = player.getConnection();
            conn.send(messages);
        }
    }

    private void sendJsonToPlayers(List<Message> ballMessages) {
        PlayerConnection conn;
        for (Player player : connectedPlayers) {
            conn = player.getConnection();
            conn.send(ballMessages);
        }
    }
}
