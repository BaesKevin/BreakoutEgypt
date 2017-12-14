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
import com.breakoutegypt.domain.levelprogression.GameDifficulty;
import com.breakoutegypt.domain.messages.BallPositionMessage;
import com.breakoutegypt.domain.messages.LevelMessage;
import com.breakoutegypt.domain.messages.LevelMessageType;
import com.breakoutegypt.domain.messages.LifeMessage;
import com.breakoutegypt.domain.messages.LifeMessageType;
import com.breakoutegypt.domain.messages.Message;
import com.breakoutegypt.domain.messages.PowerDownMessage;
import com.breakoutegypt.domain.messages.ProjectilePositionMessage;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Projectile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Keeps track of connected players in a game
 *
 * @author kevin
 */
public class SessionManager {

    private int maxPlayers;
    private Set<Player> connectedPlayers;
    private Set<Player> connectingPlayers;

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
    
    public void incrementLevelReachedForAllPlayers(GameType gameType, GameDifficulty difficulty) {
        for(Player p : getPlayers()){
            p.getProgressions().incrementHighestLevel(gameType, difficulty);
        }
    }
    
    public void notifyLevelComplete(Level currentLevel) {
        long timeScore = currentLevel.getScoreTimer().getDuration();
        int brickScore = currentLevel.getBrickScore() - (int) timeScore;
        
        LevelMessage lm = new LevelMessage("jef", currentLevel.isLastLevel(), timeScore, brickScore, LevelMessageType.COMPLETE);
        sendJsonToPlayers(lm);
    }

    public void notifyPlayers(Level currentLevel, ServerClientMessageRepository messageRepo) {
        Map<String, List<Message>> messages = createMessageMap(currentLevel, messageRepo);

        sendJsonToPlayers(messages);
        messageRepo.clearBrickMessages();
        messageRepo.clearPowerupMessages();
        messageRepo.clearPowerdownMessages();
        currentLevel.getLevelState().clearMessages();
    }

    public void notifyPlayersOfLivesLeft(Level currentLevel) {
        boolean noLivesLeft = currentLevel.noLivesLeft();
        //TODO User uit session halen en meegeven ipv 'jef'
        Message lifeMessage;
        if (noLivesLeft) {
            lifeMessage = new LifeMessage("jef", 0, LifeMessageType.GAMEOVER);
        } else {
            lifeMessage = new LifeMessage("jef", currentLevel.getLives(), LifeMessageType.PLAYING);
        }
        sendJsonToPlayers(lifeMessage);
    }

    public void notifyPlayersOfBallAction(Level currentLevel) {
        List<Message> ballMessages = currentLevel.getLevelState().getMessages();
        for (Message msg : ballMessages) {
            sendJsonToPlayers(msg);
        }
        currentLevel.getLevelState().clearMessages();
    }

    private Map<String, List<Message>> createMessageMap(Level currentLevel, ServerClientMessageRepository messageRepo) {

        Map<String, List<Message>> messages = new HashMap<>();
        List<Ball> balls = currentLevel.getLevelState().getBalls();
        List<Message> ballPositionMessages = new ArrayList();
        List<Message> brickMessages = messageRepo.getBrickMessages();
        List<Message> powerupMessages = messageRepo.getPowerupMessages();
        List<Message> powerdownMessages = messageRepo.getPowerdownMessages();
        List<Projectile> projectiles = currentLevel.getLevelState().getProjectiles();
        
        for (Projectile p : projectiles) {
            Message m = new ProjectilePositionMessage(p);
            powerdownMessages.add(m);
        }

        for (Ball b : balls) {
            BallPositionMessage bpm = new BallPositionMessage(b);
            ballPositionMessages.add(bpm);
        }
        
        if (powerupMessages.size() > 0) {
            messages.put("powerupactions", powerupMessages);
        }
        
        if (powerdownMessages.size() > 0) {
            messages.put("powerdownactions", powerdownMessages);
        }
        
        if (ballPositionMessages.size() > 0) {
            messages.put("ballpositions", ballPositionMessages);
        }

        if (brickMessages.size() > 0) {
            messages.put("brickactions", brickMessages);
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

    private void sendJsonToPlayers(Map<String, List<Message>> messages) {
        PlayerConnection conn;
        for (Player player : connectedPlayers) {
            conn = player.getConnection();
            conn.send(messages);
        }
    }
}
