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
    private int currentTargetBlocks;
    private LevelFactory levelFactory;
    private SessionManager manager;    

    public Game() {
        id = ID++;        
        manager = new SessionManager();        
        levelFactory = new LevelFactory(this);
        
        currentTargetBlocks = 1;
        currentLevel = levelFactory.getLevel(currentTargetBlocks);
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
    
    public void startLevel() {
        System.out.println("Starting from startLevel");
        this.currentLevel.start();
    }
    
    public void stopLevel() {
        currentLevel.stop();
    }
    
    public void nextLevel() {
        currentTargetBlocks++;
        System.out.println("Starting from nextLevel");
        System.out.println("currentTargetBlocks: " + currentTargetBlocks);
        currentLevel = levelFactory.getLevel(currentTargetBlocks);
        manager.notifyLevelComplete(currentLevel);
        currentLevel.start();
    }
}
