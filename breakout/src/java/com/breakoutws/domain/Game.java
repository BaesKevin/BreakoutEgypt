/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import java.util.TimerTask;
import javax.websocket.Session;

/**
 *
 * @author kevin
 */
public class Game{

    private static int ID = 0;
    private int id;
    private Level currentLevel;
   
    private LevelFactory levelFactory;
    private SessionManager manager;    

    public Game() {
        id = ID++;        
        manager = new SessionManager();        
        levelFactory = new LevelFactory(this);       
    
        currentLevel = levelFactory.getCurrentLevel();
    }

    public int getId() {
        return id;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void movePaddle(int x, int y) {
        currentLevel.movePaddle(x, y);
    }
    
    public void addPlayer(Session peer) {
        manager.addPlayer(peer);
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
        
        if(levelFactory.hasNextLevel()){
               currentLevel = levelFactory.getNextLevel();
        }
        manager.notifyLevelComplete(currentLevel);
    }
    
    public boolean hasNextLevel() {
        return levelFactory.hasNextLevel();
    }
}
