
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
        
        if(type == GameType.ARCADE){
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

        currentLevel.movePaddle(peer.getPaddle(), x, y);
    }

    public void addPlayer(Session peer, Player player) {
        manager.addPlayer(peer, player);
        int indexOfPaddleToAssign = manager.getPlayers().size() - 1;

        player.setPaddle(currentLevel.getPaddles().get(indexOfPaddleToAssign));
    }

    public void removePlayer(Session peer) {
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
}
