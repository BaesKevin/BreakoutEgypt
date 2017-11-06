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
    private SessionManager manager;
    

    public Game() {
        id = ID++;
        
        manager = new SessionManager();
        
        currentLevel = new LevelFactory(this).getLevel1();

        //currentLevel = new LevelFactory(world.getBox2dWorld()).getLevel1();
        //currentLevel = new LevelFactory().getLevel1();
        //world.setLevel(currentLevel);
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
        this.currentLevel.startLevel();
    }
    
    public void stopLevel() {
        this.currentLevel.stopLevel();
    }
    

    



}
