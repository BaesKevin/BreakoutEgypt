
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.levelprogression.GameDifficulty;
import com.breakoutegypt.connectionmanagement.PlayerConnection;
import com.breakoutegypt.connectionmanagement.SessionManager;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import com.breakoutegypt.domain.messages.PowerUpMessage;
import com.breakoutegypt.levelfactories.MultiplayerLevelFactory;
import com.breakoutegypt.levelfactories.LevelFactory;
import com.breakoutegypt.levelfactories.ArcadeLevelFactory;
import com.breakoutegypt.exceptions.BreakoutException;
import com.breakoutegypt.levelfactories.TestLevelFactory;
import java.util.Set;

/**
 *
 * @author kevin
 */
public class Game {

    private static int ID = 0;
    private int id;
    private Level currentLevel;
    private GameType gameType;
    private GameDifficulty difficultyType;
    private Difficulty difficulty;

//    private int livesLeftInLastLevel;
    private boolean isFirstLevel;

    private LevelFactory levelFactory;

    private SessionManager manager;

    public Game(GameType gameType, GameDifficulty difficulty) {
        this(1, gameType, difficulty);
    }

    public Game(int numberOfPlayers, GameType gameType, GameDifficulty difficultyType) {
        id = ID++;
        this.gameType = gameType;
        this.difficultyType = difficultyType;
        this.difficulty = Repositories.getDifficultyRepository().findByName(difficultyType); // TODO CLONE

        manager = new SessionManager(numberOfPlayers);

        levelFactory = createLevelFactoryForGameType(gameType, difficulty);
        isFirstLevel = true;
    }

    private LevelFactory createLevelFactoryForGameType(GameType gameType, Difficulty difficulty) {
        switch (gameType) {
            case ARCADE:
                return new ArcadeLevelFactory(this, difficulty);
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

    public GameType getGameType() {
        return gameType;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void movePaddle(String username, int x, int y) {
        Player peer = manager.getPlayer(username);

        if (peer != null) {
            currentLevel.movePaddle(peer.getIndex(), x, y);
        } else {
        }
    }

    public void addConnectingPlayer(Player player) {
        if (!manager.isFull()) {
            player.setLives(getInitialLives(player));
            player.getProgressions().addNewProgression(this.gameType, this.difficultyType);
            manager.addConnectingPlayer(player);
        } else {
            throw new BreakoutException("Party already full.");
        }
    }

    public boolean isPlayerInSessionManager(Player player) {
        return manager.isPlayerInSessionManager(player);
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

    public void notifyPlayersOfLivesLeft(int playerIndex) {
        Player player = manager.getPlayer(playerIndex);

        manager.notifyPlayersOfLivesLeft(player);
        if (player.noLivesLeft()) {
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

    public void initStartingLevel(int startingLevelId, LevelProgress progress) {
        if (this.currentLevel == null) {
            levelFactory.setCurrentLevel(startingLevelId, progress);
            this.currentLevel = levelFactory.getCurrentLevel();
        } else {
            throw new BreakoutException("This game has been initialized. Use initNextLevel or create a new game to go to an arbitrary level.");
        }
    }

    public void initNextLevel() {
        isFirstLevel = false;

        Set<Player> players = manager.getPlayers();
        for (Player player : players) {
            player.setLives(getInitialLives(player));
        }

        manager.notifyLevelComplete(currentLevel);

        if (levelFactory.hasNextLevel()) {
            manager.incrementLevelReachedForAllPlayers(gameType, difficultyType);
            currentLevel = levelFactory.getNextLevel();
        }

    }

    public boolean hasNextLevel() {
        return levelFactory.hasNextLevel();
    }

    public Player getPlayer(String username) {
        return manager.getPlayer(username);
    }

    public Level getLevel() {
        return levelFactory.getCurrentLevel();
    }

    public int getInitialLives(Player player) {
        if (isFirstLevel || difficulty.isLivesRegenBetweenLevels()) {
            return difficulty.getLives();
        } else {
            return player.getLives();
        }
    }

    public PowerUpMessage triggerPowerup(String powerup) {
        return getCurrentLevel().triggerPowerup(powerup);
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public LevelFactory getLevelFactory() {
        return this.levelFactory;
    }

    void loseLife(int playerIndex) {
        Player player = manager.getPlayer(playerIndex);

        if (this.difficulty.getLives() != Difficulty.INFINITE_LIVES) {
            player.decreaseLives();
        }
    }
}
