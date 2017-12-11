
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.levelprogression.GameDifficulty;
import com.breakoutegypt.connectionmanagement.PlayerConnection;
import com.breakoutegypt.connectionmanagement.SessionManager;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import com.breakoutegypt.domain.messages.PowerUpMessage;
import com.breakoutegypt.levelfactories.MultiplayerLevelFactory;
import com.breakoutegypt.levelfactories.LevelFactory;
import com.breakoutegypt.levelfactories.ArcadeLevelFactory;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.levelfactories.TestLevelFactory;

/**
 *
 * @author kevin
 */
// levels zullen altijd dezelfde initiele LevelState bevatten, 
// voorstel: Game maakt een DiffucultyConfiguration obv Difficulty enum en geeft die door aan 
// LevelFactory die hem dan weer doorgeeft aan Level. 
// obv dat object kan dan bv. in Level startBall de snelheid van een bal worden uitgelezen, 
// bij overgang van levels gekeken worden met hoeveel levens een level moet starten, etc...
public class Game {

    private static int ID = 0;
    private int id;
    private Level currentLevel;
    private GameType gameType;
    private GameDifficulty difficulty;
    
    private LevelFactory levelFactory;

    private SessionManager manager;
    
    public Game(int numberOfPlayers, int startingLevel, GameType gameType, GameDifficulty difficulty/*, LevelProgress progression*/) {
        id = ID++;
        this.gameType = gameType;
        this.difficulty = difficulty;
        
        manager = new SessionManager(numberOfPlayers);

        levelFactory = createLevelFactoryForGameType(gameType, difficulty);
//        levelFactory.setCurrentLevel(startingLevel/*, progression*/);
//        currentLevel = levelFactory.getCurrentLevel();
        
    }

    // hier zou je dan je DiffuciltyConfig aanmaken
    private LevelFactory createLevelFactoryForGameType(GameType gameType, GameDifficulty difficulty) {
        switch (gameType) {
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
    
    public GameType getGameType(){ return gameType; }

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
        player.getProgressions().addNewProgression(this.gameType, this.difficulty);
        manager.addConnectingPlayer(player);
        
    }

    public boolean isPlayerInSessionManager(Player player) {
        return manager.isPlayerInSessionManager(player);
    }

    public void assignPaddleToPlayer(Player player) {
        int indexOfPaddleToAssign = manager.getNextAvailablePaddleIndex();
        //currentLevel is null
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

    public void notifyPlayers(Level currentLevel, ServerClientMessageRepository messageRepo) {
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
            manager.incrementLevelReachedForAllPlayers(gameType, difficulty);
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

    
    public void setCurrentLevel(int levelId, LevelProgress progression) {
        levelFactory.setCurrentLevel(levelId, progression);
        this.currentLevel = levelFactory.getCurrentLevel();
    }

    public PowerUpMessage triggerPowerup(String powerup) {
        return getCurrentLevel().triggerPowerup(powerup);
    }
}
