
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.connectionmanagement.PlayerConnection;
import com.breakoutegypt.connectionmanagement.SessionManager;
import com.breakoutegypt.levelfactories.MultiplayerLevelFactory;
import com.breakoutegypt.levelfactories.LevelFactory;
import com.breakoutegypt.levelfactories.ArcadeLevelFactory;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.levelfactories.TestLevelFactory;

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

    public Game(int numberOfPlayers, int startingLevel, GameType gameType) {
        id = ID++;
        manager = new SessionManager(numberOfPlayers);

        levelFactory = createLevelFactoryForGameType(gameType);
       
        setCurrentLevel(startingLevel);
    }

    private LevelFactory createLevelFactoryForGameType(GameType gameType){
        switch(gameType){
            case ARCADE:
                return new ArcadeLevelFactory(this);
            case MULTIPLAYER:
                return new MultiplayerLevelFactory(this);
            case TEST:
                return new TestLevelFactory(this);
        }
        
        return null;

    }
    public int getId() {
        return id;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void movePaddle(String username, int x, int y) {
        Player peer = manager.getPlayer(username);
        
        if (peer != null) {
            currentLevel.movePaddle(peer.getPaddle(), x, y);
        } else {
        }
    }

    public void addConnectingPlayer(Player player) {

        manager.addConnectingPlayer(player);

    }

    public boolean isPlayerInSessionManager(Player player) {
        return manager.isPlayerInSessionManager(player);
    }

    public void assignPaddleToPlayer(Player player) {
        int indexOfPaddleToAssign = manager.getNextAvailablePaddleIndex();
        Paddle paddleToAssign = currentLevel.getLevelState().getPaddles().get(indexOfPaddleToAssign);
        player.setPaddle(paddleToAssign);
    }

    public void addConnectionForPlayer(String name, PlayerConnection conn) {
        if (manager.isConnecting(name)) {
            manager.addConnectionForPlayer(name, conn);
        }
    }

    public void removePlayer(String username) {
        manager.removePlayer(username);
    }

    public boolean hasNoPlayers() {
        return manager.hasNoPlayers();
    }

    public void notifyPlayers(Level currentLevel,ServerClientMessageRepository messageRepo) {
        manager.notifyPlayers(currentLevel, messageRepo);
    }

    public void notifyPlayersOfLivesLeft() {

        manager.notifyPlayersOfLivesLeft(currentLevel);
        if (currentLevel.noLivesLeft()) {
            currentLevel.stop();
        }

    }
    
    public void notifyPlayersOfBallAction() {
        manager.notifyPlayersOfBallAction(currentLevel);
    }

    public void startLevel() {
        this.currentLevel.start();
    }

    public void stopLevel() {
        currentLevel.stop();
    }
    
    public void togglePaused() {
        currentLevel.togglePaused();
    }

    // TODO check if last level was reached
    public void initNextLevel() {
        manager.notifyLevelComplete(currentLevel);

        if (levelFactory.hasNextLevel()) {
            currentLevel = levelFactory.getNextLevel();
        }

    }

    public boolean hasNextLevel() {
        return levelFactory.hasNextLevel();
    }

    public Player getPlayer(String username) {
        return manager.getPlayer(username);
    }

    // TODO validate by keeping track of the player's max reached level
    public Level getLevel() {
        return levelFactory.getCurrentLevel();
    }

    public void setCurrentLevel(int levelId) {
        levelFactory.setCurrentLevel(levelId);
        this.currentLevel = levelFactory.getCurrentLevel();
    }
}
