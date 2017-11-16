
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import com.breakoutws.domain.shapes.Paddle;
import java.util.List;
import javax.websocket.Session;

/**
 *
 * @author kevin
 */
public class Game {

    private static int ID = 0;
    private int id;
    private Level currentLevel;

    private LevelFactory levelFactory;
    private SessionManager manager;

    public Game(int numberOfPlayers, GameType type) {
        id = ID++;
        manager = new SessionManager(numberOfPlayers);

        if (type == GameType.ARCADE) {
            levelFactory = new ArcadeLevelFactory(this);
        } else {
            levelFactory = new MultiplayerLevelFactory(this);
        }
        currentLevel = levelFactory.getCurrentLevel();
    }

    public int getId() {
        return id;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void movePaddle(Session s, int x, int y) {
        Player peer = manager.getPlayer(s);

        if (peer != null) {
            currentLevel.movePaddle(peer.getPaddle(), x, y);
        } else {
            System.out.println("Game: trying to move paddle for player that doesn't exist");
        }
    }

    public void addConnectingPlayer(Player player) {

        if (!manager.isPlayerInSessionManager(player)) {
            System.out.printf("Game %d: Add connecting player %s\n", id, player.getUser().getUsername());
            manager.addConnectingPlayer(player);
        }

    }

    public boolean isPlayerInSessionManager(Player player) {
        return manager.isPlayerInSessionManager(player);
    }

    public void assignPaddleToPlayer(Player player) {
        int indexOfPaddleToAssign = manager.getNextAvailablePaddleIndex();
        Paddle paddleToAssign = currentLevel.getPaddles().get(indexOfPaddleToAssign);
        System.out.println("Name of assigned paddle: " + paddleToAssign.getName());
        player.setPaddle(paddleToAssign);
    }

    public void addSessionForPlayer(Player player, Session session) {
        if (manager.isConnecting(player)) {
            manager.addSessionForPlayer(player, session);
        }
    }

    public void removePlayer(Session peer) {
        System.out.printf("Game %d: remove player\n", id);
        manager.removePlayer(peer);
    }

    public boolean hasNoPlayers() {
        return manager.hasNoPlayers();
    }

    public void notifyPlayers(Level currentLevel, BreakoutWorld simulation) {
        manager.notifyPlayers(currentLevel, simulation);
    }

    public void notifyPlayersOfLivesLeft() {

        manager.notifyPlayersOfLivesLeft(currentLevel);
        if (currentLevel.noLivesLeft()) {
            currentLevel.stop();
        }

    }

    public void startLevel() {
        this.currentLevel.start();
    }

    public void stopLevel() {
        currentLevel.stop();
    }

    // TODO check if last level was reached
    public void initNextLevel() {
        System.out.printf("level %d complete, intializing next level ", currentLevel.getId());

        manager.notifyLevelComplete(currentLevel);

        if (levelFactory.hasNextLevel()) {
            currentLevel = levelFactory.getNextLevel();
        }

    }

    public boolean hasNextLevel() {
        return levelFactory.hasNextLevel();
    }

    public Player getPlayer(Player player) {
        return manager.getPlayer(player);
    }
}
